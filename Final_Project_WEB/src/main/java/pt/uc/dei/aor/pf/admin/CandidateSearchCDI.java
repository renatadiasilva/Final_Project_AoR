package pt.uc.dei.aor.pf.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementImp;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import java.io.Serializable;


@Named
@SessionScoped
public class CandidateSearchCDI implements Serializable {

	private static final long serialVersionUID = 550423548893126270L;

	private static final Logger log = 
			LoggerFactory.getLogger(CandidateSearchCDI.class);

	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private SubmissionEJBInterface submissionEJB;
	
	@EJB
	private SecureMailManagementImp mail;

	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private QuestionEJBInterface questionEJB;

	// search fields
	private String email, fname, lname, keyword;
	private String address, city, phone, country;
	private String course, school, statusSub;
	private Long idPos;
	private ScriptEntity checkScript;
	private boolean needsReason;
	private ScriptEntity script;
	private Map<String,String> availableStatusSub=new HashMap<String, String>();

	private List<UserEntity> ulist;
	private List<SubmissionEntity> submissions;
	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 

	private UserEntity manager;
	private String headerTable, reason;
	
	private SubmissionEntity submission;
	private UserEntity candidate;

	@Inject
	private UserSessionManagement userSession;
	
	public CandidateSearchCDI() {
	}

	public void enterAllCandidates() {
		manager = null;
		ulist = userEJB.findAllCandidatesWithInfo(manager);		
		setHeaderTable("Não existem candidatos.");
		clean();
		candidate = null;
	}

	public void enterMyCandidates() {
		manager = userEJB.find(userSession.getCurrentUserClone().getId());
		ulist = userEJB.findAllCandidatesWithInfo(manager);
		setHeaderTable("Não tem posições ou não tem candidaturas"
				+ " às suas posições.");
		clean();
		candidate = null;
	}

	public void clean() {
		// submission status
		availableStatusSub.put(Constants.STATUS_SUBMITED, 
				Constants.STATUS_SUBMITED);
		availableStatusSub.put(Constants.STATUS_ACCEPTED,
				Constants.STATUS_ACCEPTED);
		availableStatusSub.put(Constants.STATUS_PROPOSAL,
				Constants.STATUS_PROPOSAL);
		availableStatusSub.put(Constants.STATUS_NEGOTIATION,
				Constants.STATUS_NEGOTIATION);
		availableStatusSub.put(Constants.STATUS_REJECTED,
				Constants.STATUS_REJECTED);
		availableStatusSub.put(Constants.STATUS_HIRED,
				Constants.STATUS_HIRED);
		email = fname = lname = keyword = address = city = "";
		phone = country = course = school = "";
		setNeedsReason(false);
		reason="";
	}

	public void cleanAll() {
		clean();
		ulist = new ArrayList<UserEntity>();
		candidate = null;
	}

	public String getDateWithoutTime(Date date) {
		return ftDate.format(date);
	}

	public boolean loadedCandidate() {
		return this.candidate!=null;
	}

	public boolean checkCandidate(UserEntity user){
		if(this.candidate==null)return false;
		if(this.candidate.getId()==user.getId())return true;
		return false;
	}

	public void unloadCandidate(){
		this.candidate=null;
		needsReason = false;
		reason="";
		this.submission = null;
	}
	
	public boolean loadedSubmission() {
		return this.submission!=null;
	}

	public boolean checkSubmission(SubmissionEntity submission){
		if(this.submission==null)return false;
		if(this.submission.getId()==submission.getId())return true;
		return false;
	}

	public void unloadSubmission(){
		this.submission=null;
	}
	
	public void updateStatusSub(){
		log.info("Updating status of submission");
		String oldStatus=this.submission.getStatus();
		boolean update = false;

		log.debug("Current status: "+oldStatus);
		log.debug("New status: "+statusSub);
		
		// if current status is HIRED and new one is not, clean hiredDate
		if(oldStatus.equals(Constants.STATUS_HIRED)
				&&!statusSub.equals(Constants.STATUS_HIRED)) {
			update = true;
			this.submission.setHiredDate(null);
			//updates the number of hired people of position
			PositionEntity pos = this.submission.getPosition();
			int oldhiredP = pos.getHired_people();
			if (oldhiredP == pos.getOpenings()) {
				// open position if hired people equal to openings
				pos.setStatus(Constants.STATUS_OPEN);
				pos.setClosingDate(null);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Há novamente vagas disponíveis "
								+ " na posição. A posição foi"
								+ " aberta automaticamente."));
			}
			oldhiredP--;
			if (oldhiredP < 0) log.debug("Hired people counting is negative!");
			pos.setHired_people(oldhiredP);
			positionEJB.update(pos);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Apagada data de contratação."));
		}
		// if current status is REJECTED and new one isnt, clean rejectedReason
		if(oldStatus.equals(Constants.STATUS_REJECTED)
				&&!statusSub.equals(Constants.STATUS_REJECTED)) {
			update = true;
			this.submission.setRejectReason(null);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Apagado motivo de rejeição."));
		}

		// if current status one of PROP/ON NEGO, and new is SUB/ACCEPTED
		if((oldStatus.equals(Constants.STATUS_PROPOSAL)
				||oldStatus.equals(Constants.STATUS_NEGOTIATION))
				&&(statusSub.equals(Constants.STATUS_SUBMITED)||
						statusSub.equals(Constants.STATUS_ACCEPTED))) {
			update = true;
			this.submission.setProposalDate(null);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Apagada data de proposta."));
		}

		// Se o novo status é ACCEPTED, avisa para marcar entrevista
		if(statusSub.equals(Constants.STATUS_ACCEPTED)
				&&!oldStatus.equals(Constants.STATUS_ACCEPTED)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Não se esqueça de agendar entrevista."));
		} else if((statusSub.equals(Constants.STATUS_PROPOSAL) 
				||statusSub.equals(Constants.STATUS_NEGOTIATION))
				&&(!oldStatus.equals(Constants.STATUS_PROPOSAL) 
						&&!oldStatus.equals(Constants.STATUS_NEGOTIATION)))
			this.submission.setProposalDate(new Date());
		else if(statusSub.equals(Constants.STATUS_REJECTED))
			this.needsReason=true;
		else if(statusSub.equals(Constants.STATUS_HIRED)
				&&!oldStatus.equals(Constants.STATUS_HIRED)) {
			PositionEntity pos=this.submission.getPosition();

			//updates the number of hired people of position
			int oldhiredP = pos.getHired_people();
			oldhiredP++;
			pos.setHired_people(oldhiredP);

			if (oldhiredP == pos.getOpenings()) {
				// close position if hired people equal to openings
				pos.setStatus(Constants.STATUS_CLOSED);
				pos.setClosingDate(new Date());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Todas as vagas da posição"
								+ " foram preenchidas. A posição foi"
								+ " fechada automaticamente."));
			} else if (oldhiredP > pos.getOpenings())
				log.debug("Hired people counting is greater than openings!");

			this.submission.setHiredDate(new Date());
			positionEJB.update(pos);
			this.mail.notifyHired(submission);
		}	

		if(!needsReason) {
			
			this.submission.setStatus(statusSub);
			this.submissionEJB.update(this.submission);

			this.submission=null;
		

			this.clean();

			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage("Estado actualizado."));
		} else if (update) this.submissionEJB.update(this.submission);
		
	}

	public void saveRejectedReason() {
		log.info("Saving rejected reason");
		if(reason!=null&&!reason.isEmpty()) {
			this.submission.setRejectReason(this.reason);
			this.submission.setStatus(Constants.STATUS_REJECTED);
			this.submissionEJB.update(submission);
			this.mail.notifyRejected(submission);
			this.submission=null;
			clean();
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage("Estado actualizado."));
		} else
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Introduza o motivo de rejeição.",""));

	}

	public void searchAllCandidates() {
		this.candidate = null;
		log.info("Searching for all candidates");
		if (manager != null) 
			this.ulist=userEJB.findCandidatesByKeyword("%", manager);
		else this.ulist = userEJB.findAllCandidates();
	}

	public void searchAllCandidatesWithInfo() {
		this.candidate = null;
		log.info("Searching for all candidates with info");
		this.ulist = userEJB.findAllCandidatesWithInfo(manager);
	}

	public void searchCandidatesByEmail() {
		this.candidate = null;
		log.info("Searching for candidates by email");
		String pattern = SearchPattern.preparePattern(email);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByEmail(pattern, manager);
	}	

	public void searchCandidatesByFirstName() {
		this.candidate = null;
		log.info("Searching for candidates by first name");
		String pattern = SearchPattern.preparePattern(fname);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByFirstName(pattern, manager);
	}	

	public void searchCandidatesByLastName() {
		this.candidate = null;
		log.info("Searching for candidates by last name");
		String pattern = SearchPattern.preparePattern(lname);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByLastName(pattern, manager);
	}	

	public void searchCandidatesByAddress() {
		this.candidate = null;
		log.info("Searching for candidates by address");
		String pattern = SearchPattern.preparePattern(address);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByAddress(pattern, manager);
	}	

	public void searchCandidatesByCity() {
		this.candidate = null;
		log.info("Searching for candidates by city");
		String pattern = SearchPattern.preparePattern(city);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCity(pattern, manager);
	}	

	public void searchCandidatesByPhone() {
		this.candidate = null;
		log.info("Searching for candidates by phone");
		String pattern = SearchPattern.preparePattern(phone);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByPhone(pattern, manager);
	}	

	public void searchCandidatesByCountry() {
		this.candidate = null;
		log.info("Searching for candidates by country");
		String pattern = SearchPattern.preparePattern(country);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCountry(pattern, manager);
	}	

	public void searchCandidatesByCourse() {
		this.candidate = null;
		log.info("Searching for candidates by course");
		String pattern = SearchPattern.preparePattern(course);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCourse(pattern, manager);
	}	

	public void searchCandidatesBySchool() {
		this.candidate = null;
		log.info("Searching for candidates by school");
		String pattern = SearchPattern.preparePattern(school);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesBySchool(pattern, manager);
	}	

	public void searchCandidatesByPositionOnly() {
		this.candidate = null;
		log.info("Searching for candidates by position");
		PositionEntity pos = positionEJB.find(idPos);
		if (pos != null) {
			log.debug("Position: "+pos.getPositionCode());
			this.ulist = userEJB.findCandidatesByPosition(pos);
		} else log.error("No position with id "+idPos);
	}	

	public void searchCandidatesByPositionShort() {
		this.candidate = null;
		log.info("Searching for candidates by position and email/name");
		PositionEntity pos = positionEJB.find(idPos);
		if (pos != null) {
			log.debug("Position: "+pos.getPositionCode());
			String pattern1 = SearchPattern.preparePattern(email);
			log.debug("Internal search string (email): "+pattern1);
			String pattern2 = SearchPattern.preparePattern(fname);
			log.debug("Internal search string (first name): "+pattern2);
			String pattern3 = SearchPattern.preparePattern(lname);
			log.debug("Internal search string (last name): "+pattern3);
			this.ulist = userEJB.findCandidatesByPosition(pattern1, 
					pattern2, pattern3, pos);
		}		
	}	

	public void searchCandidatesByPositionLong() {
		this.candidate = null;
		log.info("Searching for candidates by position and more attributes");
		PositionEntity pos = positionEJB.find(idPos);
		if (pos != null) {
			log.debug("Position: "+pos.getPositionCode());
			String pattern1 = SearchPattern.preparePattern(email);
			log.debug("Internal search string (email): "+pattern1);
			String pattern2 = SearchPattern.preparePattern(fname);
			log.debug("Internal search string (first name): "+pattern2);
			String pattern3 = SearchPattern.preparePattern(lname);
			log.debug("Internal search string (last name): "+pattern3);
			String pattern4 = SearchPattern.preparePattern(address);
			log.debug("Internal search string (address): "+pattern4);
			String pattern5 = SearchPattern.preparePattern(city);
			log.debug("Internal search string (city): "+pattern5);
			String pattern6 = SearchPattern.preparePattern(country);
			log.debug("Internal search string (country): "+pattern6);
			String pattern7 = SearchPattern.preparePattern(course);
			log.debug("Internal search string (course): "+pattern7);
			String pattern8 = SearchPattern.preparePattern(school);
			log.debug("Internal search string (school): "+pattern8);
			this.ulist = userEJB.findCandidatesByPosition(pattern1, 
					pattern2, pattern3, pattern4, pattern5, pattern6,
					pattern7, pattern8, pos);
		}		
	}	

	public void searchCandidates() {
		this.candidate = null;
		log.info("Searching for candidates by several attributes");
		String pattern1 = SearchPattern.preparePattern(email);
		log.debug("Internal search string (email): "+pattern1);
		String pattern2 = SearchPattern.preparePattern(fname);
		log.debug("Internal search string (first name): "+pattern2);
		String pattern3 = SearchPattern.preparePattern(lname);
		log.debug("Internal search string (last name): "+pattern3);
		String pattern4 = SearchPattern.preparePattern(address);
		log.debug("Internal search string (address): "+pattern4);
		String pattern5 = SearchPattern.preparePattern(city);
		log.debug("Internal search string (city): "+pattern5);
		String pattern6 = SearchPattern.preparePattern(country);
		log.debug("Internal search string (country): "+pattern6);
		String pattern7 = SearchPattern.preparePattern(course);
		log.debug("Internal search string (course): "+pattern7);
		String pattern8 = SearchPattern.preparePattern(school);
		log.debug("Internal search string (school): "+pattern8);
		this.ulist = userEJB.findCandidates(pattern1, 
				pattern2, pattern3, pattern4, pattern5, pattern6,
				pattern7, pattern8);
	}	

	public void searchCandidatesByKeyword() {
		this.candidate = null;
		log.info("Searching for candidates by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByKeyword(pattern, manager);
	}	
	
	public String getPhones(UserEntity candidate) {
		String home = candidate.getUserInfo().getHomePhone();
		String mobile = candidate.getUserInfo().getMobilePhone();
		
		if (home != null) home = home.replace(" ", "")+", ";
		else home = "";
		if (mobile != null) mobile = mobile.replace(" ", "");
		else mobile = "";
		return home+mobile;
	}

	// getters e setters

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public List<UserEntity> getUlist() {
		return ulist;
	}

	public void setUlist(List<UserEntity> ulist) {
		this.ulist = ulist;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public Long getIdPos() {
		return idPos;
	}

	public void setIdPos(Long idPos) {
		this.idPos = idPos;
	}

	public UserEntity getManager() {
		return manager;
	}

	public void setManager(UserEntity manager) {
		this.manager = manager;
	}

	public String getHeaderTable() {
		return headerTable;
	}

	public void setHeaderTable(String headerTable) {
		this.headerTable = headerTable;
	}

	public String getStatusSub() {
		return statusSub;
	}

	public void setStatusSub(String statusSub) {
		this.statusSub = statusSub;
	}

	public ScriptEntity getCheckScript() {
		return checkScript;
	}

	public void setCheckScript(ScriptEntity checkScript) {
		this.checkScript = checkScript;
	}

	public boolean checkScript(ScriptEntity script){
		if(this.script==null)return false;
		if(this.script.getId()==script.getId())return true;
		return false;
	}

	public String getTypeTextOfQuestion(QuestionEntity question) {
		return questionEJB.getTypeText(question);
	}

	public ScriptEntity getScript() {
		return script;
	}

	public void setScript(ScriptEntity script) {
		this.script = script;
	}

	public Map<String,String> getAvailableStatusSub() {
		return availableStatusSub;
	}

	public void setAvailableStatusSub(Map<String,String> availableStatusSub) {
		this.availableStatusSub = availableStatusSub;
	}

	public List<SubmissionEntity> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<SubmissionEntity> submissions) {
		this.submissions = submissions;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public SubmissionEntity getSubmission() {
		return submission;
	}

	public void setSubmission(SubmissionEntity submission) {
		needsReason = false;
		this.submission = submission;
		this.statusSub = this.submission.getStatus();
	}

	public UserEntity getCandidate() {
		return candidate;
	}

	public void setCandidate(UserEntity candidate) {
		submissions = submissionEJB.findSubmissionsOfCandidate(candidate);
		needsReason = false;
		reason="";
		this.submission = null;
		this.candidate = candidate;
	}

	public boolean isNeedsReason() {
		return needsReason;
	}

	public void setNeedsReason(boolean needsReason) {
		this.needsReason = needsReason;
	}

}
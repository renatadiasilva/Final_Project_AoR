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
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
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
public class PositionSearchCDI implements Serializable {

	private static final long serialVersionUID = 5226923223683191514L;

	private static final Logger log = 
			LoggerFactory.getLogger(PositionSearchCDI.class);

	@Inject
	private UserSessionManagement userSession;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	@EJB
	private ScriptEJBInterface scriptEJB;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private QuestionEJBInterface questionEJB;
	
	@EJB
	private SecureMailManagementImp mail;

	// search fields
	private Date date1, date2;
	private String code, title, status, statusSub;
	private String company, tarea, keyword;
	private String location;
	private boolean coimbra, lisboa, porto, other;
	private List<String> locations;
	private Long idU;
	private Long idPSc;
	private int days;
	private boolean result, needsReason;
	private ScriptEntity checkScript;
	private ScriptEntity script;
	private boolean open, close, onhold;
	private Map<String,String> availableStatus=new HashMap<String, String>();
	private Map<String,String> availableStatusSub=new HashMap<String, String>();

	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 

	private List<PositionEntity> plist;
	private List<SubmissionEntity> submissions;

	private SubmissionEntity submission;
	private PositionEntity position;
	private UserEntity manager;
	private String headerTable, reason;
	
	public PositionSearchCDI() {
	}

	public void enterAllPositions() {
		manager = null;
		plist = positionEJB.findAllOrderByCode();		
		headerTable ="Não existem posições.";
		clean();
	}

	public void enterMyPositions() {
		manager = userEJB.find(userSession.getCurrentUserClone().getId());
		plist = positionEJB.findPositionsManagedByUser(manager);
		headerTable ="Não é gestor de nenhuma posição neste momento.";
		clean();
	}

	public void clean() {
		// position status
		this.availableStatus.put(Constants.STATUS_OPEN, Constants.STATUS_OPEN);
		this.availableStatus.put(Constants.STATUS_ONHOLD, Constants.STATUS_ONHOLD);
		this.availableStatus.put(Constants.STATUS_CLOSED, Constants.STATUS_CLOSED);
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
		code = title = status = company = tarea = keyword = location = "";
		coimbra = porto = lisboa = other = false;
		date1 = date2 = null;
		needsReason = false;
		reason="";
	}

	public void cleanAll() {
		clean();
		plist = new ArrayList<PositionEntity>();
	}

	public List<PositionEntity> directOpenPositions() {
		log.info("Searching for all open positions");
		return this.positionEJB.findOpenPositions();
	}

	public boolean checkIfNotEmpty() {
		return plist != null && !plist.isEmpty();
	}
	
	public boolean checkIfClosed() {		
		return position != null && position.getClosingDate() != null;
	}

	public boolean checkIfNotClosed() {		
		return position != null && position.getClosingDate() == null;
	}

	public String getDateWithoutTime(Date date) {
		return ftDate.format(date);
	}

	public int availableOpenings() {
		if (position != null) 
			return position.getOpenings()-position.getHired_people();
		else return 0;
	}
	
	public boolean loadedPosition() {
		return this.position!=null;
	}

	public boolean checkPosition(PositionEntity position){
		if(this.position==null)return false;
		if(this.position.getId()==position.getId())return true;
		return false;
	}

	public void unloadPosition(){
		this.position=null;
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
		
		//update position variable
		String pattern = SearchPattern.preparePattern(position.getPositionCode());

		List<PositionEntity> findP = positionEJB.findPositionsByCode(pattern,
				null);
		if (findP != null && !findP.isEmpty())
			this.position=findP.get(0);
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

	public void searchAll() {
		log.info("Searching for all positions");
		if (manager != null) 
			plist = positionEJB.findPositionsManagedByUser(manager);
		plist = positionEJB.findAllOrderByCode();		
	}

	public void searchPositionsByCode() {
		log.info("Searching for positions by code");
		String pattern = SearchPattern.preparePattern(code);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByCode(pattern, manager);
	}	

	public void searchPositionsByDate() {
		log.info("Searching for positions between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.plist = positionEJB.findPositionsByDate(date1, date2, manager);
	}	

	public void searchPositionsByTitle() {
		log.info("Searching for positions by title");
		String pattern = SearchPattern.preparePattern(title);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByTitle(pattern, manager);
	}	

	public void searchPositionsByLocationsOne() {
		log.info("Searching for positions with one of the given locations");
		addLocations();
		List<String> pattern = new ArrayList<String>(locations.size());
		log.debug("Locations: ");
		for(String l : locations) {
			log.debug(l);
			pattern.add(SearchPattern.preparePattern(l));
		}
		this.plist = positionEJB.findPositionsByLocationsOne(pattern, manager);
	}	

	public void searchPositionsByLocationsAll() {
		log.info("Searching for positions with all the given locations");
		addLocations();
		log.debug("Locations: ");
		for(String l : locations) log.debug(l);
		this.plist = positionEJB.findPositionsByLocationsAll(locations, manager);
	}	

	public void searchPositionsByStatus() {
		log.info("Searching for positions by status");
		log.debug("Status "+status);
		String pattern = SearchPattern.preparePattern(status);
		this.plist = positionEJB.findPositionsByStatus(pattern, manager);
	}	

	public void searchPositionsByCompany() {
		log.info("Searching for positions by company");
		String pattern = SearchPattern.preparePattern(company);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByCompany(pattern, manager);
	}	

	public void searchPositionsByTechArea() {
		log.info("Searching for positions by tecnical area");
		String pattern = SearchPattern.preparePattern(tarea);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByTechArea(pattern, manager);
	}	

	public void searchPositions() {
		log.info("Searching for positions by several attributes");		
		String pattern1 = SearchPattern.preparePattern(code);
		log.debug("Internal search string (code): "+pattern1);
		String pattern2 = SearchPattern.preparePattern(title);
		log.debug("Internal search string (title): "+pattern2);
		String pattern3 = SearchPattern.preparePattern(location);
		log.debug("Internal search string (location) "+pattern3);
		String pattern4 = SearchPattern.preparePattern(status);
		log.debug("Internal search string (status): "+pattern4);
		String pattern5 = SearchPattern.preparePattern(company);
		log.debug("Internal search string (ccompany): "+pattern5);
		String pattern6 = SearchPattern.preparePattern(tarea);
		log.debug("Internal search string (tech area): "+pattern6);
		this.plist = positionEJB.findPositions(date1, date2, pattern1,
				pattern2, pattern3, pattern4, pattern5, pattern6);
	}	

	public void searchPositionsByManager() {
		log.info("Searching for positions of a manager by several attributes");
		UserEntity manager = userEJB.find(idU);
		if ( (manager != null) && (manager.getRoles().contains("MANAGER")) ) {
			log.debug("Manager "+manager.getFirstName());
			String pattern1 = SearchPattern.preparePattern(code);
			log.debug("Internal search string (code): "+pattern1);
			String pattern2 = SearchPattern.preparePattern(title);
			log.debug("Internal search string (title): "+pattern2);
			String pattern3 = SearchPattern.preparePattern(location);
			log.debug("Internal search string (location) "+pattern3);
			String pattern4 = SearchPattern.preparePattern(status);
			log.debug("Internal search string (status): "+pattern4);
			String pattern5 = SearchPattern.preparePattern(company);
			log.debug("Internal search string (ccompany): "+pattern5);
			String pattern6 = SearchPattern.preparePattern(tarea);
			log.debug("Internal search string (tech area): "+pattern6);
			this.plist = positionEJB.findPositionsByManager(date1, date2,
					pattern1, pattern2, pattern3, pattern4, pattern5, 
					pattern6, manager);
		} else log.error("No manager with id "+idU);
	}	

	public void searchOpenPositions() {
		log.info("Searching for all open positions");
		this.plist = positionEJB.findOpenPositions();
	}

	public void searchPositionsByCandidate() {
		log.info("Searching for positions by candidate");
		UserEntity candidate = userEJB.find(idU);
		if ( (candidate != null) &&
				(candidate.getRoles().contains("CANDIDATE")) ) {
			log.debug("Candidate "+candidate.getFirstName());
			this.plist = positionEJB.findPositionsByCandidate(candidate);
		} else log.error("No candidate with id "+idU);
	}

	public void alreadyCandidateOfPosition() {
		log.info("Checking if candidate is already associated "
				+ "with a positions");
		UserEntity candidate = userEJB.find(idU);
		if ( (candidate != null) && 
				(candidate.getRoles().contains("CANDIDATE")) ) {
			log.debug("Candidate "+candidate.getFirstName());
			PositionEntity position = positionEJB.find(idPSc);
			if (position != null) {
				log.debug("Position "+position.getPositionCode());
				result = positionEJB.alreadyCandidateOfPosition(candidate,
						position);
			} else log.error("No position with id "+idPSc);
		} else log.error("No candidate with id "+idU);
	}

	public void searchCloseToSLAPositions() {
		log.info("Searching for close to SLA positions");
		log.debug("Days before: "+days);
		this.plist = positionEJB.findCloseToSLAPositions(days);
	}

	public void searchAfterSLAPositions() {
		log.info("Searching for positions which passed by SLA");
		this.plist = positionEJB.findAfterSLAPositions();
	}

	public void searchPositionsByKeyword() {
		log.info("Searching for positions by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		if (manager == null)
			this.plist = positionEJB.findPositionsByKeyword(pattern);
		else
			this.plist = positionEJB.findPositionsByKeywordAndManager(pattern,
					manager);			
	}

	public void searchOpenPositionManagedByUser() {
		log.info("Searching for open positions by manager");
		UserEntity manager = userEJB.find(idU);
		if (manager != null && manager.getRoles().contains("MANAGER")) {
			log.debug("Manager "+manager.getFirstName());
			this.plist = positionEJB.findOpenPositionsManagedByUser(manager);
		} else log.error("No manager with id "+idU);
	}

	// getters e setters

	public Date getDate1() {
		if (date1 != null) return date1;
		return new Date();
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		if (date2 != null) return date2;
		return new Date();
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getIdU() {
		return idU;
	}

	public void setIdU(Long idU) {
		this.idU = idU;
	}

	public Long getIdPSc() {
		return idPSc;
	}

	public void setIdPSc(Long id) {
		this.idPSc = id;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public List<PositionEntity> getPlist() {
		return plist;
	}

	public void setPlist(List<PositionEntity> plist) {
		this.plist = plist;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isCoimbra() {
		return coimbra;
	}

	public void setCoimbra(boolean coimbra) {
		this.coimbra = coimbra;
	}

	public boolean isLisboa() {
		return lisboa;
	}

	public void setLisboa(boolean lisboa) {
		this.lisboa = lisboa;
	}

	public boolean isPorto() {
		return porto;
	}

	public void setPorto(boolean porto) {
		this.porto = porto;
	}

	public boolean isOther() {
		return other;
	}

	public void setOther(boolean other) {
		this.other = other;
	}	

	private void addLocations() {
		this.locations = new ArrayList<String>();
		if (coimbra) this.locations.add(Constants.LOCATION_COIMBRA);
		if (lisboa) this.locations.add(Constants.LOCATION_LISBOA);
		if (porto) this.locations.add(Constants.LOCATION_PORTO);
		if (other) this.locations.add(SearchPattern.preparePattern(location));
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

	public ScriptEntity getScript() {
		return script;
	}

	public void setScript(ScriptEntity script) {
		this.script = script;
	}

	public String getTypeTextOfQuestion(QuestionEntity question) {
		return questionEJB.getTypeText(question);
	}

	public UserEntity getManager() {
		return manager;
	}

	public void setManager(UserEntity manager) {
		this.manager = manager;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	public boolean isOnhold() {
		return onhold;
	}

	public void setOnhold(boolean onhold) {
		this.onhold = onhold;
	}

	public String getHeaderTable() {
		return headerTable;
	}

	public void setHeaderTable(String headerTable) {
		this.headerTable = headerTable;
	}

	public Map<String,String> getAvailableStatus() {
		return availableStatus;
	}

	public void setAvailableStatus(Map<String,String> availableStatus) {
		this.availableStatus = availableStatus;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		submissions = submissionEJB.findSubmissionsOfPosition(position);
		this.position = position;
		needsReason = false;
		reason="";
		this.submission = null;
	}

	public List<SubmissionEntity> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<SubmissionEntity> submissions) {
		this.submissions = submissions;
	}

	public Map<String,String> getAvailableStatusSub() {
		return availableStatusSub;
	}

	public void setAvailableStatusSub(Map<String,String> availableStatusSub) {
		this.availableStatusSub = availableStatusSub;
	}

	public String getStatusSub() {
		return statusSub;
	}

	public void setStatusSub(String statusSub) {
		this.statusSub = statusSub;
	}

	public SubmissionEntity getSubmission() {
		return submission;
	}

	public void setSubmission(SubmissionEntity submission) {
		needsReason = false;
		this.submission = submission;
		this.statusSub = this.submission.getStatus();
	}

	public boolean isNeedsReason() {
		return needsReason;
	}

	public void setNeedsReason(boolean needsReason) {
		this.needsReason = needsReason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
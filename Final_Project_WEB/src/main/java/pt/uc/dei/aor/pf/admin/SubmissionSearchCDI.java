package pt.uc.dei.aor.pf.admin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementImp;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;

import java.io.Serializable;


@Named
@SessionScoped
public class SubmissionSearchCDI implements Serializable {

	private static final long serialVersionUID = -1339683317271008191L;

	private static final Logger log = 
			LoggerFactory.getLogger(SubmissionSearchCDI.class);

	@Inject
	private UploadFile uploadFile;

	@Inject
	private UserSessionManagement userManagement;

	@EJB
	private SecureMailManagementImp mail;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;

	// search fields
	private Date date1, date2;
	private String source;
	private Long id;
	private SubmissionEntity submission;
	private String status, reason;
	private boolean needsReason;

	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 

	private List<SubmissionEntity> slist, submissions;

	private SubmissionEntity submissionToEdit;

	private Map<String,String> availableStatus=new HashMap<String, String>();

	public SubmissionSearchCDI() {
	}

	public void clean() {
		// Vai buscar os Status
		availableStatus.put(Constants.STATUS_SUBMITED, 
				Constants.STATUS_SUBMITED);
		availableStatus.put(Constants.STATUS_ACCEPTED,
				Constants.STATUS_ACCEPTED);
		availableStatus.put(Constants.STATUS_PROPOSAL,
				Constants.STATUS_PROPOSAL);
		availableStatus.put(Constants.STATUS_NEGOTIATION,
				Constants.STATUS_NEGOTIATION);
		availableStatus.put(Constants.STATUS_REJECTED,
				Constants.STATUS_REJECTED);
		availableStatus.put(Constants.STATUS_HIRED,
				Constants.STATUS_HIRED);

		this.submissions=submissionEJB.findAll();
		needsReason = false;
		reason="";
	}

	public boolean changeCV(SubmissionEntity submission){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -1);

		Date oneHourAgo=calendar.getTime();

		// Só deixa alterar o CV/Motivation Letter se a submission tem menos de uma hora
		return submission.getDate().after(oneHourAgo);
	}

	public void editSubmission(SubmissionEntity submissionToEdit){
		this.submissionToEdit=submissionToEdit;	
	}

	public void cancelEditSubmission(){
		this.submissionToEdit=null;
	}

	public void uploadCustomCV(FileUploadEvent event){

		UploadedFile customCV=event.getFile();

		// Actualiza a submissão
		this.submissionToEdit.setCustomCV(true);
		this.submissionEJB.update(this.submissionToEdit);

		// Grava o customCV
		this.uploadFile.uploadFile(customCV, UploadFile.FOLDER_SUBMISSION_CV, this.submissionToEdit.getId(), UploadFile.DOCUMENT_EXTENSION_PDF);

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Novo CV submetido."));

		this.submissionToEdit=null;
	}

	public void uploadNewMotivationLetter(FileUploadEvent event){
		UploadedFile motivationLetter=event.getFile();

		// Grava o customCV
		this.uploadFile.uploadFile(motivationLetter, UploadFile.FOLDER_SUBMISSION_MOTIVATION_LETTER, this.submissionToEdit.getId(), UploadFile.DOCUMENT_EXTENSION_PDF);

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nova carta de motivação submetida."));

		this.submissionToEdit=null;
	}

	public List<SubmissionEntity> associatedSubmissions() {
		UserEntity candidate=this.userEJB.findUserByEmail(this.userManagement.getUserMail());
		return this.submissionEJB.findSubmissionsOfCandidate(candidate);
	}

	public List<SubmissionEntity> spontaneousSubmissions() {
		UserEntity candidate=this.userEJB.findUserByEmail(this.userManagement.getUserMail());
		return this.submissionEJB.findSpontaneousSubmissionsOfCandidate(candidate);
	}

	public void updateStatus(){
		log.info("Updating status of submission");

		// if current status is HIRED and new one is not, clean hiredDate
		if(this.submission.getStatus().equals(Constants.STATUS_HIRED)
				&&!this.status.equals(Constants.STATUS_HIRED)) {
			this.submission.setHiredDate(null);
			//updates the number of hired people of position
			int oldhiredP = this.submission.getPosition().getHired_people();
			oldhiredP--;
			if (oldhiredP < 0) log.debug("Hired people counting is negative!");
			this.submission.getPosition().setHired_people(oldhiredP);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Apagada data de contratação."));
		}
		// if current status is REJECTED and new one isnt, clean rejectedReason
		if(this.submission.getStatus().equals(Constants.STATUS_REJECTED)
				&&!this.status.equals(Constants.STATUS_REJECTED)) {
			this.submission.setRejectReason(null);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Apagado motivo de rejeição."));
		}

		// if current status one of PROP/ON NEGO, and new is SUB/ACCEPTED
		if((this.submission.getStatus().equals(Constants.STATUS_PROPOSAL)
				||this.submission.getStatus().equals(Constants.STATUS_NEGOTIATION))
				&&(this.status.equals(Constants.STATUS_SUBMITED)||
						this.status.equals(Constants.STATUS_ACCEPTED))) {
			this.submission.setProposalDate(null);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Apagada data de proposta."));
		}

		this.submission.setStatus(status);
		// Se o novo status é ACCEPTED, avisa para marcar entrevista
		if(this.status.equals(Constants.STATUS_ACCEPTED)
				&&!this.submission.getStatus().equals(Constants.STATUS_ACCEPTED)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Não se esqueça de agendar entrevista."));
		} else if((this.status.equals(Constants.STATUS_PROPOSAL) 
				||this.status.equals(Constants.STATUS_NEGOTIATION))
				&&(this.submission.getStatus().equals(Constants.STATUS_SUBMITED) 
						||this.status.equals(Constants.STATUS_ACCEPTED)))
			this.submission.setProposalDate(new Date());
		else if(this.status.equals(Constants.STATUS_REJECTED)
				&&!this.submission.equals(Constants.STATUS_REJECTED)) {
			this.needsReason=true;
			return;
		} else if(this.status.equals(Constants.STATUS_HIRED)
				&&!this.submission.equals(Constants.STATUS_HIRED)) {
			PositionEntity position=this.submission.getPosition();
			
			//updates the number of hired people of position
			int oldhiredP = position.getHired_people();
			oldhiredP++;
			position.setHired_people(oldhiredP);
			
			if (oldhiredP == position.getOpenings()) {
				// close position if hired people equal to openings
				position.setStatus(Constants.STATUS_CLOSED);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Todas as vagas da posição"
								+ " foram preenchidas. A posição foi"
								+ " fechada automaticamente."));
			} else if (oldhiredP > position.getOpenings())
				log.debug("Hired people counting is greater than openings!");

			this.submission.setHiredDate(new Date());
			positionEJB.update(position);
			this.mail.notifyHired(submission);
		}			

		this.submissionEJB.update(this.submission);

		this.submission=null;

		this.clean();

		FacesContext.getCurrentInstance().addMessage(
				null, new FacesMessage("Estado actualizado."));
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

	public void searchAllSponteanous() {
		log.info("Searching for all spontaneouse submissions");
		this.slist = submissionEJB.findSpontaneousSubmissions();
	}

	public void searchByDate() {
		log.info("Searching for submissions between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findSubmissionsByDate(date1, date2);
	}	

	public void searchSpontanouseByDate() {
		log.info("Searching for spontaneous submissions between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findSpontaneousSubmissionsByDate(date1,
				date2);
	}	

	public void searchRejectedByDate() {
		log.info("Searching for rejected submissions between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findRejectedSubmissions(date1, date2);
	}	

	public void searchProposalByDate() {
		log.info("Searching for proposals presented between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findPresentedProposals(date1, date2);
	}	

	public void searchBySourceAndDate() {
		log.info("Searching for submissions by source between two dates");
		log.debug("Source "+source);
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findSubmissionsBySource(source, date1,
				date2);
	}	

	public void searchSubmissionsOfPosition() {
		log.info("Searching for submissions by position");
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			log.debug("Position "+position.getPositionCode());
			this.slist = submissionEJB.findSubmissionsOfPosition(position);
		} else log.error("No position with id "+id);
	}	

	public void searchSubmissionsOfCandidate(Long id) {
		log.info("Searching for submissions by candidate");
		UserEntity user = userEJB.find(id);
		if (user != null && user.getRoles().contains("CANDIDATE")) {
			log.debug("Candidate "+user.getFirstName());
			this.slist = submissionEJB.findSubmissionsOfCandidate(user);
		} else log.error("No candidate with id "+id);
	}	

	public void redirectExternalURl(String site) throws IOException {
		if (site != null) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().
					getExternalContext();
			externalContext.redirect(site);
		}
	}

	public boolean hasLikedin(UserEntity candidate) {
		// Facultativo
		if(candidate.getUserInfo()==null) return false;

		return candidate.getUserInfo().getLinkedin() != null;
	}

	public String getMotivatioLetterPath(SubmissionEntity submission) {
		// (.pdf)
		HttpServletRequest request = (HttpServletRequest) FacesContext.
				getCurrentInstance().getExternalContext().getRequest();
		return request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+"/"+UploadFile.FOLDER_SUBMISSION_MOTIVATION_LETTER+"/"
		+submission.getId()+UploadFile.DOCUMENT_EXTENSION_PDF;
	}

	public String getCvPath(SubmissionEntity submission) {
		// (.pdf)
		HttpServletRequest request = (HttpServletRequest) FacesContext.
				getCurrentInstance().getExternalContext().getRequest();

		// Se a submission tem um customCV
		if(submission.isCustomCV())
			return request.getScheme()+"://"+request.getServerName()+":"
			+request.getServerPort()+"/"+UploadFile.FOLDER_SUBMISSION_CV+"/"
			+submission.getId()+UploadFile.DOCUMENT_EXTENSION_PDF;

		// Caso não tenha, vai o CV por defeito do candidato
		return request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+"/"+UploadFile.FOLDER_USER_CV+"/"
		+submission.getCandidate().getId()+UploadFile.DOCUMENT_EXTENSION_PDF;
	}

	public String getDateWithoutTime(Date date) {
		return ftDate.format(date);
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

	// getters e setters

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public List<SubmissionEntity> getSlist() {
		return slist;
	}

	public void setSlist(List<SubmissionEntity> slist) {
		this.slist = slist;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SubmissionEntity getSubmission() {
		return submission;
	}

	public void setSubmission(SubmissionEntity submission) {
		this.submission = submission;
		this.status = this.submission.getStatus();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getAvailableStatus() {
		return availableStatus;
	}

	public void setAvailableStatus(Map<String, String> availableStatus) {
		this.availableStatus = availableStatus;
	}

	public List<SubmissionEntity> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<SubmissionEntity> submissions) {
		this.submissions = submissions;
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
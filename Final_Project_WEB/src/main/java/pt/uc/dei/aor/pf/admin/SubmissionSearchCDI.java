package pt.uc.dei.aor.pf.admin;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
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

	private List<SubmissionEntity> slist;

	private SubmissionEntity submissionToEdit;

	public SubmissionSearchCDI() {
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

	public void searchAll() {
		log.info("Searching for all submissions");
		this.slist = submissionEJB.findAll();
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

}
package pt.uc.dei.aor.pf.admin;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;

import java.io.Serializable;

@Named
@SessionScoped
public class AssociateCandidateToPositionCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2404419108146168392L;

	@Inject
	private UserSessionManagement userManagement;

	@Inject
	private UploadFile uploadFile;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	private UserEntity newCandidate;

	private PositionEntity position;

	public void uploadCV(FileUploadEvent event){
		UploadedFile file=event.getFile();

		this.newCandidate.setUploadedCV(true);
		this.userEJB.update(newCandidate);

		this.uploadFile.uploadFile(file, UploadFile.FOLDER_USER_CV, this.newCandidate.getMiscKey()+String.valueOf(this.newCandidate.getId()), UploadFile.DOCUMENT_EXTENSION_PDF);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CV do candidato submetido."));
	}

	public void createSubmission(FileUploadEvent event){
		UploadedFile motivationLetter=event.getFile();

		UserEntity admin=this.userEJB.findUserByEmail(this.userManagement.getUserMail());

		SubmissionEntity submission=new SubmissionEntity(newCandidate, Constants.STATUS_SUBMITED, null, null, false);
		submission.setAssociatedBy(admin);

		submission=this.submissionEJB.saveAndReturn(submission);

		this.uploadFile.uploadFile(motivationLetter, UploadFile.FOLDER_SUBMISSION_MOTIVATION_LETTER, submission.getMiscKey()+String.valueOf(submission.getId()), UploadFile.DOCUMENT_EXTENSION_PDF);

		this.position=null;
	}

	public void associateCandidate(UserEntity candidate) {
		System.out.println("Candidate: "+candidate.getEmail());
		
		this.newCandidate = candidate;
	}

	public void associatePosition(PositionEntity position){
		if(this.positionEJB.alreadyCandidateOfPosition(newCandidate, position))
			this.position = position;
		else{
			this.position=null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Já é candidato."));
		}		
	}
	
	public void cleanAll(){
		this.position=null;
		this.newCandidate=null;
	}

	public void dissociatePosition(){
		this.position=null;
	}

	public boolean cancelPosition(){
		return this.candidateHasCV()&&this.hasPosition();
	}

	public boolean hasPosition(){
		return this.position!=null;
	}
	
	public boolean hasCandidate(){
		return this.newCandidate!=null;
	}

	public boolean candidateHasCV(){
		if(this.newCandidate==null)return false;
		return this.newCandidate.isUploadedCV();
	}
	
	public String getUserMail(){
		if(this.newCandidate==null) return"";
		return this.newCandidate.getEmail();
	}

}

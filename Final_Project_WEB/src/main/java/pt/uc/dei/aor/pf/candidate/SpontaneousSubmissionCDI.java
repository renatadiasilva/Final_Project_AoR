package pt.uc.dei.aor.pf.candidate;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;

@Named
@RequestScoped
public class SpontaneousSubmissionCDI {

	@Inject
	private UploadFile uploadFile;

	@Inject
	private UserSessionManagement userManagement;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private UserEJBInterface userEJB;

	private UploadedFile motivationLetter, customCV;
	
	public void createSpontaneousSubmission(FileUploadEvent event){
		
		this.motivationLetter=event.getFile();

		UserEntity candidate=this.userEJB.findUserByEmail(this.userManagement.getUserMail());

		SubmissionEntity spontaneousSubmission=new SubmissionEntity(candidate, Constants.STATUS_SUBMITED, null, null, true);

		// Persiste e devolve a entidade já com o id
		spontaneousSubmission=this.submissionEJB.saveAndReturn(spontaneousSubmission);

		// Grava a motivationLetter
		this.uploadFile.uploadFile(this.motivationLetter, UploadFile.FOLDER_SUBMISSION_MOTIVATION_LETTER, spontaneousSubmission.getId(), UploadFile.DOCUMENT_EXTENSION_PDF);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Candidatura espontânea submetida. Durante a próxima hora pode mudar o CV e a Carta de Motivação desta candidatura na área das suas candidaturas."));
	}
		
//	public void motivationLetter(FileUploadEvent event){
//		this.motivationLetter=event.getFile();
//	}
//	
//	public void customCV(FileUploadEvent event){
//		this.customCV=event.getFile();
//	}
//
//	public void createSpontaneousSubmission(){
//
//		UserEntity candidate=this.userEJB.findUserByEmail(this.userManagement.getUserMail());
//
//		SubmissionEntity spontaneousSubmission=new SubmissionEntity(candidate, Constants.STATUS_SUBMITED, null, null, true);
//		
//		if(this.customCV!=null)
//			spontaneousSubmission.setCustomCV(true);
//
//		// Persiste e devolve a entidade já com o id
//		spontaneousSubmission=this.submissionEJB.saveAndReturn(spontaneousSubmission);
//		
//		// Grava o customCV
//		if(this.customCV!=null)
//			this.uploadFile.uploadFile(this.customCV, UploadFile.FOLDER_SUBMISSION_CV, spontaneousSubmission.getId(), UploadFile.DOCUMENT_EXTENSION_PDF);
//
//		// Grava a motivationLetter
//		this.uploadFile.uploadFile(this.motivationLetter, UploadFile.FOLDER_SUBMISSION_MOTIVATION_LETTER, spontaneousSubmission.getId(), UploadFile.DOCUMENT_EXTENSION_PDF);
//		
//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Candidatura espontânea submetida."));
//	}

}

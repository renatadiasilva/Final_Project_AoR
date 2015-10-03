package pt.uc.dei.aor.pf.candidate;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;

@Named
@RequestScoped
public class UploadCVCDI {
	
	@Inject
	private UploadFile uploadFile;
	
	@Inject
	private UserSessionManagement userManagement;
	
	@EJB
	private UserEJBInterface userEJB;
	
	private UploadedFile file;
	
	public void uploadCV(FileUploadEvent event){		
		this.file=event.getFile();
		
		boolean newCV = !this.userManagement.isCv();
		
		// Actualiza o Utilizador e devolve o seu ID: this.userManagement.uploadCV()
		this.uploadFile.uploadFile(this.file, UploadFile.FOLDER_USER_CV, this.userManagement.uploadCV(), UploadFile.DOCUMENT_EXTENSION_PDF);
		if(newCV) FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CV carregado."));
		else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CV actualizado."));
	}

}

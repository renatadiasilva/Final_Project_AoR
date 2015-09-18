package pt.uc.dei.aor.pf.admin;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.StyleEJBInterface;
import pt.uc.dei.aor.pf.entities.StyleEntity;
import pt.uc.dei.aor.pf.session.StyleSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;
import java.io.Serializable;

@Named
@SessionScoped
public class NewStyleCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2905586192455647193L;

	private static final Logger log = LoggerFactory.getLogger(NewStyleCDI.class);

	@EJB
	private StyleEJBInterface styleBean;

	@Inject
	private UploadFile uploadFile;

	@Inject
	private StyleSessionManagement currentStyle;
	
	private StyleEntity newStyle;

	private long id;

	private UploadedFile file;

	private String styleName;

	private String companyName;

	private String footerMessage;

	private String primaryColor;

	private String secondaryColor;

	private boolean userDefault;

	private boolean valid;

	public NewStyleCDI() {
		this.companyName=this.footerMessage=this.styleName=null;
	}

	public void newStyle(){

		this.valid=true;

		

		//		if(this.styleName==null||this.companyName==null||this.footerMessage==null){
		//			this.valid=false;
		//			System.out.println("Campos inválidos");
		//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Campos mal definidos no bean..."));
		//		}

		if(this.valid){

			// Se for o novo padrão desmarca o padrão actual
			if(this.userDefault){

				// Vai buscar o padrão actual
				StyleEntity defaultStyle=this.styleBean.findDefaulStyle();

				// Se for padrão do padrão do sistema salta este ponto
				if(defaultStyle.isUserDefaultStyle()){
					// Se for padrão do utilizador marca a falso
					defaultStyle.setUserDefaultStyle(false);
					this.styleBean.update(defaultStyle);
				}

			}

			// Persiste e devolve a entidade
			this.newStyle=this.styleBean.saveAndReturn(new StyleEntity(styleName, companyName, footerMessage, primaryColor, secondaryColor, userDefault));
			this.id=this.newStyle.getId();
		}

	}

	public void uploadLogo(FileUploadEvent event){
		this.file=event.getFile();
		
		// Grava o ficheiro com o ID da Entidade Persistida
		String fileName=this.file.getFileName();
		String extension=fileName.substring(fileName.lastIndexOf('.'), fileName.length());
		this.uploadFile.uploadFile(this.file, UploadFile.FOLDER_CUSTOM_LOGOS, this.id, extension);
		
		// Actualiza o estilo com a extensão do ficheiro
		this.newStyle.setLogoFormat(extension);
		this.styleBean.update(this.newStyle);

		// Se for o novo padrão reinicia a apresentação
		if(this.userDefault) this.currentStyle.init();

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Novo estilo criado ("+this.styleName+")"));
		log.info("New style created ("+this.styleName+")");

		// Mete as Strings a null
		this.companyName=this.footerMessage=this.styleName=null;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFooterMessage() {
		return footerMessage;
	}

	public void setFooterMessage(String footerMessage) {
		this.footerMessage = footerMessage;
	}

	public String getPrimaryColor() {
		return this.currentStyle.getPrimaryColor();
	}

	public void setPrimaryColor(String primaryColor) {
		this.primaryColor = "#"+primaryColor;
	}

	public String getSecondaryColor() {
		return this.currentStyle.getSecondaryColor();
	}

	public void setSecondaryColor(String secondaryColor) {
		this.secondaryColor = "#"+secondaryColor;
	}

	public boolean isUserDefault() {
		return userDefault;
	}

	public void setUserDefault(boolean userDefault) {
		this.userDefault = userDefault;
	}

}

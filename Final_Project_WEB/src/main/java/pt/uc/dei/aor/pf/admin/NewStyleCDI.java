package pt.uc.dei.aor.pf.admin;

import java.util.Properties;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.aor.pf.beans.StyleEJBInterface;
import pt.uc.dei.aor.pf.entities.StyleEntity;
import pt.uc.dei.aor.pf.session.StyleSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;

@Named
@RequestScoped
public class NewStyleCDI {

	@EJB
	private StyleEJBInterface styleBean;

	@Inject
	private UploadFile uploadFile;

	@Inject
	private StyleSessionManagement currentStyle;

	private UploadedFile file;

	private String styleName;

	private String companyName;

	private String footerMessage;

	private String primaryColor;

	private String secondaryColor;

	private boolean userDefault;

	private boolean valid;

	public void newStyle(FileUploadEvent event){
		System.out.println("newStyle begin");
		
		this.valid=true;

		this.file=event.getFile();

		if(this.styleName.isEmpty()||this.companyName.isEmpty()||this.footerMessage.isEmpty())this.valid=false;


		if(this.valid){
			// Se for o novo padrão desmarca o padrão actual
//			if(this.userDefault){
//
//				// Vai buscar o padrão actual
//				StyleEntity defaultStyle=this.styleBean.findDefaulStyle();
//
//				// Se for padrão do padrão do sistema salta este ponto
//				if(defaultStyle.isUserDefaultStyle()){
//					// Se for padrão do utilizador marca a falso
//					defaultStyle.setUserDefaultStyle(false);
//					this.styleBean.update(defaultStyle);
//				}
//
//			}

			// Persiste e devolve o ID
//			long id=this.styleBean.saveAndReturn(new StyleEntity(styleName, companyName, footerMessage, primaryColor, secondaryColor, userDefault)).getId();


//			Properties props = System.getProperties();
//			try {
//				System.out.println(this.file.getFileName()+this.file.getSize());
//				this.file.write(props.getProperty("user.dir")+"\\"+UploadFile.FOLDER_CUSTOM_LOGOS+"\\"+"ninja"+"."+UploadFile.IMAGE_EXTENSION);
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("Erro a escrever o ficheiro");
//				//			log.error("Error writing "+this.file.getFileName()+" to "+folder+" with new name "+id+"."+extension);
//			}

			// Grava o ficheiro com o ID da Entidade Persistida
			this.uploadFile.uploadFile(this.file, UploadFile.FOLDER_CUSTOM_LOGOS, (long) 69, UploadFile.IMAGE_EXTENSION);

			// Se for o novo padrão reinicia a apresentação
//			if(this.userDefault) this.currentStyle.init();
		}
	}
	
	public void dummy(){
		// meh
		System.out.println("Dummy click");
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

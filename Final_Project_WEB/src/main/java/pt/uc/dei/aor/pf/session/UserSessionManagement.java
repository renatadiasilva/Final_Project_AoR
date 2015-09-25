package pt.uc.dei.aor.pf.session;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementInterface;
import pt.uc.dei.aor.pf.upload.UploadFile;
import pt.uc.dei.aor.pf.webManagement.UserManagementInterface;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

@Named
@SessionScoped
public class UserSessionManagement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7533289682838866026L;

	private static final Logger log = LoggerFactory.getLogger(UserSessionManagement.class);

	@EJB
	UserManagementInterface userManagement;

	@EJB
	UserEJBInterface userEJB;

	@EJB
	SecureMailManagementInterface mailEJB;

	//	@Inject
	//	URLQueriesCDI urlQueries;

	private FacesContext context;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private HttpSession session;

	private String password, newPassword;
	
	// clone of current user
	private UserEntity currentUserClone;
	
	public UserSessionManagement() {
	}

	public void checkForUser(){

		// Listener para a página Home.xhtml
		log.info("Checking for query in URL");

		log.info("Checking for logged user");
		log.debug("User: "+ this.userManagement.getUserEmail());

		// Se já existe um user logado reencaminha para o respectivo Landing.xhtml
		if(this.userManagement.isUserLogged()){
			this.context = FacesContext.getCurrentInstance();
			this.response = (HttpServletResponse) context.getExternalContext().getResponse();

			try {
				// Encaminha para...
				this.response.sendRedirect(this.request.getContextPath()+"/role/"+this.userManagement.getUserDefaultRole().toLowerCase()+"/landing/Landing.xhtml");
			} catch (IOException e) {
				log.error("Redirect failure");
				this.context.addMessage(null, new FacesMessage("Reencaminhamento falhou."));
			}
		}

	}

	public void checkTemporaryPassword(){
		// ActionListener para ass páginas */Landing.xhtml

		log.info("Checking if the password is temporary");

		if(this.userManagement.isTemporaryPassword()){
			RequestContext requestContext = RequestContext.getCurrentInstance();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor mude a sua password temporária.", ""));
			requestContext.execute("PF('changePassword').show();");
		}
	}

	public void login(String email, String password){

		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();

		// Se por alguma razão do diabo há uma tentativa de login
		// com um User logado na sessão é feito um logout
		if(this.userManagement.isUserLogged()) this.logout();

		try{
			// Login no servidor - vai buscar os roles lá dentro
			// Se falha salta os passos seguintes - excepção
			this.request.login(email, password);

			// Verifica se a conta está autenticada
			if(this.userManagement.checkAuthentication(email)){

				// Inicia na aplicação
				this.userManagement.login(email, password);
				
				//keep info of current user in user clone
				this.currentUserClone = this.userManagement.getUserData();

				log.info("Login sucessfull");
				this.context.addMessage(null, new FacesMessage("Login bem sucedido: "+email));

				// Reencaminha consoante o defaultRole (exemplo do output: "/role/admin/Landing.xhtml")
				// return "/role/"+this.currentUser.getDefaultRole().toLowerCase()+"/Landing?facesRedirect=true";

				// Encaminha para...
				this.response = (HttpServletResponse) context.getExternalContext().getResponse();

				try {
					// Reencaminha consoante o defaultRole (exemplo do output: "/role/admin/Landing.xhtml")
					this.response.sendRedirect(request.getContextPath()+"/role/"+this.userManagement.getUserDefaultRole().toLowerCase()+"/landing/Landing.xhtml");
				} catch (IOException e) {
					log.error("Redirect failure");
					this.context.addMessage(null, new FacesMessage("Reencaminhamento falhou."));
				}

			}else{
				// A conta não está autenticada, o utilizador é notificado e são anulados os passos do login efectuados até ao momento
				this.request.logout();
				this.context.addMessage(null, new FacesMessage("Esta conta ainda não se encontra autenticada. Por favor, consulte a sua caixa de correio para terminar o processo de registo."));
			}

		} catch (ServletException e){
			// Falhou a autenticação no servidor
			log.error("Login failure");
			this.context.addMessage(null, new FacesMessage("Login falhou."));
		}

	}

	public void logout(){

		log.info("Doing logout");
		log.debug("User: "+ this.userManagement.getUserEmail());

		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
		this.response = (HttpServletResponse) context.getExternalContext().getResponse();

		try{
			// Logout no servidor
			this.request.logout();

			// Logout na aplicação
			this.userManagement.logout();

			// Invalida a sessão
			this.session = request.getSession();
			this.session.invalidate();

			// Encaminha para...
			this.response.sendRedirect(request.getContextPath()+"/Home.xhtml");

		} catch (ServletException e) {
			log.error("Logout failure");
			this.context.addMessage(null, new FacesMessage("Logout falhou."));
		} catch (IOException e) {
			log.error("Redirect failure");
			this.context.addMessage(null, new FacesMessage("Reencaminhamento falhou."));
		}
	}

	public void defaultRoleAdmin() {
		this.userManagement.defaultRole(Constants.ROLE_ADMIN);
	}

	public void defaultRoleManager() {
		this.userManagement.defaultRole(Constants.ROLE_MANAGER);
	}
	
	public void defaultRoleInterviewer() {
		this.userManagement.defaultRole(Constants.ROLE_INTERVIEWER);
	}
	
	public void defaultRoleCandidate() {
		this.userManagement.defaultRole(Constants.ROLE_CANDIDATE);
	}
	
	public boolean checkDefaultAdmin(String role) {
		return this.userManagement.checkDefault(Constants.ROLE_ADMIN);
	}

	public boolean checkDefaultManager(String role) {
		return this.userManagement.checkDefault(Constants.ROLE_MANAGER);
	}

	public boolean checkDefaultInterviewer(String role) {
		return this.userManagement.checkDefault(Constants.ROLE_INTERVIEWER);
	}

	public boolean checkDefaultCandidate() {
		return this.userManagement.checkDefault(Constants.ROLE_CANDIDATE);
	}
	
	public void changePassword (){
		log.info("Changing password");
		log.debug("User: "+ this.userManagement.getUserEmail());

		this.context=FacesContext.getCurrentInstance();

		// Manda para a camada de negócio, espera o boleano e reporta para a UI
		if(this.userManagement.changePassword(password, newPassword)){
			this.context.addMessage(null, new FacesMessage("Password alterada com sucesso."));
			log.info("Successfully changed password");
		}else {
			log.error("Wrong password");
			this.context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Password errada", ""));
		}
	}

	// Novo utilizador com ROLE_CANDIDATE. Se vem do signup: boolean createdByAdmin=false.
	// Se é criado por admin: boolean createdByAdmin=true
	public void newUser(String email, String password, String firstName, String lastName,
			Date birthday, String address, String city,
			String homePhone, String mobilePhone, String country, String course, String school, 
			String linkedin, boolean createdByAdmin,
			boolean admin, boolean manager, boolean interviewer){

		log.info("Creating new user (candidate)");

		// Manda para a camada de negócio, espera o boleano e reporta para a UI
		if(this.userManagement.newUser(email, password, firstName, lastName, birthday, address, city, homePhone, mobilePhone,
				country, course, school, linkedin, createdByAdmin, admin, manager, interviewer)){

			this.context=FacesContext.getCurrentInstance();

			// Se foi criado à mão, fecha o dialog (Home.xhtml)
			if(!createdByAdmin){
//				RequestContext requestContext = RequestContext.getCurrentInstance();
//				requestContext.execute("PF('signup').hide();");
				this.context.addMessage(null, new FacesMessage("Novo Utilizador criado com sucesso: "+email));
				this.context.addMessage(null, new FacesMessage("Consulte a sua caixa de correio e siga as instruções apresentadas."));
			}else{
				this.context.addMessage(null, new FacesMessage("Novo Utilizador criado com sucesso: "+email));
//				this.context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Anote a password temporária: "+password, ""));
			}

		}else {
			log.info("Registration failure: the email already exists");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registo falhou, email já se encontra em uso: "+email));
		}
	}

	// Novo utilizador criado por um admin sem o ROLE_CANDIDATE
	public void newUserNC (String email, String password, String firstName,
			String lastName, boolean admin, boolean manager, boolean interviewer) {

		log.info("Creating new user (internal)");

		this.context=FacesContext.getCurrentInstance();

		// Manda para a camada de negócio, espera o boleano e reporta para a UI
		if(this.userManagement.newUserNC(email, password, firstName, lastName, admin, manager, interviewer)){

			this.context.addMessage(null, new FacesMessage("Novo Utilizador criado com sucesso: "+email));
//			this.context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Anote a password temporária: "+password, ""));

		}else this.context.addMessage(null, new FacesMessage("Registo falhou, email já se encontra em uso: "+email));
	}
	
	public void updateUserData(String firstName, String lastName, 
			Date birthday, String address, 
			String city, String homePhone, String mobilePhone, String country, 
			String course, String school, String linkedin) {

		log.info("Updating user info");
		log.debug("New user: "+ this.userManagement.getUserEmail());

		// Vai para a camada de negócio
		this.userManagement.updateUserData(firstName, lastName, 
				birthday, address, city, homePhone, mobilePhone,
				country, course, school, linkedin);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Dados atualizados."));
		
		//update data in user clone
		this.currentUserClone = this.userManagement.getUserData();

	}

	public void recoverPassword(String email, String temporaryPassword){
		this.userManagement.recoverPassword(email, temporaryPassword);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Foi enviado um email para a sua conta com os novos dados de autenticação."));
	}

	@SuppressWarnings("unused")
	private void redirect(String path){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		try {
			response.sendRedirect(request.getContextPath()+path);
		} catch (IOException e) {
			// Erro a redireccionar
			e.printStackTrace();
		}
	}
	
	public long uploadCV(){
		return this.userManagement.uploadCV();
	}

	public boolean isAdmin() {
		return this.userManagement.isAdmin();
	}

	public boolean isManager() {
		return this.userManagement.isManager();
	}

	public boolean isInterviewer() {
		return this.userManagement.isInterviewer();
	}

	public boolean isCandidate() {
		return this.userManagement.isCandidate();
	}

	public String getRandomPass() {
		return RandomStringUtils.randomAlphanumeric(8);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getUserFullName(){
		return this.userManagement.getUserFullName();
	}

	public List<String>getStyle(){
		return this.userManagement.getStyle();
	}

	public boolean isCv() {
		return this.userManagement.isCv();
	}

	public String getCvPath(){
		// (.pdf)
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/userCV/"+this.userManagement.getId()+UploadFile.DOCUMENT_EXTENSION_PDF;
	}
	
	public String getUserMail(){
		return this.userManagement.getUserEmail();
	}

	public UserEntity getCurrentUserClone() {
		return currentUserClone;
	}

	public void setCurrentUserClone(UserEntity currentUserClone) {
		this.currentUserClone = currentUserClone;
	}

}

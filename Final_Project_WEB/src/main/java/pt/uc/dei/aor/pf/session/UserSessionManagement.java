package pt.uc.dei.aor.pf.session;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.primefaces.context.RequestContext;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
import pt.uc.dei.aor.pf.credentials.CredentialsCatchFilter;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class UserSessionManagement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2527354852846254610L;

	@Inject
	UserEJBInterface userBean;

	@Inject
	UserInfoEJBInterface userInfoBean;

	private UserEntity currentUser;

	private boolean admin, manager, interviewer, candidate;

	private FacesContext context;

	private HttpServletRequest request;

	private HttpServletResponse response;
	
	private HttpSession session;

	private String randomPass, password, newPassword;

	private boolean newPasswordCheck;


	public UserSessionManagement() {		
		this.currentUser=new UserEntity();
		this.admin=this.manager=this.interviewer=this.candidate=false;

		this.newPasswordCheck=true;
	}

	public void checkForUser(){
		// ActionListener para as páginas Index.xhtml e Signup.xhtml
		// Se já existe um user logado reencaminha para o respectivo Landing.xhtml
		if(this.currentUser.getEmail()!=null){
			this.context = FacesContext.getCurrentInstance();
			this.response = (HttpServletResponse) context.getExternalContext().getResponse();

			try {
				// Encaminha para...
				this.response.sendRedirect(request.getContextPath()+"/role/"+this.currentUser.getDefaultRole().toLowerCase()+"/Landing.xhtml");
			} catch (IOException e) {
				this.context.addMessage(null, new FacesMessage("Redirect falhou."));
			}
		}
	}

	public void checkTemporaryPassword(){
		if(this.currentUser.isTemporaryPassword()&&this.newPasswordCheck){
			RequestContext requestContext = RequestContext.getCurrentInstance();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor mude a sua password temporária.", ""));
			requestContext.execute("PF('changePassword').show();");
			this.newPasswordCheck=false;
		}
	}

	public void login(String email, String password){

		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();

		if(this.currentUser.getEmail()!=null) this.logout();

		try{
			// Login no servidor - vai buscar os roles lá dentro
			// Se falha salta os passos seguintes - excepção
			this.request.login(email, password);

			// Se o servidor consegue logar o utilizador não cria a excepção e chega aqui: logo a password está correcta
			this.currentUser = userBean.findUserByEmail(email);

			// Roles para mostrar na web
			this.setAvailableRoles();

			this.context.addMessage(null, new FacesMessage("Login sucessfull: "+email));

			// Reencaminha consoante o defaultRole (exemplo do output: "/role/admin/Landing.xhtml")
			// return "/role/"+this.currentUser.getDefaultRole().toLowerCase()+"/Landing?facesRedirect=true";

			// Encaminha para...
			this.response = (HttpServletResponse) context.getExternalContext().getResponse();

			try {
				// Reencaminha consoante o defaultRole (exemplo do output: "/role/admin/Landing.xhtml")
				this.response.sendRedirect(request.getContextPath()+"/role/"+this.currentUser.getDefaultRole().toLowerCase()+"/Landing.xhtml");
			} catch (IOException e) {
				this.context.addMessage(null, new FacesMessage("Reencaminhamento falhou."));
			}

		} catch (ServletException e){
			this.context.addMessage(null, new FacesMessage("Login falhou."));
		}
	}

	public void logout(){
		
		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
		this.response = (HttpServletResponse) context.getExternalContext().getResponse();

		try{
			this.request.logout();
			this.admin=this.manager=this.interviewer=this.candidate=false;
			this.currentUser=new UserEntity();

			// Invalida a sessão
			this.session = request.getSession();
			this.session.invalidate();

			// Encaminha para...
			this.response.sendRedirect(request.getContextPath()+"/Index.xhtml");


		} catch (ServletException e) {
			this.context.addMessage(null, new FacesMessage("Logout falhou."));
		} catch (IOException e) {
			this.context.addMessage(null, new FacesMessage("Redirect falhou."));
		}
	}

	// Para exibição do menu de navegação (rendered)
	private void setAvailableRoles() {
		for (String s: this.currentUser.getRoles()){
			if(s.equals(UserEntity.ROLE_ADMIN)) this.admin=true;
			if(s.equals(UserEntity.ROLE_MANAGER)) this.manager=true;
			if(s.equals(UserEntity.ROLE_INTERVIEWER)) this.interviewer=true;
			if(s.equals(UserEntity.ROLE_CANDIDATE)) this.candidate=true;
		}
	}

	public void defaultRole(String role){

		if(role.equals(UserEntity.ROLE_ADMIN)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_MANAGER)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_INTERVIEWER)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_CANDIDATE)) this.currentUser.setDefaultRole(role);

		this.userBean.update(this.currentUser);	
	}

	public boolean checkDefault(String role){
		if(this.currentUser.getDefaultRole().equals(role)) return true;
		return false;
	}

	public void changePassword (){
		if(this.userBean.checkPassword(this.currentUser, password)){
			this.currentUser.setPassword(newPassword);
			this.userBean.updatePassword(currentUser);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password alterada com sucesso."));
		}else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Password errada", ""));
	}

	// Novo utilizador com ROLE_CANDIDATE. Se vem do signup: boolean createdByAdmin=false. Se é criado por admin: boolean createdByAdmin=true
	public void newUser(String email, String password, String firstName, String lastName, Date birthday,  String address, String city,
			String homePhone, String mobilePhone, String country, String course, String school, String linkedin, boolean createdByAdmin,
			boolean admin, boolean manager, boolean interviewer){

		// Verifica primeiro se o email já está a uso
		if(this.userBean.findUserByEmail(email)==null){
			List<String> roles = new ArrayList<String>();
			roles.add(UserEntity.ROLE_CANDIDATE);

			if(admin&&createdByAdmin)roles.add(UserEntity.ROLE_ADMIN);
			if(manager&&createdByAdmin)roles.add(UserEntity.ROLE_MANAGER);
			if(interviewer&&createdByAdmin)roles.add(UserEntity.ROLE_INTERVIEWER);

			// Atributos do UserEntity
			UserEntity newUser=new UserEntity(email, password, firstName, lastName, roles);
			newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);	

			// Atributos do UserInfoEntity do respectivo UserEntity
			UserInfoEntity newUserInfo= new UserInfoEntity(birthday, address, city, homePhone, mobilePhone, country, course, school, null, newUser);

			newUser.setUserInfo(newUserInfo);

			// Se for criado por um admin, esse admin é o currentUser e a temporaryPassword=true
			if(createdByAdmin){
				newUser.setCreatedBy(this.currentUser);
				newUser.setTemporaryPassword(true);
			}
			// Se foi criado à mão, fecha o dialog (Index.xhtml)
			else {
				RequestContext requestContext = RequestContext.getCurrentInstance();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor mude a sua password temporária.", ""));
				requestContext.execute("PF('signup').hide();");
			}

			// Grava o UserEntity
			this.userBean.save(newUser);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Novo Utilizador cirado com sucesso: "+email));
			if(createdByAdmin)FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Anote a password temporária: "+password, ""));

		}else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registo falhou, email já se encontra em uso: "+email));
	}

	// Novo utilizador criado por um admin sem o ROLE_CANDIDATE
	public boolean newUserNC (String email, String password, String firstName, String lastName, boolean admin, boolean manager, boolean interviewer) {

		// Verifica primeiro se o email já está a uso
		if(this.userBean.findUserByEmail(email)==null){

			UserEntity newUser=new UserEntity();
			List<String> roles = new ArrayList<String>();

			if(admin)roles.add(UserEntity.ROLE_ADMIN);
			if(manager)roles.add(UserEntity.ROLE_MANAGER);
			if(interviewer)roles.add(UserEntity.ROLE_INTERVIEWER);

			newUser=new UserEntity(email, password, firstName, lastName, roles);

			// Role por default
			if(interviewer)newUser.setDefaultRole(UserEntity.ROLE_INTERVIEWER);
			if(manager)newUser.setDefaultRole(UserEntity.ROLE_MANAGER);
			if(admin)newUser.setDefaultRole(UserEntity.ROLE_ADMIN);

			// Foi criado por um admin, esse admin é o currentUser, e a temporaryPassword=true
			newUser.setCreatedBy(this.currentUser);
			newUser.setTemporaryPassword(true);

			this.userBean.save(newUser);

			return true;

		}else return false;
	}

	public void updateUserInfo(String firstName, String lastName, String address, String city, String homePhone, String mobilePhone, String country, String course, String school, String linkedin) {
		this.currentUser.setFirstName(firstName);
		this.currentUser.setLastName(lastName);

		if(this.getCurrentUser().getUserInfo()==null){
			this.currentUser.setUserInfo(new UserInfoEntity());
			this.currentUser.getUserInfo().setOwner(this.currentUser);
		}

		this.currentUser.getUserInfo().setAddress(address);
		this.currentUser.getUserInfo().setCity(city);
		this.currentUser.getUserInfo().setHomePhone(homePhone);
		this.currentUser.getUserInfo().setMobilePhone(mobilePhone);
		this.currentUser.getUserInfo().setCountry(country);
		this.currentUser.getUserInfo().setCourse(course);
		this.currentUser.getUserInfo().setSchool(school);
		this.currentUser.getUserInfo().setLinkedin(linkedin);

		this.userBean.update(this.currentUser);
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

	public UserEntity getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserEntity currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public boolean isInterviewer() {
		return interviewer;
	}

	public void setInterviewer(boolean interviewer) {
		this.interviewer = interviewer;
	}

	public boolean isCandidate() {
		return candidate;
	}

	public void setCandidate(boolean candidate) {
		this.candidate = candidate;
	}

	public String getRandomPass() {
		this.randomPass=RandomStringUtils.randomAlphanumeric(8);
		return randomPass;
	}

	public void setRandomPass(String randomPass) {
		this.randomPass = randomPass;
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

}

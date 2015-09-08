package pt.uc.dei.aor.pf.DGeRS.session;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.uc.dei.aor.pf.beans.TestUserInfoInterface;
import pt.uc.dei.aor.pf.beans.TestUserInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
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


	public UserSessionManagement() {		
		this.currentUser=new UserEntity();
		this.admin=this.manager=this.interviewer=this.candidate=false;
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

	public String login(String email, String password){

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
			return "/role/"+this.currentUser.getDefaultRole().toLowerCase()+"/Landing?facesRedirect=true";

		} catch (ServletException e){
			this.context.addMessage(null, new FacesMessage("Login falhou."));
		}

		return "#";
	}

	public void logout(){

		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
		this.response = (HttpServletResponse) context.getExternalContext().getResponse();

		try{
			this.request.logout();
			this.admin=this.manager=this.interviewer=this.candidate=false;
			this.currentUser=new UserEntity();

			// Encaminha para...
			this.response.sendRedirect(request.getContextPath()+"/Index.xhtml");

		} catch (ServletException e) {
			this.context.addMessage(null, new FacesMessage("Logout falhou."));
		} catch (IOException e) {
			this.context.addMessage(null, new FacesMessage("Redirect falhou."));
		}
	}

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

	public void newUser(String email, String password, String firstName, String lastName, Date birthday,  String adress, String city,
			String homePhone, String mobilePhone, String country, String course, String school, String linkedin){

		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
		this.response = (HttpServletResponse) context.getExternalContext().getResponse();

		// Verifica primeiro se o email já está a uso
		if(this.userBean.findUserByEmail(email)==null){
			List<String> roles = new ArrayList<String>();
			roles.add(UserEntity.ROLE_CANDIDATE);

			// Atributos do UserEntity
			UserEntity newUser=new UserEntity(email, password, firstName, lastName, roles);
			newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);	
			

			// Atributos do UserInfoEntity do respectivo UserEntity
			UserInfoEntity newUserInfo= new UserInfoEntity(birthday, adress, city, homePhone, mobilePhone, country, course, school, null);

			newUser.setUserInfo(newUserInfo);
			
			this.userBean.save(newUser);						

			this.context.addMessage(null, new FacesMessage("Novo Utilizador cirado com sucesso: "+email));

		}else this.context.addMessage(null, new FacesMessage("Registo falhou, email já se encontra em uso: "+email));
	}

	public void updateUser(String firstName, String lastName){
		this.currentUser.setFirstName(firstName);
		this.currentUser.setLastName(lastName);
		this.userBean.update(this.currentUser);
	}

	public void updateUserInfo(String firstName, String lastName, String address, String city, String homePhone, String mobilePhone, String country, String course, String school, String linkedin) {
		
		if(this.getCurrentUser().getUserInfo()==null) this.currentUser.setUserInfo(new UserInfoEntity());
		
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

}

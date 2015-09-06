package pt.uc.dei.aor.pf.webManagement;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.uc.dei.aor.pf.beans.TestUserInterface;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//@Stateless
public class UserManagementImp implements UserManagementInterface {

	@EJB
	TestUserInterface testUserBean;
	
	protected UserEntity currentUser;

	private boolean admin, manager, interviewer, candidate;

	@Override
	public void init() {		
		this.currentUser=new UserEntity();
		this.admin=this.manager=this.interviewer=this.candidate=false;
	}
	
	@Override
	public boolean isLogged(){
		if(this.currentUser.getEmail()!=null) return true;
		return false;
	}

	@Override
	public String login(String email, String password){

//		this.context = FacesContext.getCurrentInstance();
//		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
//
//		if(this.currentUser.getEmail()!=null) this.logout();
//
//		try{
//			// Login no servidor - vai buscar os roles lá dentro
//			// Se falha salta os passos seguintes - excepção
//			this.request.login(email, password);
//
//			// Se o servidor consegue logar o utilizador não cria a excepção e chega aqui: logo a password está correcta
//			//			this.currentUser = testUserBean.findUserByEmail(email);
//
//			// Roles para mostrar na web
//			this.setAvailableRoles();
//
//			this.context.addMessage(null, new FacesMessage("Login sucessfull: "+email));
//
//			// Reencaminha consoante o defaultRole (exemplo do output: "/role/admin/Landing.xhtml")
//			return "/role/"+this.currentUser.getDefaultRole().toLowerCase()+"/Landing?faces-redirect=true";
//
//		} catch (ServletException e){
//			this.context.addMessage(null, new FacesMessage("Login falhou."));
//		}
//
		return "#";
	}

	@Override
	public void logout(){

//		this.context = FacesContext.getCurrentInstance();
//		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
//		this.response = (HttpServletResponse) context.getExternalContext().getResponse();
//
//		try{
//			this.request.logout();
//			this.admin=this.manager=this.interviewer=this.candidate=false;
//			this.currentUser=new UserEntity();
//
//			// Encaminha para...
//			this.response.sendRedirect(request.getContextPath()+"/Index.xhtml");
//
//		} catch (ServletException e) {
//			this.context.addMessage(null, new FacesMessage("Logout falhou."));
//		} catch (IOException e) {
//			this.context.addMessage(null, new FacesMessage("Redirect falhou."));
//		}
	}

	private void setAvailableRoles() {
		for (String s: this.currentUser.getRoles()){
			if(s.equals(UserEntity.ROLE_ADMIN)) this.admin=true;
			if(s.equals(UserEntity.ROLE_MANAGER)) this.manager=true;
			if(s.equals(UserEntity.ROLE_INTERVIEWER)) this.interviewer=true;
			if(s.equals(UserEntity.ROLE_CANDIDATE)) this.candidate=true;
		}
	}

	@Override
	public void defaultRole(String role){

		if(role.equals(UserEntity.ROLE_ADMIN)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_MANAGER)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_INTERVIEWER)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_CANDIDATE)) this.currentUser.setDefaultRole(role);

		this.testUserBean.update(this.currentUser);	
	}

	@Override
	public boolean checkDefault(String role){
		if(this.currentUser.getDefaultRole().equals(role)) return true;
		return false;
	}

	@Override
	public void newUser(String email, String password, String firstName, String lastName, String adress, String city,
			String homePhone, String mobilePhone, String country, String course, String school, String linkedin){
//
//		this.context = FacesContext.getCurrentInstance();
//		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
//		this.response = (HttpServletResponse) context.getExternalContext().getResponse();
//
//		// Verifica primeiro se o email já está a uso
//		if(this.testUserBean.findUserByEmail(email)==null){
//			List<String> roles = new ArrayList<String>();
//			roles.add(UserEntity.ROLE_CANDIDATE);
//
//			// Atributos do UserEntity
//			UserEntity newUser=new UserEntity(email, password, firstName, lastName, roles);
//			newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);
//
//			// Atributos do UserInfoEntity do respectivo UserEntity
//			UserInfoEntity newUserInfo= new UserInfoEntity(adress, city, homePhone, mobilePhone, country, course, school, null);
//			newUser.setUserInfo(newUserInfo);
//
//			this.testUserBean.save(newUser);
//
//			try {
//				this.response.sendRedirect(request.getContextPath()+"/Index.xhtml");
//			} catch (IOException e) {
//				this.context.addMessage(null, new FacesMessage("Novo Utilizador cirado com sucesso: "+email+" Redirect Falhou."));
//			}
//
//			this.context.addMessage(null, new FacesMessage("Novo Utilizador cirado com sucesso: "+email));
//		}
//
//		this.context.addMessage(null, new FacesMessage("Registo falhou, email já se encontra em uso: "+email));
	}

	@Override
	public boolean isAdmin() {
		return admin;
	}

	@Override
	public boolean isManager() {
		return manager;
	}

	@Override
	public boolean isInterviewer() {
		return interviewer;
	}

	@Override
	public boolean isCandidate() {
		return candidate;
	}

	@Override
	public String getUserFullName() {
		return currentUser.getFirstName()+" "+currentUser.getLastName();
	}
	
	@Override
	public String getDefaultRole() {
		return this.currentUser.getDefaultRole();
	}
	
}

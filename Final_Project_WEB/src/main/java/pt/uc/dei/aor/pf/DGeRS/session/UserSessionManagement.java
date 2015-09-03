package pt.uc.dei.aor.pf.DGeRS.session;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pt.uc.dei.aor.pf.beans.TestUserInterface;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.io.Serializable;

@Named
@SessionScoped
public class UserSessionManagement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2527354852846254610L;
	
	@Inject
	TestUserInterface testUserBean;
	
	private UserEntity currentUser;
	
	private boolean admin, manager, interviewer, candidate;
	
	public UserSessionManagement() {
		this.currentUser=new UserEntity();
		this.admin=this.manager=this.interviewer=this.candidate=false;
	}
	
	public String login(String email, String password){

		this.logout();
		
		// Server
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try{
			request.login(email, password);
			
			// Se o servidor consegue logar o utilizador não cria a excepção e chega aqui: logo a password está correcta
			this.currentUser = testUserBean.findUserByEmail(email);
			
			// Roles para mostrar na web
			this.setDefaultRoles();
			
			System.out.println("Login sucessfull: "+email);
		} catch (ServletException e){
			e.printStackTrace();
			System.out.println("Login failed: "+email);
			context.addMessage(null, new FacesMessage("Login failed."));
		}
		
		return "/role/"+this.currentUser.getDefaultRole().toLowerCase()+"/Landing?faces-redirect=true";
	}

	public void logout(){
		System.out.println("Logout");

		// Server
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		try{
			request.logout();
			
			this.admin=this.manager=this.interviewer=this.candidate=false;
			
			this.currentUser=new UserEntity();
			
			System.out.println("Logout sucessfull.");
		} catch (ServletException e) {
			System.out.println("Logout failed.");
			context.addMessage(null, new FacesMessage("Logout failed."));
		}
	}
	
	private void setDefaultRoles() {
		for (String s: this.currentUser.getDefaultRoles()){
			if(s.equals(UserEntity.ROLE_ADMIN)) this.admin=true;
			if(s.equals(UserEntity.ROLE_MANAGER)) this.manager=true;
			if(s.equals(UserEntity.ROLE_INTERVIEWER)) this.interviewer=true;
			if(s.equals(UserEntity.ROLE_CANDIDATE)) this.candidate=true;
		}
	}
	
	public void defaultRole(String role){
		System.out.println("Role currente: "+currentUser.getDefaultRole()+"Novo role: "+role);
		
		System.out.println("Antes: "+currentUser.getDefaultRole());
		
		if(role.equals(UserEntity.ROLE_ADMIN)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_MANAGER)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_INTERVIEWER)) this.currentUser.setDefaultRole(role);
		if(role.equals(UserEntity.ROLE_CANDIDATE)) this.currentUser.setDefaultRole(role);
		
		System.out.println("Depois: "+currentUser.getDefaultRole());
		
		this.testUserBean.update(this.currentUser);	
	}
	
	public boolean checkDefault(String role){
		if(this.currentUser.getDefaultRole().equals(role)) return true;
		return false;
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

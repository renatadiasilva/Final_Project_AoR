package pt.uc.dei.aor.pf.DGeRS;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pt.uc.dei.aor.pf.DGeRS.credentials.CredentialsCatchFilter;
import pt.uc.dei.aor.pf.DGeRS.credentials.FlushCredentialsListener;
import pt.uc.dei.aor.pf.beans.TestUserInterface;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.io.Serializable;

@Named
@SessionScoped
public class LoginTestsCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5595743605410577249L;

	@Inject
	TestUserInterface testUserBean;

	private UserEntity currentUser;

	private boolean admin, manager, interviewer, candidate;

	public LoginTestsCDI() {
		currentUser=new UserEntity();

		admin=manager=interviewer=candidate=false;
	}

	public void changeRole(String role) {
		// Vai para a camada de negócio, bem escondido!

		for (String s: this.currentUser.getRoles()) if(s.equals(role)) this.currentUser.setRole(role);

		this.testUserBean.update(currentUser);

		this.login(currentUser.getEmail());

	}

	public void createUserAdmin(){
		List<String> roles = new ArrayList<String>();
		roles.add(UserEntity.ROLE_ADMIN);
		roles.add(UserEntity.ROLE_MANAGER);
		roles.add(UserEntity.ROLE_INTERVIEWER);

		UserEntity newUser=new UserEntity("admin@mail.com", "12345", "Admin", "Admin", roles);
		newUser.setRole(UserEntity.ROLE_ADMIN);

		this.testUserBean.save(newUser);
	}

	public void createUserManager(){
		List<String> roles = new ArrayList<String>();
		roles.add(UserEntity.ROLE_MANAGER);
		roles.add(UserEntity.ROLE_CANDIDATE);

		UserEntity newUser=new UserEntity("manager@mail.com", "12345", "Manager", "Manager", roles);
		newUser.setRole(UserEntity.ROLE_MANAGER);

		this.testUserBean.save(newUser);
	}

	public void createUserInterviewer(){
		List<String> roles = new ArrayList<String>();
		roles.add(UserEntity.ROLE_INTERVIEWER);

		UserEntity newUser=new UserEntity("interviewer@mail.com", "12345", "Interviewer", "Interviewer", roles);
		newUser.setRole(UserEntity.ROLE_INTERVIEWER);

		this.testUserBean.save(newUser);
	}

	public void createUserCandidate(){
		List<String> roles = new ArrayList<String>();
		roles.add(UserEntity.ROLE_CANDIDATE);

		UserEntity newUser=new UserEntity("candidate@mail.com", "12345", "Candidate", "Candidate", roles);
		newUser.setRole(UserEntity.ROLE_CANDIDATE);

		this.testUserBean.save(newUser);
	}

	public void login(String mail){
		this.logout();

		String email=mail;

		// A retirar
		if(!mail.endsWith("@mail.com")){
			email=mail+"@mail.com";
		}

		System.out.println("Login: "+email);

//		this.currentUser = testUserBean.findUserByEmail(email);

		// Server
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try{
			request.login(email, "12345");
			System.out.println("Login sucessfull: "+email);
			this.setRoles(this.currentUser);
		} catch (ServletException e){
			e.printStackTrace();
			System.out.println("Login failed: "+email);
			context.addMessage(null, new FacesMessage("Login failed."));
		}
	}

	public void logout(){
		System.out.println("Logout");

		// Server
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();


		try{
			request.logout();
			
//			this.flushCredentials();
			
			this.admin=this.manager=this.interviewer=this.candidate=false;

			this.currentUser=new UserEntity();
			
			System.out.println("Logout sucessfull.");
		} catch (ServletException e) {
			System.out.println("Logout failed.");
			context.addMessage(null, new FacesMessage("Logout failed."));
		}
	}

	private void flushCredentials() {
		
		CredentialsCatchFilter credentialsCatch=new CredentialsCatchFilter();
		FlushCredentialsListener flushCredentials=new FlushCredentialsListener();
		
//		credentialsCatch.doFilter(request, response, next);
		
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

	private void setRoles(UserEntity currentUser) {

		for (String s: currentUser.getRoles()){
			if(s.equals(UserEntity.ROLE_ADMIN)) this.admin=true;
			if(s.equals(UserEntity.ROLE_MANAGER)) this.manager=true;
			if(s.equals(UserEntity.ROLE_INTERVIEWER)) this.interviewer=true;
			if(s.equals(UserEntity.ROLE_CANDIDATE)) this.candidate=true;
		}

	}

}

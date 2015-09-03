package pt.uc.dei.aor.pf.DGeRS;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.DGeRS.session.UserSessionManagement;
import pt.uc.dei.aor.pf.beans.TestUserInterface;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Named
@RequestScoped
public class LoginCDI {
	
	@Inject
	private UserSessionManagement userSessionManagement;
	
	private String email;
	
	private String password;

	public LoginCDI() {
	}

	public String login() {
		System.out.println("Atempting to log "+email+" with pass "+password);
		return this.userSessionManagement.login(email, password);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	// Populate: sai fora!!!
	@Inject
	TestUserInterface testUserBean;
	
	public void populate(){
		UserEntity newUser=new UserEntity();
		List<String> roles = new ArrayList<String>();
		
		roles.add(UserEntity.ROLE_ADMIN);
		roles.add(UserEntity.ROLE_MANAGER);
		roles.add(UserEntity.ROLE_INTERVIEWER);

		newUser=new UserEntity("admin@mail.com", "12345", "Admin", "Admin", roles);
		newUser.setDefaultRole(UserEntity.ROLE_ADMIN);

		this.testUserBean.save(newUser);
		
		
		
		
		
		roles.clear();
		roles.add(UserEntity.ROLE_MANAGER);
		roles.add(UserEntity.ROLE_CANDIDATE);

		newUser=new UserEntity("manager@mail.com", "12345", "Manager", "Manager", roles);
		newUser.setDefaultRole(UserEntity.ROLE_MANAGER);

		this.testUserBean.save(newUser);
		
		
		
		
		
		roles.clear();
		roles.add(UserEntity.ROLE_INTERVIEWER);

		newUser=new UserEntity("interviewer@mail.com", "12345", "Interviewer", "Interviewer", roles);
		newUser.setDefaultRole(UserEntity.ROLE_INTERVIEWER);

		this.testUserBean.save(newUser);
		
		
		
		
		
		roles.clear();
		roles.add(UserEntity.ROLE_CANDIDATE);

		newUser=new UserEntity("candidate@mail.com", "12345", "Candidate", "Candidate", roles);
		newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);

		this.testUserBean.save(newUser);
	}

}

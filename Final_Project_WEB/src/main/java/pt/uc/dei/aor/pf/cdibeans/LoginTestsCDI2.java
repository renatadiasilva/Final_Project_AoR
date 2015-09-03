package pt.uc.dei.aor.pf.cdibeans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.UserEJBInterfaceRENATA;
import pt.uc.dei.aor.pf.entities.UserEntity;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginTestsCDI2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5595743605410577249L;
	
	@Inject
	UserEJBInterfaceRENATA userEntityBean;

	public LoginTestsCDI2() {
	}
	
	public void createUserAdmin(){
		List<String> roles = new ArrayList<String>();
		roles.add(UserEntity.ROLE_ADMIN);
		
		UserEntity newUser=new UserEntity("admin@mail.com", "12345", "Admin", "Admin", roles);
		newUser.setDefaultRole(UserEntity.ROLE_ADMIN);
		
		this.userEntityBean.save(newUser);
	}
	
	public void createUserManager(){
		List<String> roles = new ArrayList<String>();
		roles.add(UserEntity.ROLE_MANAGER);
		
		UserEntity newUser=new UserEntity("manager@mail.com", "12345", "Admin", "Admin", roles);
		newUser.setDefaultRole(UserEntity.ROLE_MANAGER);
		
		this.userEntityBean.save(newUser);
	}
	
	public void createUserInterviewer(){
		List<String> roles = new ArrayList<String>();
		roles.add(UserEntity.ROLE_INTERVIEWER);
		
		UserEntity newUser=new UserEntity("interviewer@mail.com", "12345", "Admin", "Admin", roles);
		newUser.setDefaultRole(UserEntity.ROLE_INTERVIEWER);
		
		this.userEntityBean.save(newUser);
	}
	
	public void createUserCandidate(){
		List<String> roles = new ArrayList<String>();
		roles.add(UserEntity.ROLE_CANDIDATE);
		
		UserEntity newUser=new UserEntity("candidate@mail.com", "12345", "Admin", "Admin", roles);
		newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);
		
		this.userEntityBean.save(newUser);
	}

}

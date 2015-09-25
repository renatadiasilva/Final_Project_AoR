package pt.uc.dei.aor.pf;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.emailpattern.EmailPattern;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementInterface;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

@Named
@RequestScoped
public class ChangeEmailCDI {
	
	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private SecureMailManagementInterface mailEJB;
	
	private String password;
	
	private String newEmail;
	
	public void changeEmail(){
		boolean valid=true;
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String userEmail=request.getRemoteUser();
		UserEntity user=this.userEJB.findUserByEmail(userEmail);
		
		if(this.userEJB.checkPassword(user, userEmail)){
			valid=false;
			this.error("Password Errada");
		}
		
		if(!EmailPattern.checkEmailPattern(userEmail)){
			valid=false;
			this.error("Insira um email válido");
		}
		
		if(valid){
			user.setEmail(newEmail);
			user.setAuthenticated(false);
			this.userEJB.update(user);
			
//			this.userManagement.logout();
			
			this.mailEJB.newEmail(user);
		}
		
	}
	
	private void error(String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

}

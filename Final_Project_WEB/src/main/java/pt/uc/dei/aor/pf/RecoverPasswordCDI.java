package pt.uc.dei.aor.pf;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.session.UserSessionManagement;

@Named
@RequestScoped
public class RecoverPasswordCDI {
	
	@Inject
	UserSessionManagement userManagement;
	
	private String emailRecovery;
	
	public void recover(){
		this.userManagement.recoverPassword(this.emailRecovery.toLowerCase(), this.userManagement.getRandomPass());
	}

	public String getEmailRecovery() {
		return emailRecovery;
	}

	public void setEmailRecovery(String emailRecovery) {
		this.emailRecovery = emailRecovery;
	}

}

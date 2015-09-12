package pt.uc.dei.aor.pf;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.session.UserSessionManagement;

@Named
@RequestScoped
public class LoginCDI {

	@Inject
	private UserSessionManagement userSessionManagement;

	private String email;

	private String password;

	public LoginCDI() {
	}

	public void login() {
		this.userSessionManagement.login(this.email, this.password);
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

}
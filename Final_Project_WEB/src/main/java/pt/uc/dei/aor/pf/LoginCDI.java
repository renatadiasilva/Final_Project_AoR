package pt.uc.dei.aor.pf;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.session.UserSessionManagement;

@Named
@RequestScoped
public class LoginCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(LoginCDI.class);

	@Inject
	private UserSessionManagement userSessionManagement;

	private String email;

	private String password;

	public LoginCDI() {
	}

	public void login() {
		try {
			this.userSessionManagement.login(this.email, this.password);
		} catch (IllegalStateException e) {
			log.error("Já há um user logado na mesma sessão");
		}
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

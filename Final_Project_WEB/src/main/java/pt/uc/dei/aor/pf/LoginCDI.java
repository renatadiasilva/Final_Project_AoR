package pt.uc.dei.aor.pf;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.webManagement.UserManagementInterface;

@Named
@RequestScoped
public class LoginCDI {

	@Inject
	private UserSessionManagement userSessionManagement;

	@EJB
	UserManagementInterface userManagement;

	private String email;

	private String password;

	public LoginCDI() {
	}

	public void login() {
		// Verifica se já existe um user logado
		if(!this.userManagement.isUserLogged()){
			this.userSessionManagement.login(this.email, this.password);
		} else 
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Já existe um user logado", ""));
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

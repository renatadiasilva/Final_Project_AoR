package pt.uc.dei.aor.pf;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.emailpattern.EmailPattern;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementInterface;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

import java.io.Serializable;

@Named
@SessionScoped
public class ChangeEmailCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -356811975789126356L;

	@Inject
	private UserSessionManagement userManagement;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private SecureMailManagementInterface mailEJB;

	private String password;

	private String newEmail;

	public void clean(){
		this.password=this.newEmail="";
	}

	public void changeEmail(){
		boolean valid=true;

		// Vai buscar o user
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String userEmail=request.getRemoteUser();
		UserEntity user=this.userEJB.findUserByEmail(userEmail);

		// Verifica a password
		if(!this.userEJB.checkPassword(user, this.password)){
			valid=false;
			this.error("Password Errada.");
		}

		// Verifica a validade do novo email
		if(!EmailPattern.checkEmailPattern(this.newEmail)){
			valid=false;
			this.error("Novo email inválido.");
		}

		//Verifica se o novo email já está a uso
		if(this.userEJB.findUserByEmail(this.newEmail)!=null){
			valid=false;
			this.error("Novo email já se encontra em uso.");
		}

		// Se é válido
		if(valid){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Dados actualizados, consulte a sua inbox."));

			// Manipula e persiste a entidade com os novos dados e chave de autenticação
			user.setEmail(newEmail);
			user.setAuthenticated(false);
			user.setAuthenticationKey(RandomStringUtils.randomAlphanumeric(8));
			this.userEJB.update(user);

			// Envia o email para autenticar os novos dados
			this.mailEJB.newEmail(user);

			// Desloga o user
			this.userManagement.logout();

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

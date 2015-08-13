package pt.uc.dei.aor.pf.cdibeans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import pt.uc.dei.aor.pf.beans.UserInterface;
import pt.uc.dei.aor.pf.entities.Utilizador;

@Named
@SessionScoped
public class UserSession implements Serializable {

	private static final long serialVersionUID = -537522388020645530L;

	private boolean isLogged;
	private HttpSession session;
	private Utilizador current;
	@Inject
	private UserInterface ubean;
	private String password;
	@Inject Login login;

	public UserSession() {
		current = null;
		isLogged = false;
	}

	// editar informacao
	public void editar() {
		try {
		ubean.update(current);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"User info updated successfully.", "");
		FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception e){
			
		}
	}

	public void editarPass() {
		try {
		current.setPassword(password);
		ubean.updatePassword(current);
		password="";
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Password updated successfully.", "");
		FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (Exception e){
			
		}
	}
	//apagar conta
	public String deleteAccount(){
		try{
		ubean.delete(current);
		return login.logout();
		
		} catch (Exception e){
			
		}
		return null;
	}
	// Métodos para filtro da sessao HTTP //
	public void startSession() {
		session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		session.setAttribute("isLogged", isLogged);

	}

	public void endSession() {
		if (session != null) {
			session.invalidate();
			isLogged = false;
		}
	}

	// **************************************//

	// ***** Getters e Setters ***************//

	public boolean isLogged() {
		return isLogged;
	}

	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}

	public Utilizador getCurrent() {
		return current;
	}

	public void setCurrent(Utilizador current) {
		this.current = current;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

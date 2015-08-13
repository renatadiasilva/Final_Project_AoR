package pt.uc.dei.aor.pf.cdibeans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.UserInterface;
import pt.uc.dei.aor.pf.entities.Utilizador;

@Named
@SessionScoped
public class Login implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5054797081274626573L;
	@Inject
	private UserInterface ubean;
	@Inject
	private UserSession userSession;
	private Utilizador current = new Utilizador();

	public Login() {

	}

	public String login() {
		
	String mail= FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
	
	current=ubean.findUserByEmail(mail);
	current = ubean.login(current);
		if (current != null) {
			userSession.setCurrent(current);
			userSession.setLogged(true);
	
			return "/app/main?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Login Failed. E-mail or Password incorrect.", "");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return "/loginError.xhtml";
		}
	}

	public String logout() {
		String mail= FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
		//Vai deslogar o UTILIZADOR 
		System.out.println("Vai deslogar o UTILIZADOR"+mail );
		current=ubean.findUserByEmail(mail);
		ubean.logout(current);
		current=new Utilizador();
//		userSession.endSession();
		
		return "/index?faces-redirect=true";
	}

	public void register() {
		boolean success = ubean.save(current);
		if(success){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"User registered successfully.", "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"E-mail already registered", "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		current = new Utilizador();
	}

	public Utilizador getCurrent() {
		return current;
	}

	public void setCurrent(Utilizador current) {
		this.current = current;
	}

}

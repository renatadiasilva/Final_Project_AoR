package pt.uc.dei.aor.pf.cdibeans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@SessionScoped
public class LoginStart implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8706244561009198948L;
	@Inject
	Login log;
	
	private String mail="";
	private String password="";

	public String login(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try{
			request.login(mail, password);
	
			return log.login();
		} catch (ServletException e){
			context.addMessage(null, new FacesMessage("Login failed."));
			return "/loginError.xhtml";
		}
		
	}

	
	public String logout(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		try{
			String path=log.logout();
			this.mail="";
			this.password="";
			request.logout();
			return path;
			
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("Logout failed."));
			return log.logout();
		}
		
	}
	
	public String getMail() {
		return mail;
	}



	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
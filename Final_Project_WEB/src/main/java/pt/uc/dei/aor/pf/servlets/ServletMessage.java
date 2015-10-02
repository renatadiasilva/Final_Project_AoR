package pt.uc.dei.aor.pf.servlets;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@RequestScoped
public class ServletMessage {
	
	protected void addMessage(String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
	}

}

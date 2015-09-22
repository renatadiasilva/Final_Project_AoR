package pt.uc.dei.aor.pf.admin;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class EditUserCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1833165703643163171L;

	private static final Logger log = LoggerFactory.getLogger(EditUserCDI.class);

	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private PositionEJBInterface positionEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;

	private UserEntity userToEdit;

	private boolean admin, manager, interviewer, candidate;
	
	private String managerMessage, interviewerMessage;
	
	public void meh(){
		System.out.println("meh");
	}

	public void editUser(UserEntity userToEdit){
		this.userToEdit=userToEdit;
		
		log.info("Loading up user "+this.userToEdit.getEmail()+" to edit his roles.");

		this.admin=this.manager=this.interviewer=this.candidate=false;

		for(String role:this.userToEdit.getRoles()){
			if(role.equals(Constants.ROLE_ADMIN))this.admin=true;
			if(role.equals(Constants.ROLE_MANAGER))this.manager=true;
			if(role.equals(Constants.ROLE_INTERVIEWER))this.interviewer=true;
			if(role.equals(Constants.ROLE_CANDIDATE))this.candidate=true;
		}

		this.isShowInterviewer();
		this.isShowManager();
	}

	public void saveUser(){
		List<String>roles=new ArrayList<>();
		
		// Verifica se tem pelo menos um role
		if(this.admin||this.manager||this.interviewer||this.candidate){
			if(this.candidate){
				roles.add(Constants.ROLE_CANDIDATE);
				this.userToEdit.setDefaultRole(Constants.ROLE_CANDIDATE);
			}
			if(this.interviewer){
				roles.add(Constants.ROLE_INTERVIEWER);
				this.userToEdit.setDefaultRole(Constants.ROLE_INTERVIEWER);
			}
			if(this.manager){
				roles.add(Constants.ROLE_MANAGER);
				this.userToEdit.setDefaultRole(Constants.ROLE_MANAGER);
			}
			if(this.admin){
				roles.add(Constants.ROLE_ADMIN);
				this.userToEdit.setDefaultRole(Constants.ROLE_ADMIN);
			}

			this.userToEdit.setRoles(roles);

			this.userEJB.update(userToEdit);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Roles do user "+this.userToEdit.getEmail()+" actualizados."));

			log.info("Edited "+this.userToEdit.getEmail()+" roles.");
			
		}else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("O utilizador precisa de pelo menos um role, dados não actualizados."));
		
	}

	public void deleteUser(UserEntity user){
		// Listar as posiçoes dependentes do user antes de apagar!!!

		this.userEJB.delete(user);
	}

	public String getUserEmail(){
		if(this.userToEdit==null)return "";
		return this.userToEdit.getEmail();
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isManager() {
		return manager;
	}

	public void setManager(boolean manager) {
		this.manager = manager;
	}

	public boolean isInterviewer() {
		return interviewer;
	}

	public void setInterviewer(boolean interviewer) {
		this.interviewer = interviewer;
	}

	public boolean isCandidate() {
		return candidate;
	}

	public void setCandidate(boolean candidate) {
		this.candidate = candidate;
	}

	public boolean isShowManager() {
		if(!this.positionEJB.findOpenPositionsManagedByUser(this.userToEdit).isEmpty()){
			this.managerMessage="Este gestor tem posições à sua dependência.";
			this.manager=true;
			return false;
		}
		this.managerMessage="";
		return true;
	}

	public boolean isShowInterviewer() {
		if(!this.interviewEJB.findScheduledInterviewsByUser(this.userToEdit).isEmpty()){
			this.interviewerMessage="Este entrevistador tem entrevistas à sua dependência.";
			this.interviewer=true;
			return false;
		}
		this.interviewerMessage="";
		return true;
	}

	public String getManagerMessage() {
		return managerMessage;
	}

	public String getInterviewerMessage() {
		return interviewerMessage;
	}

}

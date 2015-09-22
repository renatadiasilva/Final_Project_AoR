package pt.uc.dei.aor.pf.admin;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private UserEntity userToEdit;

	private boolean admin, manager, interviewer, candidate;
	
	public void meh(){
		System.out.println("meh");
	}

	public void editUser(UserEntity userToEdit){
		System.out.println("Ninjas!");
		
		log.info("Loading up user "+this.userToEdit.getEmail()+" to edit his roles.");

		this.userToEdit=userToEdit;
		this.admin=this.manager=this.interviewer=this.candidate=false;

		for(String role:this.userToEdit.getRoles()){
			if(role.equals(Constants.ROLE_ADMIN))this.admin=true;
			if(role.equals(Constants.ROLE_MANAGER))this.manager=true;
			if(role.equals(Constants.ROLE_INTERVIEWER))this.interviewer=true;
			if(role.equals(Constants.ROLE_CANDIDATE))this.candidate=true;
		}
		
		System.out.println(admin);
		System.out.println(manager);
		System.out.println(interviewer);
		System.out.println(admin);
	}

	public void saveUser(){
		// Ver o role por defeito!!!
		// Tem de ter pelo menos um role!!!
		List<String>roles=new ArrayList<>();

		if(this.admin)roles.add(Constants.ROLE_ADMIN);
		if(this.manager)roles.add(Constants.ROLE_MANAGER);
		if(this.interviewer)roles.add(Constants.ROLE_INTERVIEWER);
		if(this.candidate)roles.add(Constants.ROLE_CANDIDATE);

		this.userToEdit.setRoles(roles);

		this.userEJB.update(userToEdit);

		log.info("Edited "+this.userToEdit.getEmail()+" roles.");
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

}

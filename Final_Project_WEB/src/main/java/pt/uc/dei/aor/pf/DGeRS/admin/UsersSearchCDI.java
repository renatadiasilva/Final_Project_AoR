package pt.uc.dei.aor.pf.DGeRS.admin;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.UserEntity;


@Named
@RequestScoped
public class UsersSearchCDI {

	@EJB
	private UserEJBInterface userEJB;
	
	// search fields
	private String email, fname, lname;

	private UserEntity user;
	
	private List<UserEntity> ulist;

	public UsersSearchCDI() {
	}
	
	public void search() {
		//all users
		this.ulist = userEJB.findAll();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public List<UserEntity> getUlist() {
		return ulist;
	}

	public void setUlist(List<UserEntity> ulist) {
		this.ulist = ulist;
	}

}

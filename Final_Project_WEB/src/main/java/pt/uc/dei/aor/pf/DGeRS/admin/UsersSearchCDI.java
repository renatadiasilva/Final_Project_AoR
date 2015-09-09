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
	private String email, fname, lname, keyword;

	private UserEntity user;
	
	private List<UserEntity> ulist;

	public UsersSearchCDI() {
	}
	
	// ALL
	
	public void searchTest() {
		this.ulist = userEJB.findTest();
	}	
	
	public void searchAll() {
		this.ulist = userEJB.findAll();
	}

	public void searchAllAdmins() {
		this.ulist = userEJB.findAllAdmins();
	}

	public void searchAllManagers() {
		this.ulist = userEJB.findAllManagers();
	}

	public void searchAllInterviewers() {
		this.ulist = userEJB.findAllInterviewers();
	}

	public void searchAllCandidates() {
		this.ulist = userEJB.findAllCandidates();
	}

	// INNER USERS
	
	public void searchByEmail() {
		//search by email pattern		
		this.ulist = userEJB.findUsersByEmail(preparePattern(email));
	}

	public void searchByName() {
		//seach by Name (first/last)
		this.ulist = userEJB.findUsersByName(preparePattern(fname));
	}
	
	public void searchByKeyword() {
		//seach by Name (first/last)
		this.ulist = userEJB.findUsersByKeyword(preparePattern(keyword));
	}
	
	// CANDIDATES

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
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private String preparePattern(String searchWord) {
		return "%"+searchWord.toUpperCase()+"%";
	}

}

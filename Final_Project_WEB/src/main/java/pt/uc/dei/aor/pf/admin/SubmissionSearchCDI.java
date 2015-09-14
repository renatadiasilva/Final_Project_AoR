package pt.uc.dei.aor.pf.admin;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;


@Named
@RequestScoped
public class SubmissionSearchCDI {

	private static final Logger log = LoggerFactory.getLogger(SubmissionSearchCDI.class);

	@EJB
	private SubmissionEJBInterface submissionEJB;

	// search fields
	private Date date1, date2;
	private String email, fname, lname, role, keyword;

	private List<SubmissionEntity> slist;

	public SubmissionSearchCDI() {
	}

	// ALL

	public void searchAll() {
		log.info("Searching for all users");
		this.slist = submissionEJB.findAll();
	}

//	public void searchAllAdmins() {
//		log.info("Searching for all administrators");
//		this.ulist = submissionEJB.findAllAdmins();
//	}
//
//	public void searchAllManagers() {
//		log.info("Searching for all managers");
//		this.ulist = submissionEJB.findAllManagers();
//	}
//
//	public void searchAllInterviewers() {
//		log.info("Searching for all interviwers");
//		this.ulist = submissionEJB.findAllInterviewers();
//	}
//
//	// INTERNAL USERS
//
//	public void searchByEmail() {
//		log.info("Searching for internal users by email");
//		String pattern = SearchPattern.preparePattern(email);
//		log.debug("Internal search string: "+pattern);
//		this.ulist = submissionEJB.findUsersByEmail(pattern);
//	}
//
//	public void searchByName() {
//		log.info("Searching for internal users by name (first/second)");
//		String pattern = SearchPattern.preparePattern(fname);
//		log.debug("Internal search string: "+pattern);
//		this.ulist = submissionEJB.findUsersByName(pattern);
//	}
//
//	public void searchByKeyword() {
//		log.info("Searching for internal users by keyword");
//		String pattern = SearchPattern.preparePattern(keyword);
//		log.debug("Internal search string: "+pattern);
//		this.ulist = submissionEJB.findUsersByKeyword(pattern);
//	}
//
//	public void searchByKeywordAndRole() {
//		log.info("Searching for internal users by keyword");
//		String pattern = SearchPattern.preparePattern(keyword);
//		log.debug("Internal search string: "+pattern);
//		this.ulist = submissionEJB.findUsersByKeywordAndRole(pattern, role);
//	}

	// getters e setters

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<SubmissionEntity> getSlist() {
		return slist;
	}

	public void setSlist(List<SubmissionEntity> slist) {
		this.slist = slist;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	// tirar
	public void meh() {
		// Deal with it
	}

}
package pt.uc.dei.aor.pf.admin;

import java.text.Normalizer;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.UserEntity;


@Named
@RequestScoped
public class UsersSearchCDI {

	private static final Logger log = LoggerFactory.getLogger(UsersSearchCDI.class);
	
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
		log.info("Test searching");
		this.ulist = userEJB.findTest(preparePattern(keyword));
	}	

	public void searchAll() {
		log.info("Searching for all users");
		this.ulist = userEJB.findAll();
	}

	public void searchAllAdmins() {
		log.info("Searching for all administrators");
		this.ulist = userEJB.findAllAdmins();
	}

	public void searchAllManagers() {
		log.info("Searching for all managers");
		this.ulist = userEJB.findAllManagers();
	}

	public void searchAllInterviewers() {
		log.info("Searching for all interviwers");
		this.ulist = userEJB.findAllInterviewers();
	}

	public void searchAllCandidates() {
		log.info("Searching for all candidates");
		this.ulist = userEJB.findAllCandidates();
	}

	// INTERNAL USERS

	public void searchByEmail() {
		log.info("Searching for internal users by email");
		String pattern = preparePattern(email);
		log.debug("Search word: "+pattern);
		this.ulist = userEJB.findUsersByEmail(pattern);
	}

	public void searchByName() {
		log.info("Searching for internal users by name (first/second)");
		String pattern = preparePattern(fname);
		log.debug("Search word: "+pattern);
		this.ulist = userEJB.findUsersByName(pattern);
	}

	public void searchByKeyword() {
		log.info("Searching for internal users by keyword");
		String pattern = preparePattern(keyword);
		log.debug("Search word: "+pattern);
		this.ulist = userEJB.findUsersByKeyword(pattern);
	}

	// CANDIDATES
	
	
	
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

		// removes all non-word characters of the word
		String pattern = searchWord.replaceAll("\\W", "");
		// removes all whitespaces of the word
		//		String pattern = searchWord.replaceAll("\\s", "");
		System.out.println(pattern);

		// adds % because of database search
		pattern = "%"+pattern.toUpperCase()+"%";
		System.out.println(pattern);

		// separates all of the accent marks from the characters
		pattern = Normalizer.normalize(pattern, Normalizer.Form.NFD);
		System.out.println(pattern);

		//		string = string.replaceAll("[^\\p{ASCII}]", "");

		// compares each character against being a letter and 
		// throw out the ones that aren't.
		pattern = pattern.replaceAll("\\p{M}", "");
		System.out.println(pattern);

		return pattern;
	}

	public void meh() {
		// Deal with it
	}

}
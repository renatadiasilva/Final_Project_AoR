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
	private String email, fname, lname, role, keyword;
	private String address, city, phone, mobile, country;
	private String course, school;

	//	PositionEntity position;  // testar (dado)

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
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findUsersByEmail(pattern);
	}

	public void searchByName() {
		log.info("Searching for internal users by name (first/second)");
		String pattern = preparePattern(fname);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findUsersByName(pattern);
	}

	public void searchByKeyword() {
		log.info("Searching for internal users by keyword");
		String pattern = preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findUsersByKeyword(pattern);
	}

	public void searchByKeywordAndRole() {
		log.info("Searching for internal users by keyword");
		String pattern = preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findUsersByKeywordAndRole(pattern, role);
	}

	// CANDIDATES

	public void searchCandidatesByEmail() {
		log.info("Searching for candidates by email");
		String pattern = preparePattern(email);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByEmail(pattern);
	}	

	public void searchCandidatesByFirstName() {
		log.info("Searching for candidates by first name");
		String pattern = preparePattern(fname);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByFirstName(pattern);
	}	

	public void searchCandidatesByLastName() {
		log.info("Searching for candidates by last name");
		String pattern = preparePattern(lname);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByLastName(pattern);
	}	

	public void searchCandidatesByAddress() {
		log.info("Searching for candidates by address");
		String pattern = preparePattern(address);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByAddress(pattern);
	}	

	public void searchCandidatesByCity() {
		log.info("Searching for candidates by city");
		String pattern = preparePattern(city);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCity(pattern);
	}	

	public void searchCandidatesByPhone() {
		log.info("Searching for candidates by phone");
		String pattern = preparePattern(phone);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByPhone(pattern);
	}	

	public void searchCandidatesByMobile() {
		log.info("Searching for candidates by mobile");
		String pattern = preparePattern(mobile);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByMobile(pattern);
	}	

	public void searchCandidatesByCountry() {
		log.info("Searching for candidates by country");
		String pattern = preparePattern(country);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCountry(pattern);
	}	

	public void searchCandidatesByCourse() {
		log.info("Searching for candidates by course");
		String pattern = preparePattern(course);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCourse(pattern);
	}	

	public void searchCandidatesBySchool() {
		log.info("Searching for candidates by school");
		String pattern = preparePattern(school);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesBySchool(pattern);
	}	

	//	public void searchCandidatesByPositionOnly() {
	//		log.info("Searching for candidates by position");
	//		log.debug("Position: "+position.getPositionCode());
	//		this.ulist = userEJB.findCandidatesByPosition(position);
	//	}	

	//	public void searchCandidatesByPositionShort() {
	//		log.info("Searching for candidates by position and email/name");
	//		log.debug("Position: "+position.getPositionCode());
	// 		String pattern1 = preparePattern(email);
	//		log.debug("Internal search string (1): "+pattern1);
	//		String pattern2 = preparePattern(fname);
	//		log.debug("Internal search string (2): "+pattern2);
	//		String pattern3 = preparePattern(lname);
	//		log.debug("Internal search string (3): "+pattern3);
	//		this.ulist = userEJB.findCandidatesByPosition(pattern1, 
	//			pattern2, partern3, position);
	//	}	

	//	public void searchCandidatesByPositionLong() {
	//		log.info("Searching for candidates by position and more attributes");
	//		log.debug("Position: "+position.getPositionCode());
	//		String pattern1 = preparePattern(email);
	//		log.debug("Internal search string (1): "+pattern1);
	//		String pattern2 = preparePattern(fname);
	//		log.debug("Internal search string (2): "+pattern2);
	//		String pattern3 = preparePattern(lname);
	//		log.debug("Internal search string (3): "+pattern3);
	//		String pattern4 = preparePattern(address);
	//		log.debug("Internal search string (4): "+pattern4);
	//		String pattern5 = preparePattern(city);
	//		log.debug("Internal search string (5): "+pattern5);
	//		String pattern6 = preparePattern(country);
	//		log.debug("Internal search string (6): "+pattern6);
	//		String pattern7 = preparePattern(course);
	//		log.debug("Internal search string (7): "+pattern7);
	//		String pattern8 = preparePattern(school);
	//		log.debug("Internal search string (8): "+pattern8);
	//		this.ulist = userEJB.findCandidatesByPosition(pattern1, 
	//			pattern2, pattern3, pattern4, pattern5, pattern6,
	//			pattern7, pattern8, position);
	//}	

	public void searchCandidates() {
		log.info("Searching for candidates by several attributes");
		String pattern1 = preparePattern(email);
		log.debug("Internal search string (1): "+pattern1);
		String pattern2 = preparePattern(fname);
		log.debug("Internal search string (2): "+pattern2);
		String pattern3 = preparePattern(lname);
		log.debug("Internal search string (3): "+pattern3);
		String pattern4 = preparePattern(address);
		log.debug("Internal search string (4): "+pattern4);
		String pattern5 = preparePattern(city);
		log.debug("Internal search string (5): "+pattern5);
		String pattern6 = preparePattern(country);
		log.debug("Internal search string (6): "+pattern6);
		String pattern7 = preparePattern(course);
		log.debug("Internal search string (7): "+pattern7);
		String pattern8 = preparePattern(school);
		log.debug("Internal search string (8): "+pattern8);
		this.ulist = userEJB.findCandidates(pattern1, 
				pattern2, pattern3, pattern4, pattern5, pattern6,
				pattern7, pattern8);
	}	
	
	//falta no test
	public void searchCandidatesByKeyword() {
		log.info("Searching for candidates by keyword");
		String pattern = preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByKeyword(pattern);
	}	

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
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

	// tirar
	public void meh() {
		// Deal with it
	}

}
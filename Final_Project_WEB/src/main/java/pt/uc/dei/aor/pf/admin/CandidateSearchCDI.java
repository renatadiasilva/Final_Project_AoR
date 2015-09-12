package pt.uc.dei.aor.pf.admin;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;


@Named
@RequestScoped
public class CandidateSearchCDI {

	private static final Logger log = LoggerFactory.getLogger(CandidateSearchCDI.class);

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	// search fields
	private String email, fname, lname, keyword;
	private String address, city, phone, country;
	private String course, school;

	private Long idPos;   // id=18, id=19

	private List<UserEntity> ulist;

	public CandidateSearchCDI() {
	}

	// CANDIDATES

	public void searchAllCandidates() {
		log.info("Searching for all candidates");
		this.ulist = userEJB.findAllCandidates();
	}

	public void searchCandidatesByEmail() {
		log.info("Searching for candidates by email");
		String pattern = SearchPattern.preparePattern(email);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByEmail(pattern);
	}	

	public void searchCandidatesByFirstName() {
		log.info("Searching for candidates by first name");
		String pattern = SearchPattern.preparePattern(fname);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByFirstName(pattern);
	}	

	public void searchCandidatesByLastName() {
		log.info("Searching for candidates by last name");
		String pattern = SearchPattern.preparePattern(lname);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByLastName(pattern);
	}	

	public void searchCandidatesByAddress() {
		log.info("Searching for candidates by address");
		String pattern = SearchPattern.preparePattern(address);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByAddress(pattern);
	}	

	public void searchCandidatesByCity() {
		log.info("Searching for candidates by city");
		String pattern = SearchPattern.preparePattern(city);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCity(pattern);
	}	

	public void searchCandidatesByPhone() {
		log.info("Searching for candidates by phone");
		String pattern = SearchPattern.preparePattern(phone);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByPhone(pattern);
	}	

	public void searchCandidatesByCountry() {
		log.info("Searching for candidates by country");
		String pattern = SearchPattern.preparePattern(country);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCountry(pattern);
	}	

	public void searchCandidatesByCourse() {
		log.info("Searching for candidates by course");
		String pattern = SearchPattern.preparePattern(course);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesByCourse(pattern);
	}	

	public void searchCandidatesBySchool() {
		log.info("Searching for candidates by school");
		String pattern = SearchPattern.preparePattern(school);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findCandidatesBySchool(pattern);
	}	

	// acentos!!!!

	public void searchCandidatesByPositionOnly() {
		log.info("Searching for candidates by position");
		PositionEntity pos = positionEJB.find(idPos);
		if (pos != null) {
			log.debug("Position: "+pos.getPositionCode());
			this.ulist = userEJB.findCandidatesByPosition(pos);
		} else log.error("No position with id "+idPos);
	}	

	public void searchCandidatesByPositionShort() {
		log.info("Searching for candidates by position and email/name");
		PositionEntity pos = positionEJB.find(idPos);
		if (pos != null) {
			log.debug("Position: "+pos.getPositionCode());
			String pattern1 = SearchPattern.preparePattern(email);
			log.debug("Internal search string (email): "+pattern1);
			String pattern2 = SearchPattern.preparePattern(fname);
			log.debug("Internal search string (first name): "+pattern2);
			String pattern3 = SearchPattern.preparePattern(lname);
			log.debug("Internal search string (last name): "+pattern3);
			this.ulist = userEJB.findCandidatesByPosition(pattern1, 
					pattern2, pattern3, pos);
		}		
	}	

	public void searchCandidatesByPositionLong() {
		log.info("Searching for candidates by position and more attributes");
		PositionEntity pos = positionEJB.find(idPos);
		if (pos != null) {
			log.debug("Position: "+pos.getPositionCode());
			String pattern1 = SearchPattern.preparePattern(email);
			log.debug("Internal search string (email): "+pattern1);
			String pattern2 = SearchPattern.preparePattern(fname);
			log.debug("Internal search string (first name): "+pattern2);
			String pattern3 = SearchPattern.preparePattern(lname);
			log.debug("Internal search string (last name): "+pattern3);
			String pattern4 = SearchPattern.preparePattern(address);
			log.debug("Internal search string (address): "+pattern4);
			String pattern5 = SearchPattern.preparePattern(city);
			log.debug("Internal search string (city): "+pattern5);
			String pattern6 = SearchPattern.preparePattern(country);
			log.debug("Internal search string (country): "+pattern6);
			String pattern7 = SearchPattern.preparePattern(course);
			log.debug("Internal search string (course): "+pattern7);
			String pattern8 = SearchPattern.preparePattern(school);
			log.debug("Internal search string (school): "+pattern8);
			this.ulist = userEJB.findCandidatesByPosition(pattern1, 
					pattern2, pattern3, pattern4, pattern5, pattern6,
					pattern7, pattern8, pos);
		}		
	}	

	public void searchCandidates() {
		log.info("Searching for candidates by several attributes");
		String pattern1 = SearchPattern.preparePattern(email);
		log.debug("Internal search string (email): "+pattern1);
		String pattern2 = SearchPattern.preparePattern(fname);
		log.debug("Internal search string (first name): "+pattern2);
		String pattern3 = SearchPattern.preparePattern(lname);
		log.debug("Internal search string (last name): "+pattern3);
		String pattern4 = SearchPattern.preparePattern(address);
		log.debug("Internal search string (address): "+pattern4);
		String pattern5 = SearchPattern.preparePattern(city);
		log.debug("Internal search string (city): "+pattern5);
		String pattern6 = SearchPattern.preparePattern(country);
		log.debug("Internal search string (country): "+pattern6);
		String pattern7 = SearchPattern.preparePattern(course);
		log.debug("Internal search string (course): "+pattern7);
		String pattern8 = SearchPattern.preparePattern(school);
		log.debug("Internal search string (school): "+pattern8);
		this.ulist = userEJB.findCandidates(pattern1, 
				pattern2, pattern3, pattern4, pattern5, pattern6,
				pattern7, pattern8);
	}	

	public void searchCandidatesByKeyword() {
		log.info("Searching for candidates by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
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

	public Long getIdPos() {
		return idPos;
	}

	public void setIdPos(Long idPos) {
		this.idPos = idPos;
	}

}
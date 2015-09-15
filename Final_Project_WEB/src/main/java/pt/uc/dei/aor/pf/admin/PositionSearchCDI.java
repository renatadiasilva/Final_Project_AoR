package pt.uc.dei.aor.pf.admin;

import java.util.Date;
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
public class PositionSearchCDI {

	private static final Logger log = LoggerFactory.getLogger(PositionSearchCDI.class);

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	// search fields
	private Date date1, date2;

	private String code, title, location, status;
	private String company, tarea, keyword;

	private List<String> locations; // select buttons

	private Long idU;
	private Long idPos;

	private int days;

	private List<PositionEntity> plist;

	public PositionSearchCDI() {
	}

	public void searchAll() {
		log.info("Searching for all positions");
		this.plist = positionEJB.findAll();
	}

	// needed??
	public void searchPositionByCode() {
		log.info("Searching for position by exact code");
		log.debug("Code "+code);
		this.plist.add(positionEJB.findPositionByCode(code));
	}	

	public void searchPositionsByCode() {
		log.info("Searching for positions by code");
		String pattern = SearchPattern.preparePattern(code);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByCode(pattern);
	}	

	//open??
	public void searchPositionsByDate() {
		log.info("Searching for positions between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.plist = positionEJB.findPositionsByDate(date1, date2);
	}	

	public void searchPositionsByTitle() {
		log.info("Searching for positions by title");
		String pattern = SearchPattern.preparePattern(title);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByTitle(pattern);
	}	

	public void searchPositionsByLocationsOne() {
		log.info("Searching for positions with one of the given locations");
		log.debug("Locations: ");
		for(String l : locations) log.debug(l); 
		this.plist = positionEJB.findPositionsByLocationsOne(locations);
	}	

	public void searchPositionsByLocationsAll() {
		log.info("Searching for positions with all the given locations");
		log.debug("Locations: ");
		for(String l : locations) log.debug(l); 
		this.plist = positionEJB.findPositionsByLocationsAll(locations);
	}	

	public void searchPositionsByStatus() {
		log.info("Searching for positions by status");
		log.debug("Status "+status);
		this.plist = positionEJB.findPositionsByStatus(status);
	}	

	public void searchPositionsByCompany() {
		log.info("Searching for positions by company");
		String pattern = SearchPattern.preparePattern(company);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByCompany(pattern);
	}	

	public void searchPositionsByTechArea() {
		log.info("Searching for positions by tecnical area");
		String pattern = SearchPattern.preparePattern(tarea);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByTechArea(pattern);
	}	

	public void searchPositions() {
		log.info("Searching for positions by several attributes");		
		String pattern1 = SearchPattern.preparePattern(code);
		log.debug("Internal search string (code): "+pattern1);
		String pattern2 = SearchPattern.preparePattern(title);
		log.debug("Internal search string (title): "+pattern2);
		String pattern3 = SearchPattern.preparePattern(location);
		log.debug("Internal search string (location): "+pattern3);
		String pattern4 = SearchPattern.preparePattern(status);
		log.debug("Internal search string (status): "+pattern4);
		String pattern5 = SearchPattern.preparePattern(company);
		log.debug("Internal search string (ccompany): "+pattern5);
		String pattern6 = SearchPattern.preparePattern(tarea);
		log.debug("Internal search string (tech area): "+pattern6);
		this.plist = positionEJB.findPositions(date1, date2, pattern1,
				pattern2, pattern3, pattern4, pattern5, pattern6);
	}	

	public void searchPositionsByManager() {
		log.info("Searching for positions of a manager by several attributes");
		UserEntity manager = userEJB.find(idU);
		if ( (manager != null) && (manager.getRoles().contains("MANAGER")) ) {
			log.debug("Manager "+manager.getFirstName());
			String pattern1 = SearchPattern.preparePattern(code);
			log.debug("Internal search string (code): "+pattern1);
			String pattern2 = SearchPattern.preparePattern(title);
			log.debug("Internal search string (title): "+pattern2);
			String pattern3 = SearchPattern.preparePattern(location);
			log.debug("Internal search string (location): "+pattern3);
			String pattern4 = SearchPattern.preparePattern(status);
			log.debug("Internal search string (status): "+pattern4);
			String pattern5 = SearchPattern.preparePattern(company);
			log.debug("Internal search string (ccompany): "+pattern5);
			String pattern6 = SearchPattern.preparePattern(tarea);
			log.debug("Internal search string (tech area): "+pattern6);
			this.plist = positionEJB.findPositionsByManager(date1, date2, pattern1,
					pattern2, pattern3, pattern4, pattern5, pattern6, manager);
		} else log.error("No manager with id "+idU);
	}	
	
	public void searchOpenPositions() {
		log.info("Searching for all open positions");
		this.plist = positionEJB.findOpenPositions();
	}

	public void searchPositionsByCandidate() {
		log.info("Searching for positions by candidate");
		UserEntity candidate = userEJB.find(idU);
		if ( (candidate != null) && (candidate.getRoles().contains("CANDIDATE")) ) {
			log.debug("Candidate "+candidate.getFirstName());
			this.plist = positionEJB.findPositionsByCandidate(candidate);
		} else log.error("No candidate with id "+idU);
	}	

	//	public void searchCandidatesByPositionOnly() {
	//		log.info("Searching for candidates by position");
	//		PositionEntity pos = positionEJB.find(idPos);
	//		if (pos != null) {
	//			log.debug("Position: "+pos.getPositionCode());
	//			this.plist = userEJB.findCandidatesByPosition(pos);
	//		} else log.error("No position with id "+idPos);
	//	}	
	//
	//	public void searchCandidatesByPositionShort() {
	//		log.info("Searching for candidates by position and email/name");
	//		PositionEntity pos = positionEJB.find(idPos);
	//		if (pos != null) {
	//			log.debug("Position: "+pos.getPositionCode());
	//			String pattern1 = SearchPattern.preparePattern(email);
	//			log.debug("Internal search string (email): "+pattern1);
	//			String pattern2 = SearchPattern.preparePattern(fname);
	//			log.debug("Internal search string (first name): "+pattern2);
	//			String pattern3 = SearchPattern.preparePattern(lname);
	//			log.debug("Internal search string (last name): "+pattern3);
	//			this.plist = userEJB.findCandidatesByPosition(pattern1, 
	//					pattern2, pattern3, pos);
	//		}		
	//	}	
	//
	//	public void searchCandidatesByPositionLong() {
	//		log.info("Searching for candidates by position and more attributes");
	//		PositionEntity pos = positionEJB.find(idPos);
	//		if (pos != null) {
	//			log.debug("Position: "+pos.getPositionCode());
	//			String pattern1 = SearchPattern.preparePattern(email);
	//			log.debug("Internal search string (email): "+pattern1);
	//			String pattern2 = SearchPattern.preparePattern(fname);
	//			log.debug("Internal search string (first name): "+pattern2);
	//			String pattern3 = SearchPattern.preparePattern(lname);
	//			log.debug("Internal search string (last name): "+pattern3);
	//			String pattern4 = SearchPattern.preparePattern(address);
	//			log.debug("Internal search string (address): "+pattern4);
	//			String pattern5 = SearchPattern.preparePattern(city);
	//			log.debug("Internal search string (city): "+pattern5);
	//			String pattern6 = SearchPattern.preparePattern(country);
	//			log.debug("Internal search string (country): "+pattern6);
	//			String pattern7 = SearchPattern.preparePattern(course);
	//			log.debug("Internal search string (course): "+pattern7);
	//			String pattern8 = SearchPattern.preparePattern(school);
	//			log.debug("Internal search string (school): "+pattern8);
	//			this.plist = userEJB.findCandidatesByPosition(pattern1, 
	//					pattern2, pattern3, pattern4, pattern5, pattern6,
	//					pattern7, pattern8, pos);
	//		}		
	//	}	
	//
	//	public void searchCandidates() {
	//		log.info("Searching for candidates by several attributes");
	//		String pattern1 = SearchPattern.preparePattern(email);
	//		log.debug("Internal search string (email): "+pattern1);
	//		String pattern2 = SearchPattern.preparePattern(fname);
	//		log.debug("Internal search string (first name): "+pattern2);
	//		String pattern3 = SearchPattern.preparePattern(lname);
	//		log.debug("Internal search string (last name): "+pattern3);
	//		String pattern4 = SearchPattern.preparePattern(address);
	//		log.debug("Internal search string (address): "+pattern4);
	//		String pattern5 = SearchPattern.preparePattern(city);
	//		log.debug("Internal search string (city): "+pattern5);
	//		String pattern6 = SearchPattern.preparePattern(country);
	//		log.debug("Internal search string (country): "+pattern6);
	//		String pattern7 = SearchPattern.preparePattern(course);
	//		log.debug("Internal search string (course): "+pattern7);
	//		String pattern8 = SearchPattern.preparePattern(school);
	//		log.debug("Internal search string (school): "+pattern8);
	//		this.plist = userEJB.findCandidates(pattern1, 
	//				pattern2, pattern3, pattern4, pattern5, pattern6,
	//				pattern7, pattern8);
	//	}	
	//
	//	public void searchCandidatesByKeyword() {
	//		log.info("Searching for candidates by keyword");
	//		String pattern = SearchPattern.preparePattern(keyword);
	//		log.debug("Internal search string: "+pattern);
	//		this.plist = userEJB.findCandidatesByKeyword(pattern);
	//	}

	// getters e setters

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getIdU() {
		return idU;
	}

	public void setIdU(Long idU) {
		this.idU = idU;
	}

	public Long getIdPos() {
		return idPos;
	}

	public void setIdPos(Long idPos) {
		this.idPos = idPos;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public List<PositionEntity> getPlist() {
		return plist;
	}

	public void setPlist(List<PositionEntity> plist) {
		this.plist = plist;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}	

}
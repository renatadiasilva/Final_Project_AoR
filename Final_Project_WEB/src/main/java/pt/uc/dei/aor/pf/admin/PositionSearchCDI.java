package pt.uc.dei.aor.pf.admin;

import java.util.ArrayList;
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

	private String code, title, status;
	private String company, tarea, keyword;

	private String location;
	private List<String> locations;

	private Long idU;
	private Long idPos;

	private int days;

	private boolean result;

	private List<PositionEntity> plist;

	public PositionSearchCDI() {
	}

	public void searchAll() {
		log.info("Searching for all positions");
		this.plist = positionEJB.findAll();
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
		String pattern4 = SearchPattern.preparePattern(status);
		log.debug("Internal search string (status): "+pattern4);
		String pattern5 = SearchPattern.preparePattern(company);
		log.debug("Internal search string (ccompany): "+pattern5);
		String pattern6 = SearchPattern.preparePattern(tarea);
		log.debug("Internal search string (tech area): "+pattern6);
		List<String> pattern = new ArrayList<String>(locations.size());		
		for (String l : locations) {
			String p = SearchPattern.preparePattern(l);
			pattern.add(p);
			log.debug("Internal search string (location "+
				(locations.indexOf(l)+1)+"): "+p);
		}
		this.plist = positionEJB.findPositions(date1, date2, pattern1,
				pattern2, pattern, pattern4, pattern5, pattern6);
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
			String pattern4 = SearchPattern.preparePattern(status);
			log.debug("Internal search string (status): "+pattern4);
			String pattern5 = SearchPattern.preparePattern(company);
			log.debug("Internal search string (ccompany): "+pattern5);
			String pattern6 = SearchPattern.preparePattern(tarea);
			log.debug("Internal search string (tech area): "+pattern6);
			List<String> pattern = new ArrayList<String>(locations.size());		
			for (String l : locations) {
				String p = SearchPattern.preparePattern(l);
				pattern.add(p);
				log.debug("Internal search string (location "+
					(locations.indexOf(l)+1)+"): "+p);
			}
			this.plist = positionEJB.findPositionsByManager(date1, date2, pattern1,
					pattern2, pattern, pattern4, pattern5, pattern6, manager);
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

	public void alreadyCandidateOfPosition() {
		log.info("Checking if candidate is already associated with a positions");
		UserEntity candidate = userEJB.find(idU);
		if ( (candidate != null) && (candidate.getRoles().contains("CANDIDATE")) ) {
			log.debug("Candidate "+candidate.getFirstName());
			PositionEntity position = positionEJB.find(idPos);
			if (position != null) {
				log.debug("Position "+position.getPositionCode());
				result = positionEJB.alreadyCandidateOfPosition(candidate, position);
			} else log.error("No position with id "+idPos);
		} else log.error("No candidate with id "+idU);
	}

	public void searchCloseToSLAPositions() {
		log.info("Searching for close to SLA positions");
		this.plist = positionEJB.findCloseToSLAPositions(days);
	}

	public void searchPositionsByKeyword() {
		log.info("Searching for positions by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByKeyword(pattern);
	}

	public void searchPositionsByKeywordAndManager() {
		log.info("Searching for positions of manager by keyword");
		UserEntity manager = userEJB.find(idU);
		if ( (manager != null) && (manager.getRoles().contains("MANAGER")) ) {
			String pattern = SearchPattern.preparePattern(keyword);
			log.debug("Internal search string: "+pattern);
			this.plist = positionEJB.findPositionsByKeywordAndManager(pattern, manager);
		} else log.error("No manager with id "+idU);
	}

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

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	
	public void addLocation() {
		if (this.locations == null) this.locations = new ArrayList<String>();
		this.locations.add(location);
	}

	public void addLocationCoimbra() {
		if (this.locations == null) this.locations = new ArrayList<String>();
		this.locations.add("COIMBRA");
	}

	public void addLocationPorto() {
		if (this.locations == null) this.locations = new ArrayList<String>();
		this.locations.add("PORTO");
	}

	public void addLocationLisboa() {
		if (this.locations == null) this.locations = new ArrayList<String>();
		this.locations.add("LISBOA");
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}	

}
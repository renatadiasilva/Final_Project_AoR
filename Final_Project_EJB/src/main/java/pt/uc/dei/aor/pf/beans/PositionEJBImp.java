package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.dao.PositionDao;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class PositionEJBImp implements PositionEJBInterface {

	private static final Logger log = 
			LoggerFactory.getLogger(PositionEJBImp.class);
	
	@EJB
	private PositionDao positionDAO;
	
	@Override
	public void save(PositionEntity position) {
		log.info("Saving position in DB");
		isPositionComplete(position);
		positionDAO.save(position);
	}

	@Override
	public void update(PositionEntity position) {
		log.info("Updating position of DB");
		isPositionComplete(position);
		positionDAO.update(position);
	}

	@Override
	public void delete(PositionEntity position) {
		log.info("Deleting position from DB");
		positionDAO.delete(position);
	}

	@Override
	public PositionEntity find(Long id) {
		log.info("Finding position by ID");
		return positionDAO.find(id);
	}

	@Override
	public List<PositionEntity> findAll() {
		log.info("Creating Query for all positions");
		return positionDAO.findAll();
	}

	@Override
	public List<PositionEntity> findPositionsByCode(String code) {
		log.info("Finding positions by code");
		return positionDAO.findPositions(code, "%", "%", "%", "%", "%", null);
	}

	@Override
	public List<PositionEntity> findPositionsByDate(Date openingDate1,
			Date openingDate2) {
		log.info("Finding all positions opened between two dates");
		return positionDAO.findPositionsByDate(openingDate1, openingDate2, "%",
				"%", "%", "%", "%", "%", null);
	}

	@Override
	public List<PositionEntity> findPositionsByTitle(String title) {
		log.info("Finding positions by title");
		return positionDAO.findPositions("%", title, "%",
				"%", "%", "%", null);
	}

	@Override
	public List<PositionEntity> findPositionsByLocationsOne(
			List<String> locations) {
		log.info("Finding positions by locations");
		return positionDAO.findPositionsByLocationsOne(locations);
	}

	@Override
	public List<PositionEntity> findPositionsByLocationsAll(
			List<String> locations) {
		log.info("Finding positions by locations");
		return positionDAO.findPositionsByLocationsAll(locations);
	}

	@Override
	public List<PositionEntity> findPositionsByStatus(String currentStatus) {
		log.info("Finding positions by status");
		return positionDAO.findPositions("%", "%", "%",
				currentStatus, "%", "%", null);
	}

	@Override
	public List<PositionEntity> findPositionsByCompany(String company) {
		log.info("Finding positions by company");
		return positionDAO.findPositions("%", "%", "%",
				"%", company, "%", null);
	}

	@Override
	public List<PositionEntity> findPositionsByTechArea(String tecnhicalArea) {
		log.info("Finding positions by technical area");
		return positionDAO.findPositions("%", "%", "%",	"%", "%", 
				tecnhicalArea, null);
	}

	@Override
	public List<PositionEntity> findPositions(Date openingDate1,
			Date openingDate2, String positionCode, String title,
			String location, String currentStatus, String company,
			String technicalArea) {
		log.info("Finding positions by several attributes");
		return positionDAO.findPositionsByDate(openingDate1, openingDate2, 
				positionCode, title, location, currentStatus, company, 
				technicalArea, null);
	}

	@Override
	public List<PositionEntity> findPositionsByManager(Date openingDate1,
			Date openingDate2, String positionCode, String title,
			String location, String currentStatus, String company,
			String technicalArea, UserEntity positionManager) {
		log.info("Finding positions of given manager by several attributes");
		return positionDAO.findPositionsByDate(openingDate1, openingDate2, 
				positionCode, title, location, currentStatus, company, 
				technicalArea, positionManager);
	}

	@Override
	public List<PositionEntity> findOpenPositions() {
		log.info("Finding all open positions");
		return positionDAO.findPositions("%", "%", "%",
				"OPEN", "%", "%", null);
	}

	@Override
	public List<PositionEntity> findPositionsByCandidate(
			UserEntity candidate) {
		log.info("Finding positions associated to a given candidate");
		return positionDAO.findPositionsByCandidate(candidate);		
	}

	@Override
	public boolean alreadyCandidateOfPosition(
			UserEntity candidate, PositionEntity position) {
		log.info("Find if a positions is already associated to a "
				+ "given candidate");
		List<PositionEntity> pos = positionDAO.findByPositionAndCandidate(
				candidate, position);
		if (pos == null) return false; // no previous submission exists
		if (pos.size() == 1) return true; // candidate/position association
		return false; // no previous submission exists		
	}

	@Override
	public List<PositionEntity> findCloseToSLAPositions(int daysBefore) {
		log.info("Finding all close to SLA positions");
		return positionDAO.findCloseToSLAPositions(daysBefore);
	}

	@Override
	public List<PositionEntity> findPositionsByKeyword(String keyword) {
		log.info("Finding positions by keyword");
		return positionDAO.findPositionsByKeyword(keyword, null);
	}

	@Override
	public List<PositionEntity> findPositionsByKeywordAndManager(String keyword,
			UserEntity positionManager) {
		log.info("Finding positions of given manager by keyword");
		return positionDAO.findPositionsByKeyword(keyword, positionManager);
	}

	private void isPositionComplete(PositionEntity position) {
		boolean hasError = false;
		
		if (position == null) hasError = true;
		else if (position.getOpeningDate() == null) hasError = true;
		else if (position.getPositionCode() == null) hasError = true;
		else if (position.getStatus() == null) hasError = true;
		else if (position.getOpenings() <= 0) hasError = true;
		else if (position.getPositionManager() == null) hasError = true;
		else if (position.getPositionCreator() == null) hasError = true;
		else if (position.getCompany() == null) hasError = true;
		else if (position.getTechnicalArea() == null) hasError = true;
		else if (position.getCompany() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The position is missing data. "
					+ "Check the notnull attributes.");
	}

}

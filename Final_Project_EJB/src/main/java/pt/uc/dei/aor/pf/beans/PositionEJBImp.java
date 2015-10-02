package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.dao.PositionDao;
import pt.uc.dei.aor.pf.dao.SubmissionDao;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
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
	
	@EJB
	private SubmissionDao submissionDAO;
	
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
		positionDAO.delete(position.getId(), PositionEntity.class);
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
	public List<PositionEntity> findPositionsByCode(String code,
			UserEntity manager) {
		log.info("Finding positions by code");
		return positionDAO.findPositions(code, "%", "%", "%", "%", "%", manager);
	}

	@Override
	public List<PositionEntity> findPositionsByDate(Date openingDate1,
			Date openingDate2, UserEntity manager) {
		log.info("Finding all positions opened between two dates");
		return positionDAO.findPositionsByDate(openingDate1, openingDate2, "%",
				"%", "%", "%", "%", "%", manager);
	}

	@Override
	public List<PositionEntity> findPositionsByTitle(String title,
			UserEntity manager) {
		log.info("Finding positions by title");
		return positionDAO.findPositions("%", title, "%",
				"%", "%", "%", manager);
	}

	@Override
	public List<PositionEntity> findPositionsByLocationsOne(
			List<String> locations, UserEntity manager) {
		log.info("Finding positions by locations");
		return positionDAO.findPositionsByLocationsOne(locations,
				manager);
	}

	@Override
	public List<PositionEntity> findPositionsByLocationsAll(
			List<String> locations, UserEntity manager) {
		log.info("Finding positions by locations");
		return positionDAO.findPositionsByLocationsAll(locations,
				manager);
	}

	@Override
	public List<PositionEntity> findPositionsByStatus(String currentStatus,
			UserEntity manager) {
		log.info("Finding positions by status");
		return positionDAO.findPositions("%", "%", "%",
				currentStatus, "%", "%", manager);
	}

	@Override
	public List<PositionEntity> findPositionsByCompany(String company,
			UserEntity manager) {
		log.info("Finding positions by company");
		return positionDAO.findPositions("%", "%", "%",
				"%", company, "%", manager);
	}

	@Override
	public List<PositionEntity> findPositionsByTechArea(String tecnhicalArea,
			UserEntity manager) {
		log.info("Finding positions by technical area");
		return positionDAO.findPositions("%", "%", "%",	"%", "%", 
				tecnhicalArea, manager);
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
				Constants.STATUS_OPEN.toUpperCase(), "%", "%", null);
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
	public List<PositionEntity> findClosedPositions() {
		log.info("Finding all closed positions");
		return positionDAO.findClosedPositions();
	}

	@Override
	public List<PositionEntity> findCloseToSLAPositions(int daysBefore) {
		log.info("Finding all close to SLA positions");
		return positionDAO.findCloseToSLAPositions(daysBefore);
	}
	
	@Override
	public List<PositionEntity> findAfterSLAPositions() {
		log.info("Finding all positions which passed by the SLA date");
		return positionDAO.findAfterSLAPositions();
	}

	@Override
	public List<PositionEntity> findPositionsByKeyword(String keyword) {
		log.info("Finding positions by keyword");
		return positionDAO.findPositionsByKeyword(keyword, null);
	}

	@Override
	public List<PositionEntity> findOpenPositionsByKeyword(String keyword,
			UserEntity manager) {
		log.info("Finding open positions by keyword");
		return positionDAO.findOpenPositionsByKeyword(keyword, manager);
	}

	@Override
	public List<PositionEntity> findPositionsByKeywordShort(String keyword,
			String status) {
		log.info("Finding positions by keyword short");
		return positionDAO.findPositionsByKeywordShort(keyword, null, status);
	}

	@Override
	public List<PositionEntity> findPositionsByKeywordAndManager(String keyword,
			UserEntity positionManager) {
		log.info("Finding positions of given manager by keyword");
		return positionDAO.findPositionsByKeyword(keyword, positionManager);
	}

	@Override
	public List<PositionEntity> findPositionsByScript(
			ScriptEntity script) {
		log.info("Finding open positions using a script as default");
		return positionDAO.findPositionsByScript(script);
	}

	@Override
	public List<PositionEntity> findPositionsManagedByUser(UserEntity manager) {
		log.info("Finding positions managed by a user");
		return positionDAO.findPositionsManagedByUser(manager);
	}

	@Override
	public List<PositionEntity> findOpenPositionsManagedByUser(
			UserEntity manager) {
		log.info("Finding open positions managed by a user");
		return positionDAO.findOpenPositionsManagedByUser(manager);
	}
	
	@Override
	public List<Object[]> countSubmissionsByPosition(Date date1,
			Date date2) {
		log.info("Counting submissions by position between two dates");
		return positionDAO.countSubmissionsByPosition(date1, date2);
	}

	@Override
	public List<Object[]> countRejectedByPosition(Date date1,
			Date date2) {
		log.info("Counting rejected submissions by position");
		return positionDAO.countRejectedByPosition(date1, date2);
	}

	@Override
	public List<Object[]> countProposalsByPosition(Date date1,
			Date date2) {
		log.info("Counting presented proposals by position");
		return positionDAO.countProposalsByPosition(date1, date2);
	}

	@Override
	public List<Object[]> averageTimeToClose(Date date1, Date date2,
			char period) {
		log.info("Computing the average time to Close a position"
				+ "created between two dates (by period)");
		return positionDAO.averageTimeToClose(date1, date2, period);
	}
	
	@Override
	public Double overallAverageTimeToClose(Date date1, Date date2) {
		log.info("Computing the overall average time to close a position"
				+ "created between two dates");
		return positionDAO.overallAverageTimeToClose(date1, date2);
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

	@Override
	public List<PositionEntity> findAllOrderByCode() {
		return this.positionDAO.findAllOrderByCode();
	}

}
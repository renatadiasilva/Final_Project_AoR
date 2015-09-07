package pt.uc.dei.aor.pf.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class PositionDao extends GenericDao<PositionEntity> {

	public PositionDao() {
		super(PositionEntity.class);
	}

	public List<PositionEntity> findPositionsByDate(Date date1,
			Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);  //formato!!
		return super.findSomeResults("Position.positionsByDate", parameters);
	}

	public List<PositionEntity> findPositionsByCode(String code) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("c", code);
		return super.findSomeResults("Position.positionsByCode", parameters);
	}
		
	public List<PositionEntity> findPositionsByTitle(String title) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("t", title);
		return super.findSomeResults("Position.positionsByTitle", parameters);
	}

	public List<PositionEntity> findPositionsByLocation(String location) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("loc", location);
		return super.findSomeResults("Position.positionsByLocation", parameters);
	}

	public List<PositionEntity> findPositionsByStatus(String status) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("s", status);
		return super.findSomeResults("Position.positionsByStatus", parameters);
	}

	public List<PositionEntity> findPositionsByCompany(String company) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("c", company);
		return super.findSomeResults("Position.positionsByCompany", parameters);
	}

	public List<PositionEntity> findPositionsByTechArea(String technicalArea) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ta", technicalArea);
		return super.findSomeResults("Position.positionsByTechArea", parameters);
	}

	public List<PositionEntity> findPositions(Date openingDate1, Date openingDate2, String positionCode,
			String title, String location, String currentStatus, String company, String technicalArea, 
			UserEntity positionManager) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", openingDate1);
		parameters.put("date2", openingDate2);
		parameters.put("c", positionCode);
		parameters.put("t", title);
		parameters.put("loc", location);
		parameters.put("s", currentStatus);
		parameters.put("comp", company);
		parameters.put("ta", technicalArea);
		if (positionManager != null) {
			parameters.put("id", positionManager);
			return super.findSomeResults("Position.positionsBySeveralAttributesByManager", parameters);
		} else return super.findSomeResults("Position.positionsBySeveralAttributes", parameters);
	}

	public List<PositionEntity> findCloseToSLAPositions(int daysBefore) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance(); // today
		cal.add(Calendar.DAY_OF_YEAR, daysBefore); // today plus the given days before SLA
		parameters.put("date", cal.getTime());
		return super.findSomeResults("Position.closeToSLAPositions", parameters);
	}

	public List<PositionEntity> findPositionsByKeyword(String keyword,
			UserEntity positionManager) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("keyword", keyword);
		if (positionManager != null) {
			parameters.put("id", positionManager);
			return super.findSomeResults("Position.positionsByKeywordByManager", parameters);
		} else return super.findSomeResults("Position.positionsByKeyword", parameters);		
	}

}
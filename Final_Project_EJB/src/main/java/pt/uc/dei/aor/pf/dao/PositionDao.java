package pt.uc.dei.aor.pf.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

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
		return super.findSomeResults("Position.findPositionsByDate", parameters);
	}

	public List<PositionEntity> findPositionsByCode(String code) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("c", code);
		return super.findSomeResults("Position.findPositionsByCode", parameters);
	}

	public List<PositionEntity> findPositionsByTitle(String title) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("t", title);
		return super.findSomeResults("Position.findPositionsByTitle", parameters);
	}

	//	public List<PositionEntity> findPositionsByLocation(String location) {
	//		Map<String, Object> parameters = new HashMap<String, Object>();
	//		parameters.put("loc", location);
	//		return super.findSomeResults("Position.findPositionsByLocation", parameters);
	//	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByLocations(List<String> locations, String AND_OR) {
		String queryS = "SELECT * FROM positions, locations WHERE "
				+ "positions.id = locations.position_id AND (locations.location = "+locations.get(0);
		for (int i = 1; i < locations.size(); i++) 
			queryS += AND_OR+"locations.location = "+locations.get(i);
		queryS += ") ORDER BY positions.code";
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("loc", locations);
//		return super.findSomeResults("Position.findPositionsByLocation", parameters);
	}

	public List<PositionEntity> findPositionsByStatus(String status) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("s", status);
		return super.findSomeResults("Position.findPositionsByStatus", parameters);
	}

	public List<PositionEntity> findPositionsByCompany(String company) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("c", company);
		return super.findSomeResults("Position.findPositionsByCompany", parameters);
	}

	public List<PositionEntity> findPositionsByTechArea(String technicalArea) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ta", technicalArea);
		return super.findSomeResults("Position.findPositionsByTechArea", parameters);
	}

	public List<PositionEntity> findPositionsByCandidate(UserEntity candidate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", candidate);
		return super.findSomeResults("Position.findPositionsByCandidate", parameters);
	}

	public List<PositionEntity> findByPositionAndCandidate(UserEntity candidate, 
			PositionEntity position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", candidate);
		parameters.put("idP", position);
		return super.findSomeResults("Position.findByPositionAndCandidate", parameters);
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
			return super.findSomeResults("Position.findPositionsBySeveralAttributesByManager", parameters);
		} else return super.findSomeResults("Position.findPositionsBySeveralAttributes", parameters);
	}

	public List<PositionEntity> findCloseToSLAPositions(int daysBefore) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance(); // today
		cal.add(Calendar.DAY_OF_YEAR, daysBefore); // today plus the given days before SLA
		parameters.put("date", cal.getTime());
		return super.findSomeResults("Position.findCloseToSLAPositions", parameters);
	}

	public List<PositionEntity> findPositionsByKeyword(String keyword,
			UserEntity positionManager) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("keyword", keyword);
		if (positionManager != null) {
			parameters.put("id", positionManager);
			return super.findSomeResults("Position.findPositionsByKeywordByManager", parameters);
		} else return super.findSomeResults("Position.findPositionsByKeyword", parameters);		
	}

}
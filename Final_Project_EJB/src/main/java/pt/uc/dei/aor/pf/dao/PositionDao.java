package pt.uc.dei.aor.pf.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class PositionDao extends GenericDao<PositionEntity> {

	public PositionDao() {
		super(PositionEntity.class);
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByLocationsOne(
			List<String> locations) {
		
		int sizel = locations.size(); 
		String[] values = new String[sizel];
		String[] attributes = new String[sizel];
		int i = 0;
		for (String l : locations) {
			attributes[i] = "locations.location";
			values[i] = l;
			i++;
		}
		String queryS = makeQuery("DISTINCT positions.*", 
			"positions, locations", "(", attributes, values, " OR ", 
			"positions.id = locations.position_id", "code");
	
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByLocationsAll(
			List<String> locations) {
		
		int sizel = locations.size(); 
		String queryS = "SELECT * FROM"
			+ " (SELECT positions.*, count(positions.*)"
			+ " FROM positions, locations"
			+ " WHERE positions.id = locations.position_id AND"
			+ " (locations.location LIKE \'"+locations.get(0)+"\'";
		for (int i = 1; i < sizel; i++) 
			queryS += " OR locations.location LIKE \'"+locations.get(i)+"\'";
		queryS += ") GROUP BY id) AS c WHERE c.count = "+sizel+
			" ORDER BY c.code";
		
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();
	
	}

	public List<PositionEntity> findPositionsByCandidate(
			UserEntity candidate) {		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", candidate);
		return super.findSomeResults("Position.findPositionsByCandidate", 
				parameters);
	}

	public List<PositionEntity> findByPositionAndCandidate(
			UserEntity candidate, PositionEntity position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", candidate);
		parameters.put("position", position);
		return super.findSomeResults("Position.findByPositionAndCandidate",
				parameters);
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositions(String positionCode,
			String title, String location, String currentStatus, 
			String company, String technicalArea, 
			UserEntity positionManager) {

		String[] attributes = {"code", "title", "location", "status",
			"company", "technical_area"};		
		String[] values = {positionCode, title, location, currentStatus,
			company, technicalArea};

		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = "
				+positionManager.getId();
		String queryS = makeQuery("DISTINCT positions.*",
				"positions, locations", "(", attributes, values, " AND ", 
				"positions.id = locations.position_id"+extra, "code");

		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByDate(Date openingDate1, 
			Date openingDate2, String positionCode,
			String title, String location, String currentStatus,
			String company, String technicalArea, 
			UserEntity positionManager) {
		
		String[] attributes = {"code", "title", "location", "status", 
			"company", "technical_area"};		
		String[] values = {positionCode, title, location, currentStatus,
			company, technicalArea};

		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = "
				+positionManager.getId();
		String queryS = makeQuery("DISTINCT positions.*",
				"positions, locations", "(", attributes, values, " AND ", 
				"positions.id = locations.position_id"
				+ " AND positions.opening_date BETWEEN :date1 AND :date2"
				+extra, "opening_date");
		
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		query.setParameter("date1", openingDate1);
		query.setParameter("date2", openingDate2);
		return (List<PositionEntity>) query.getResultList();	
		
	}

	public List<PositionEntity> findCloseToSLAPositions(int daysBefore) {

		// today
		Calendar cal = Calendar.getInstance();
		 // today plus the given days before SLA
		cal.add(Calendar.DAY_OF_YEAR, daysBefore);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date", cal.getTime());
		parameters.put("status", PositionEntity.STATUS_OPEN);
		return super.findSomeResults("Position.findCloseToSLAPositions", 
				parameters);
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByKeyword(String keyword,
			UserEntity positionManager) {

		String[] values = {keyword, keyword, keyword, keyword, keyword, 
			keyword};
		String[] attributes = {"code", "title", "locations.location",
			"company", "technical_area", "description"};

		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = "
				+positionManager.getId();
		String queryS = makeQuery("DISTINCT positions.*",
				"positions, locations", "(", attributes, values, " OR ", 
				"positions.id = locations.position_id"+extra, "code");
		
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();
		
	}

	public List<PositionEntity> findOpenPositionsByScript(
			ScriptEntity script) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("status", PositionEntity.STATUS_OPEN);
		parameters.put("script", script);
		return super.findSomeResults("Position.findOpenPositionsByScript",
				parameters);
	}

	public List<PositionEntity> findNotOpenPositionsByScript(
			ScriptEntity script) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("status", PositionEntity.STATUS_OPEN);
		parameters.put("script", script);
		return super.findSomeResults("Position.findNotOpenPositionByScript",
				parameters);
	}

	public List<PositionEntity> findPositionsManagedByUser(UserEntity manager) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", manager);
		return super.findSomeResults("Position.findPositionsManagedByUser",
				parameters);
	}
	
	public List<PositionEntity> findOpenPositionsManagedByUser(
			UserEntity manager) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", manager);
		parameters.put("status", PositionEntity.STATUS_OPEN);
		return super.findSomeResults("Position.findOpenPositionsManagedByUser",
				parameters);
	}

}
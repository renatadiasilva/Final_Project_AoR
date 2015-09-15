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

//	public List<PositionEntity> findPositionsByDate(Date date1,
//			Date date2) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("date1", date1);
//		parameters.put("date2", date2);
//		return super.findSomeResults("Position.findPositionsByDate", parameters);
//	}
//
//	public List<PositionEntity> findPositionsByCode(String code) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("c", code);
//		return super.findSomeResults("Position.findPositionsByCode", parameters);
//	}
//
//	public List<PositionEntity> findPositionsByTitle(String title) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("t", title);
//		return super.findSomeResults("Position.findPositionsByTitle", parameters);
//	}

	//	public List<PositionEntity> findPositionsByLocation(String location) {
	//		Map<String, Object> parameters = new HashMap<String, Object>();
	//		parameters.put("loc", location);
	//		return super.findSomeResults("Position.findPositionsByLocation", parameters);
	//	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByLocationsOne(List<String> locations) {
		int sizel = locations.size(); 
//		String queryS = "SELECT * FROM positions, locations WHERE "
//				+ "positions.id = locations.position_id AND (locations.location = "+locations.get(0);
//		for (int i = 1; i < locations.size(); i++) 
//			queryS += AND_OR+"locations.location = "+locations.get(i);
//		queryS += ") ORDER BY positions.code";
//		System.out.println(queryS);
//		Query query = em.createNativeQuery(queryS, PositionEntity.class);
//		return (List<PositionEntity>) query.getResultList();
	
		String[] values = new String[sizel];
		String[] attributes = new String[sizel];
		int i = 0;
		for (String l : locations) {
			attributes[i] = "locations.location";
			values[i] = l;
			i++;
		}
		String queryS = makeQuery("DISTINCT positions.*", "positions, locations",
					"(", attributes, values, " OR ", 
					"positions.id = locations.position_id", "code");
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();
		
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("loc", locations);
//		return super.findSomeResults("Position.findPositionsByLocation", parameters);
	}
	
	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByLocationsAll(List<String> locations) {
		int sizel = locations.size(); 
		String queryS = "SELECT * FROM (SELECT positions.*, count(positions.*) FROM"
				+ " positions, locations WHERE positions.id = locations.position_id AND"
				+ " (locations.location LIKE \'"+locations.get(0)+"\'";
		for (int i = 1; i < sizel; i++) 
			queryS += " OR locations.location LIKE \'"+locations.get(i)+"\'";
		queryS += ") GROUP BY id) AS c WHERE c.count = "+sizel+" ORDER BY c.code";
		
//		
//		String queryS = "select * from (select positions.*, count(positions.*) "
//				+ "from locations, positions where positions.id = locations.position_id "
//				+ "and (locations.location = 'COIMBRA' or locations.location = 'PORTO') group by id) as c "
//				+ "where c.count = 2";
	
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();
		
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("loc", locations);
//		return super.findSomeResults("Position.findPositionsByLocation", parameters);
	}

//	public List<PositionEntity> findPositionsByStatus(String status) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("s", status);
//		return super.findSomeResults("Position.findPositionsByStatus", parameters);
//	}
//
//	public List<PositionEntity> findPositionsByCompany(String company) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("c", company);
//		return super.findSomeResults("Position.findPositionsByCompany", parameters);
//	}
//
//	public List<PositionEntity> findPositionsByTechArea(String technicalArea) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("ta", technicalArea);
//		return super.findSomeResults("Position.findPositionsByTechArea", parameters);
//	}
//
	public List<PositionEntity> findPositionsByCandidate(UserEntity candidate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", candidate);
		return super.findSomeResults("Position.findPositionsByCandidate", parameters);
	}

	public List<PositionEntity> findByPositionAndCandidate(UserEntity candidate, 
			PositionEntity position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", candidate);
		parameters.put("position", position);
		return super.findSomeResults("Position.findByPositionAndCandidate", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositions(String positionCode,
			String title, String location, String currentStatus, 
			String company, String technicalArea, 
			UserEntity positionManager) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("c", positionCode);
//		parameters.put("t", title);
//		parameters.put("loc", location);
//		parameters.put("s", currentStatus);
//		parameters.put("comp", company);
//		parameters.put("ta", technicalArea);
		String queryS;
		Query query;
		String[] attributes = {"code", "title", "location", "status", "company", 
			"technical_area"};		
		String[] values = {positionCode, title, location, currentStatus,
			company, technicalArea};

		if (positionManager != null) {
			// DISTINCT??
			queryS = makeQuery("DISTINCT positions.*", "positions, locations",
					"(", attributes, values, " AND ", "positions.id = locations.position_id"
							+ " AND positions.manager = "+positionManager.getId(), "code");
//			parameters.put("id", positionManager);
			
//	query = "SELECT p FROM PositionEntity p WHERE "
//		+ " AND UPPER(p.positionCode) LIKE :c AND UPPER(p.title) LIKE :t"
//		+ " AND :loc MEMBER OF p.locations AND UPPER(p.status) LIKE :s"
//		+ " AND UPPER(p.company) LIKE :comp"
//		+ " AND UPPER(p.technicalArea) LIKE :ta AND p.positionManager = :id"
//		+ " ORDER BY p.positionCode"),

//			return super.findSomeResults("Position.findPositionsBySeveralAttributesByManager", parameters);
		} else {
			queryS = makeQuery("DISTINCT positions.*", "positions, locations",
					"(", attributes, values, " AND ", "positions.id = locations.position_id", "code");

//			return super.findSomeResults("Position.findPositionsBySeveralAttributes", parameters);
//			@NamedQuery(name = "Position.findPositionsBySeveralAttributes",
//			query = "SELECT p FROM PositionEntity p WHERE p.openingDate BETWEEN :date1 AND :date2"
//					+ " AND UPPER(p.positionCode) LIKE :c AND UPPER(p.title) LIKE :t"
//					+ " AND :loc MEMBER OF p.locations AND UPPER(p.status) LIKE :s"
//					+ " AND UPPER(p.company) LIKE :comp"
//					+ " AND UPPER(p.technicalArea) LIKE :ta ORDER BY p.positionCode"),
		}
		System.out.println(queryS);
		query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByDate(Date openingDate1, Date openingDate2, String positionCode,
			String title, String location, String currentStatus, String company, String technicalArea, 
			UserEntity positionManager) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("date1", openingDate1);
//		parameters.put("date2", openingDate2);
//		parameters.put("c", positionCode);
//		parameters.put("t", title);
//		parameters.put("loc", location);
//		parameters.put("s", currentStatus);
//		parameters.put("comp", company);
//		parameters.put("ta", technicalArea);
		String queryS;
		Query query;
		String[] attributes = {"code", "title", "location", "status", "company", 
			"technical_area"};		
		String[] values = {positionCode, title, location, currentStatus,
			company, technicalArea};

		if (positionManager != null) {
			queryS = makeQuery("DISTINCT positions.*", "positions, locations",
					"(", attributes, values, " AND ", "positions.id = locations.position_id"
							+ " AND positions.opening_date BETWEEN :date1 AND :date2 AND"
							+ " positions.manager = "+positionManager.getId(), "opening_date");
			
//			parameters.put("id", positionManager);
//			@NamedQuery(name = "Position.findPositionsBySeveralAttributesByManager",
//			query = "SELECT p FROM PositionEntity p WHERE p.openingDate BETWEEN :date1 AND :date2"
//					+ " AND UPPER(p.positionCode) LIKE :c AND UPPER(p.title) LIKE :t"
//					+ " AND :loc MEMBER OF p.locations AND UPPER(p.status) LIKE :s"
//					+ " AND UPPER(p.company) LIKE :comp"
//					+ " AND UPPER(p.technicalArea) LIKE :ta AND p.positionManager = :id"
//					+ " ORDER BY p.positionCode"),

//			return super.findSomeResults("Position.findPositionsBySeveralAttributesByManager", parameters);
		} else {
			queryS = makeQuery("DISTINCT positions.*", "positions, locations",
					"(", attributes, values, " AND ", "positions.id = locations.position_id"
					+ " AND positions.opening_date BETWEEN :date1 AND :date2", "opening_date");

//		return super.findSomeResults("Position.findPositionsBySeveralAttributes", parameters);
//		@NamedQuery(name = "Position.findPositionsBySeveralAttributes",
//		query = "SELECT p FROM PositionEntity p WHERE p.openingDate BETWEEN :date1 AND :date2"
//				+ " AND UPPER(p.positionCode) LIKE :c AND UPPER(p.title) LIKE :t"
//				+ " AND :loc MEMBER OF p.locations AND UPPER(p.status) LIKE :s"
//				+ " AND UPPER(p.company) LIKE :comp"
//				+ " AND UPPER(p.technicalArea) LIKE :ta ORDER BY p.positionCode"),
		}
		System.out.println(queryS);
		query = em.createNativeQuery(queryS, PositionEntity.class);
		query.setParameter("date1", openingDate1);
		query.setParameter("date2", openingDate2);
		return (List<PositionEntity>) query.getResultList();			
	}

	public List<PositionEntity> findCloseToSLAPositions(int daysBefore) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance(); // today
		cal.add(Calendar.DAY_OF_YEAR, daysBefore); // today plus the given days before SLA
		parameters.put("date", cal.getTime());
		parameters.put("status", PositionEntity.STATUS_OPEN);
		return super.findSomeResults("Position.findCloseToSLAPositions", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByKeyword(String keyword,
			UserEntity positionManager) {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("keyword", keyword);
//		String[] attributes = {"address", "city", "country", "course", "school"};

		String queryS;
		Query query;
		String[] values = {keyword, keyword, keyword, keyword, keyword, keyword};
		String[] attributes = {"code", "title", "locations.location", "company", 
				"technical_area", "description"};
		if (positionManager != null) {
			queryS = makeQuery("DISTINCT positions.*", "positions, locations",
					"(", attributes, values, " OR ", "positions.id = locations.position_id"
					+ " AND positions.manager = "+positionManager.getId(), "code");
//			parameters.put("id", positionManager);
//			@NamedQuery(name = "Position.findPositionsByKeywordByManager",
//			query = "SELECT p FROM PositionEntity p WHERE"
//					+ " (UPPER(p.positionCode) LIKE :keyword OR"
//					+ " UPPER(p.title) LIKE :keyword OR"
//					+ " :keyword MEMBER OF p.locations OR"
//					+ " UPPER(p.company) LIKE :keyword OR"
//					+ " UPPER(p.technicalArea) LIKE :keyword OR"
//					+ " UPPER(p.description) LIKE :keyword) "
//					+ " AND p.positionManager = :id ORDER BY p.positionCode"),
//			return super.findSomeResults("Position.findPositionsByKeywordByManager", parameters);
		} else {
			queryS = makeQuery("DISTINCT positions.*", "positions, locations",
					"(", attributes, values, " OR ", 
					"positions.id = locations.position_id", "code");
//			return super.findSomeResults("Position.findPositionsByKeyword", parameters);		
//		@NamedQuery(name = "Position.findPositionsByKeyword",
//				query = "SELECT p FROM PositionEntity p WHERE"
//						+ " UPPER(p.positionCode) LIKE :keyword OR"
//						+ " UPPER(p.title) LIKE :keyword OR"
//						+ " :keyword MEMBER OF p.locations OR"
//						+ " UPPER(p.company) LIKE :keyword OR"
//						+ " UPPER(p.technicalArea) LIKE :keyword OR"
//						+ " UPPER(p.description) LIKE :keyword ORDER BY p.positionCode"),
	
		}
		System.out.println(queryS);
		query = em.createNativeQuery(queryS, PositionEntity.class);
		return (List<PositionEntity>) query.getResultList();			
	}

}
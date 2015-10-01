package pt.uc.dei.aor.pf.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.constants.Constants;
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
			List<String> locations, UserEntity positionManager) {
		
		int sizel = locations.size();
		if (sizel == 0) return new ArrayList<PositionEntity>();
		
		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = :id";
		
		String queryS = "SELECT DISTINCT positions.*"
				+" FROM positions, locations WHERE "
				+"(TRANSLATE(UPPER(REPLACE(locations.location"
				+",\' \',\'\')), \'ÀÁÂÃÄÅĀĂĄÉÊĒĔĖĘĚÌÍÎÏÌĨĪĬÒÓÔÕÖŌŎŐÙÚÛÜŨŪŬŮÇ\',"
				+"\'AAAAAAAAAEEEEEEEIIIIIIIIOOOOOOOOUUUUUUUUC\'"
				+") LIKE :loc0";
			
		for (int i = 1; i < sizel; i++) 
			queryS += " OR TRANSLATE(UPPER(REPLACE(locations.location"
				+",\' \',\'\')), \'ÀÁÂÃÄÅĀĂĄÉÊĒĔĖĘĚÌÍÎÏÌĨĪĬÒÓÔÕÖŌŎŐÙÚÛÜŨŪŬŮÇ\',"
				+"\'AAAAAAAAAEEEEEEEIIIIIIIIOOOOOOOOUUUUUUUUC\'"
				+") LIKE :loc"+i;
		
		queryS += ") AND positions.id = locations.position_id"+extra
				+" ORDER BY code";
	
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		for (int i = 0; i < sizel; i++)
			query.setParameter("loc"+i, locations.get(i));
		if (positionManager != null)
			query.setParameter("id", positionManager.getId());
		return (List<PositionEntity>) query.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByLocationsAll(
			List<String> locations, UserEntity positionManager) {
		
		int sizel = locations.size(); 
		if (sizel == 0) return new ArrayList<PositionEntity>();

		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = :id";

		String queryS = "SELECT * FROM"
			+ " (SELECT positions.*, count(positions.*)"
			+ " FROM positions, locations"
			+ " WHERE positions.id = locations.position_id"+extra+" AND"
			+ " (locations.location LIKE :loc0";
		for (int i = 1; i < sizel; i++) 
			queryS += " OR locations.location LIKE :loc"+i;
		queryS += ") GROUP BY id) AS c WHERE c.count = :size"
			+" ORDER BY c.code";

		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		for (int i = 0; i < sizel; i++)
			query.setParameter("loc"+i, locations.get(i));
		query.setParameter("size", sizel);
		if (positionManager != null)
			query.setParameter("id", positionManager.getId());
		return (List<PositionEntity>) query.getResultList();
	
	}

	public List<PositionEntity> findPositionsByCandidate(
			UserEntity candidate) {		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", candidate);
		return super.findSomeResults("Position.findPositionsByCandidate", 
				parameters);
	}

	public List<PositionEntity> findClosedPositions() {		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("closed", Constants.STATUS_CLOSED);
		return super.findSomeResults("Position.findClosedPositions", 
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

		String[] attributes = {"code", "title", "status",
			"company", "technical_area"};		

		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = :id";
		String queryS = makeQuery("DISTINCT positions.*",
				"positions, locations", 
				"(TRANSLATE(UPPER(REPLACE(locations.location"
				+",\' \',\'\')), \'ÀÁÂÃÄÅĀĂĄÉÊĒĔĖĘĚÌÍÎÏÌĨĪĬÒÓÔÕÖŌŎŐÙÚÛÜŨŪŬŮÇ\',"
				+"\'AAAAAAAAAEEEEEEEIIIIIIIIOOOOOOOOUUUUUUUUC\'"
				+") LIKE :loc AND ", attributes, " AND ", 
				"positions.id = locations.position_id"+extra, "code");

		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		query.setParameter("loc", location);
		query.setParameter("code", positionCode);
		query.setParameter("title", title);
		query.setParameter("status", currentStatus);
		query.setParameter("company", company);
		query.setParameter("technical_area", technicalArea);
		if (positionManager != null) 
			query.setParameter("id", positionManager.getId());
		return (List<PositionEntity>) query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByDate(Date openingDate1, 
			Date openingDate2, String positionCode,
			String title, String location, String currentStatus,
			String company, String technicalArea, 
			UserEntity positionManager) {
		
		String[] attributes = {"code", "title", "status", 
			"company", "technical_area"};		

		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = :id";
		String queryS = makeQuery("DISTINCT positions.*",
				"positions, locations",
				"(TRANSLATE(UPPER(REPLACE(locations.location"
				+",\' \',\'\')), \'ÀÁÂÃÄÅĀĂĄÉÊĒĔĖĘĚÌÍÎÏÌĨĪĬÒÓÔÕÖŌŎŐÙÚÛÜŨŪŬŮÇ\',"
				+"\'AAAAAAAAAEEEEEEEIIIIIIIIOOOOOOOOUUUUUUUUC\'"
				+") LIKE :loc AND ", attributes, " AND ", 
				"positions.id = locations.position_id"
				+ " AND positions.opening_date BETWEEN :date1 AND :date2"
				+extra, "opening_date");
		
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		query.setParameter("loc", location);
		query.setParameter("code", positionCode);
		query.setParameter("title", title);
		query.setParameter("status", currentStatus);
		query.setParameter("company", company);
		query.setParameter("technical_area", technicalArea);
		query.setParameter("date1", openingDate1);
		query.setParameter("date2", openingDate2);
		if (positionManager != null) 
			query.setParameter("id", positionManager.getId());
		return (List<PositionEntity>) query.getResultList();	
		
	}

	public List<PositionEntity> findCloseToSLAPositions(int daysBefore) {

		// today
		Calendar cal = Calendar.getInstance();
		 // today plus the given days before SLA
		cal.add(Calendar.DAY_OF_YEAR, daysBefore);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date", cal.getTime());
		parameters.put("open", Constants.STATUS_OPEN);
		return super.findSomeResults("Position.findCloseToSLAPositions", 
				parameters);
	}

	public List<PositionEntity> findAfterSLAPositions() {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("status", Constants.STATUS_OPEN);
		return super.findSomeResults("Position.findAfterSLAPositions", 
				parameters);
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByKeyword(String keyword,
			UserEntity positionManager) {

		String[] attributes = {"code", "title",
			"company", "technical_area", "description"};

		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = :id";
		
		String queryS = makeQuery("DISTINCT positions.*",
				"positions, locations", 
				"(TRANSLATE(UPPER(REPLACE(locations.location"
				+",\' \',\'\')), \'ÀÁÂÃÄÅĀĂĄÉÊĒĔĖĘĚÌÍÎÏÌĨĪĬÒÓÔÕÖŌŎŐÙÚÛÜŨŪŬŮÇ\',"
				+"\'AAAAAAAAAEEEEEEEIIIIIIIIOOOOOOOOUUUUUUUUC\'"
				+") LIKE :loc OR ", attributes, " OR ", 
				"positions.id = locations.position_id"+extra, "code");
		System.out.println("Query");
		System.out.println(queryS);
		if (positionManager != null)
			System.out.println(keyword+" "+positionManager.getId());
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		query.setParameter("code", keyword);
		query.setParameter("title", keyword);
		query.setParameter("company", keyword);
		query.setParameter("loc", keyword);
		query.setParameter("technical_area", keyword);
		query.setParameter("description", keyword);
		if (positionManager != null) 
			query.setParameter("id", positionManager.getId());
		return (List<PositionEntity>) query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<PositionEntity> findPositionsByKeywordShort(String keyword,
			UserEntity positionManager, String status) {

		String[] attributes = {"code", "title",
			"company", "technical_area"};

		String extra = "";
		if (positionManager != null) extra = " AND positions.manager = :id";
		
		String queryS = makeQuery("*",
				"positions", 
				"(", attributes, " OR ",
				"positions.status LIKE :status"+extra, "code");
		Query query = em.createNativeQuery(queryS, PositionEntity.class);
		query.setParameter("code", keyword);
		query.setParameter("title", keyword);
		query.setParameter("company", keyword);
		query.setParameter("technical_area", keyword);
		query.setParameter("status", "%"+status+"%");
		if (positionManager != null) 
			query.setParameter("id", positionManager.getId());
		return (List<PositionEntity>) query.getResultList();
		
	}

	public List<PositionEntity> findPositionsByScript(
			ScriptEntity script) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("script", script);
		return super.findSomeResults("Position.findPositionsByScript",
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
		parameters.put("status", Constants.STATUS_OPEN);
		return super.findSomeResults("Position.findOpenPositionsManagedByUser",
				parameters);
	}

	public List<Object[]> countSubmissionsByPosition(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResultsList("Position.countSubmissionsByPosition",
				parameters);	
	}

	public List<Object[]> countRejectedByPosition(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		parameters.put("rejected", Constants.STATUS_REJECTED);
		return super.findSomeResultsList("Position.countRejectedByPosition",
				parameters);	
	}

	public List<Object[]> countProposalsByPosition(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResultsList("Position.countProposalsByPosition",
				parameters);	
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> averageTimeToClose(Date date1, Date date2,
			char period) {
		
		String m1 = "", m2 = "";
		if (period == Constants.MONTHLY) {
			m1 = ", DATE_PART(\'MONTH\', opening_date) AS m";
			m2 = ", m";
		}
		String queryS = "SELECT AVG(DATE_PART(\'DAY\', "
				+ " closing_date\\:\\:timestamp -"
				+ " opening_date\\:\\:timestamp)),"
				+ " DATE_PART(\'YEAR\', opening_date) AS y"+m1
				+ " FROM positions "
				+ " WHERE opening_date BETWEEN :date1 AND :date2"
				+ " AND closing_date IS NOT NULL"
				+ " GROUP BY y"+m2+" ORDER BY y"+m2;
		Query query = em.createNativeQuery(queryS);
		query.setParameter("date1", date1);
		query.setParameter("date2", date2);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Double overallAverageTimeToClose(Date date1, Date date2) {
		
		String queryS = "SELECT AVG(DATE_PART(\'DAY\', "
				+ " closing_date\\:\\:timestamp -"
				+ " opening_date\\:\\:timestamp))"
				+ " FROM positions "
				+ " WHERE opening_date BETWEEN :date1 AND :date2"
				+ " AND closing_date IS NOT NULL";
		Query query = em.createNativeQuery(queryS);
		query.setParameter("date1", date1);
		query.setParameter("date2", date2);
		List<Double> result = (List<Double>) query.getResultList();
		if (result == null || result.isEmpty() || result.get(0) == null)
			return -1.0;
		return (Double) result.get(0);
	}

	public List<PositionEntity> findAllOrderByCode() {
		return super.findSomeResults("Position.findAllOrderByCode", null);
	}

}
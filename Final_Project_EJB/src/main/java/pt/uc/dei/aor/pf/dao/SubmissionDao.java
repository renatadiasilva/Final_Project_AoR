package pt.uc.dei.aor.pf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class SubmissionDao extends GenericDao<SubmissionEntity> {
	
	public SubmissionDao() {
		super(SubmissionEntity.class);
	}
	
	public List<SubmissionEntity> findSpontaneousSubmissions() {
		return super.findSomeResults("Submission.findSpontaneousSubmissions",
				null);
	}
	
	public List<SubmissionEntity> findSubmissionsByDate(Date date1,
			Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Submission.findSubmissionsByDate",
				parameters);
	}
	
	public List<SubmissionEntity> findSpontaneousSubmissionsByDate(
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults(
				"Submission.findSpontaneousSubmissionsByDate", parameters);
	}
	
	public List<SubmissionEntity> findRejectSubmissionsByDate(
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		parameters.put("status", Constants.STATUS_REJECTED);
		return super.findSomeResults("Submission.findRejectedSubmissionsByDate",
			parameters);
	}
	
	public List<SubmissionEntity> findPresentedProposalByDate(
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Submission.findPresentedProposalsByDate",
				parameters);
	}

	public List<SubmissionEntity> findSubmissionsBySource(String source,
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("source", source);
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Submission.findSubmissionsBySource",
				parameters);
	}
	
	public List<SubmissionEntity> findSubmissionsOfPosition(
			PositionEntity position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("position", position);
		return super.findSomeResults("Submission.findSubmissionsOfPosition",
				parameters);	
	}
	
	public List<SubmissionEntity> findSubmissionsOfPositionByDate(
			PositionEntity position, Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("position", position);
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults(
				"Submission.findSubmissionsOfPositionByDate", parameters);	
	}
	
	public List<SubmissionEntity> findSubmissionsOfCandidate(
			UserEntity candidate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", candidate);
		return super.findSomeResults("Submission.findSubmissionsOfCandidate",
				parameters);	
	}

	public Long countTotalSubmissionsPos(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findCount("Submission.countTotalSubmissionsPos",
				parameters);	
	}

	public Long countTotalSubmissions(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findCount("Submission.countTotalSubmissions",
				parameters);	
	}

	public Long countTotalSpontaneous(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findCount("Submission.countTotalSpontaneous",
				parameters);	
	}

	public Long countTotalRejected(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		parameters.put("rejected", Constants.STATUS_REJECTED);
		return super.findCount("Submission.countTotalRejected",
				parameters);	
	}
	
	public Long countTotalProposals(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findCount("Submission.countTotalProposals",
				parameters);	
	}
	
	public Long countTotalHired(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		parameters.put("hired", Constants.STATUS_HIRED);
		return super.findCount("Submission.countTotalHired",
				parameters);	
	}
	
	public Long countTotalRejectedPos(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		parameters.put("rejected", Constants.STATUS_REJECTED);
		return super.findCount("Submission.countTotalRejectedPos",
				parameters);	
	}
	
	public Long countTotalProposalsPos(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return (Long) super.findCount("Submission.countTotalProposalsPos",
				parameters);	
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> countSubmissionsByDate(Date date1, Date date2,
			char period, String result, String restriction) {
		
		String m1 = "", m2 = "";
		switch (period) {
		case Constants.DAILY: 
			m1 = "date";
			m2 = "date";
			break;
		case Constants.MONTHLY: 
			m1 = "DATE_PART(\'year\', date) AS y,"
					+ " DATE_PART(\'month\', date) AS m";
			m2 = "y, m";
			break;
		case Constants.YEARLY:
			m1 = "DATE_PART(\'year\', date) AS y";
			m2 = "y";
			break;
		default: return null; // error
		}
		
		String queryS = "SELECT COUNT(*) AS c, "+m1+result
				+ " FROM submissions "
				+ " WHERE date BETWEEN :date1 AND :date2"
				+ restriction
				+ " GROUP BY "+m2+result+" ORDER BY "+m2+", c DESC"+result;
		
		Query query = em.createNativeQuery(queryS);
		query.setParameter("date1", date1);
		query.setParameter("date2", date2);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> averageTimeToHired(Date date1, Date date2,
			char period) {
		
		String m1 = "", m2 = "";
		if (period == Constants.MONTHLY) {
			m1 = ", DATE_PART(\'MONTH\', date) AS m";
			m2 = ", m";
		}
		String queryS = "SELECT AVG(DATE_PART(\'DAY\', "
				+ " hired_date\\:\\:timestamp - date\\:\\:timestamp)),"
				+ " DATE_PART(\'YEAR\', date) AS y"+m1
				+ " FROM submissions "
				+ " WHERE date BETWEEN :date1 AND :date2"
				+ " AND hired_date IS NOT NULL"
				+ " GROUP BY y"+m2+" ORDER BY y"+m2;
		Query query = em.createNativeQuery(queryS);
		query.setParameter("date1", date1);
		query.setParameter("date2", date2);
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public Double overallAverageTimeToHired(Date date1, Date date2) {
		
		String queryS = "SELECT AVG(DATE_PART(\'DAY\', "
				+ " hired_date\\:\\:timestamp - date\\:\\:timestamp))"
				+ " FROM submissions "
				+ " WHERE date BETWEEN :date1 AND :date2"
				+ " AND hired_date IS NOT NULL";

		Query query = em.createNativeQuery(queryS);
		query.setParameter("date1", date1);
		query.setParameter("date2", date2);
		List<Double> result = (List<Double>) query.getResultList();
		if (result == null || result.isEmpty() || result.get(0) == null)
			return -1.0;
		return (Double) result.get(0);
		
	}

}
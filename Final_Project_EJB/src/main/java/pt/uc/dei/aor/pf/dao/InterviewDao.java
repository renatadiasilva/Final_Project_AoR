package pt.uc.dei.aor.pf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class InterviewDao extends GenericDao<InterviewEntity> {

	public InterviewDao() {
		super(InterviewEntity.class);
	}

	public List<InterviewEntity> findCarriedOutInterviews(Date date1,
			Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Interview.findCarriedOutInterviews",
				parameters);
	}

	public List<InterviewEntity> findCarriedOutInterviewsByUser(
			UserEntity interviewer) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", interviewer);
		return super.findSomeResults("Interview.findCarriedOutInterviewsByUser",
				parameters);
	}

	public List<InterviewEntity> findScheduledInterviewsByUser(
			UserEntity interviewer) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", interviewer);
		return super.findSomeResults("Interview.findScheduledInterviewsByUser",
				parameters);
	}

	public List<InterviewEntity> findScheduledInterviewsByCandidate(
			UserEntity candidate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("candidate", candidate);
		return super.findSomeResults(
				"Interview.findScheduledInterviewsByCandidate", parameters);
	}

	public List<InterviewEntity> findByDateAndInterviewer(Date date,
			UserEntity interviewer) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date", date);
		parameters.put("user", interviewer);
		return super.findSomeResults("Interview.findByDateAndInterviewer",
				parameters);
	}

	public List<InterviewEntity> findByDateAndCandidate(Date date,
			UserEntity candidate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date", date);
		parameters.put("candidate", candidate);
		return super.findSomeResults("Interview.findByDateAndCandidate",
				parameters);
	}

	public List<InterviewEntity> findInterviewByPosition(
			PositionEntity position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("position", position);
		return super.findSomeResults("Interview.findInterviewByPosition",
				parameters);
	}

	public List<InterviewEntity> findCarriedOutInterviewByPosition(
			PositionEntity position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("position", position);
		return super.findSomeResults(
				"Interview.findCarriedOutInterviewByPosition", parameters);
	}

	public List<InterviewEntity> findScheduledInterviewByPosition(
			PositionEntity position) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("position", position);
		return super.findSomeResults(
				"Interview.findScheduledInterviewByPosition", parameters);
	}

	public List<InterviewEntity> findInterviewsOfUser(UserEntity interviewer) {
		Map<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("user", interviewer);
		return super.findSomeResults("Interview.findInterviewsOfUser",
				parameters);
	}

	public List<InterviewEntity> findInterviewsOfSubmission(
			SubmissionEntity submission) {
		Map<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("submission", submission);
		return super.findSomeResults("Interview.findInterviewsOfSubmission",
				parameters);
	}

	public List<InterviewEntity> findCarriedOutInterviewsWithScript(
			ScriptEntity script) {
		Map<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("script", script);
		return super.findSomeResults(
				"Interview.findCarriedOutInterviewsWithScript", parameters);
	}

	public List<InterviewEntity> findScheduledInterviewsWithScript(
			ScriptEntity script) {
		Map<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("script", script);
		return super.findSomeResults(
				"Interview.findScheduledInterviewsWithScript", parameters);
	}
	

	public Long findTotalCarriedOutInterviews(Date date1,
			Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();		
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findCount("Interview.findTotalCarriedOutInterviews",
				parameters);
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> averageTimeToFirstInterview(Date date1, Date date2,
			char period) {
		
		String m1 = "", m2 = "";
		if (period == Constants.MONTHLY) {
			m1 = ", DATE_PART(\'MONTH\', s.date) AS m";
			m2 = ", m";
		}
		String queryS = "SELECT AVG(DATE_PART(\'DAY\', "
				+ " i.date\\:\\:timestamp - s.date\\:\\:timestamp)),"
				+ " DATE_PART(\'YEAR\', s.date) AS y"+m1
				+ " FROM interviews AS i, submissions AS s"
				+ " WHERE i.submission = s.id"
				+ " AND s.date BETWEEN :date1 AND :date2"
				+ " AND i.first = TRUE"
				+ " GROUP BY y"+m2+" ORDER BY y"+m2;
		Query query = em.createNativeQuery(queryS);
		query.setParameter("date1", date1);
		query.setParameter("date2", date2);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Double overallAverageTimeToFirstInterview(Date date1, Date date2) {
		
		String queryS = "SELECT AVG(DATE_PART(\'DAY\', "
				+ " i.date\\:\\:timestamp - s.date\\:\\:timestamp))"
				+ " FROM interviews AS i, submissions AS s"
				+ " WHERE i.submission = s.id"
				+ " AND s.date BETWEEN :date1 AND :date2"
				+ " AND i.first = TRUE";
		System.out.println(queryS);
		Query query = em.createNativeQuery(queryS);
		query.setParameter("date1", date1);
		query.setParameter("date2", date2);
		List<Double> result = (List<Double>) query.getResultList();
		if (result == null || result.isEmpty() || result.get(0) == null)
			return -1.0;
		return (Double) result.get(0);
	}

	public List<InterviewEntity> findCarriedOutInterviewsByCandidate(
			UserEntity candidate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("candidate", candidate);
		return super.findSomeResults(
				"Interview.findCarriedOutInterviewsByCandidate", parameters);
	}

}
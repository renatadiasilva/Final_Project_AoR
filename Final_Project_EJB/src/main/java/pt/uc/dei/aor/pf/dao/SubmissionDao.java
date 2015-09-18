package pt.uc.dei.aor.pf.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
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
		parameters.put("status", Constants.STATUS_PROPOSAL);
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
	
	public List<SubmissionEntity> findSubmissionsOfCandidate(
			UserEntity candidate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", candidate);
		return super.findSomeResults("Submission.findSubmissionsOfCandidate",
				parameters);	
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> countSubmissionsByPosition() {
		
		String queryS = "SELECT position, count(*) FROM submissions "
			+ "WHERE position IS NOT NULL GROUP BY position";

		Query query = em.createNativeQuery(queryS);
		return query.getResultList();

	}

}
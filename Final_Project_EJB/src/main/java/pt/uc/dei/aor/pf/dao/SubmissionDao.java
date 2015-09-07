package pt.uc.dei.aor.pf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uc.dei.aor.pf.entities.SubmissionEntity;

public class SubmissionDao extends GenericDao<SubmissionEntity> {

	public SubmissionDao() {
		super(SubmissionEntity.class);
	}
	
	public List<SubmissionEntity> findSpontaneousSubmissions() {
		return super.findSomeResults("Submission.spontaneousSubmissions", null);
	}	

	public List<SubmissionEntity> findSubmissionsByDate(Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Submission.submissionsByDate", parameters);
	}
	
	public List<SubmissionEntity> findSpontaneousSubmissionsByDate(
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Submission.spontaneousSubmissionsByDate", parameters);
	}
	
	public List<SubmissionEntity> findRejectSubmissionsByDate(
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Submission.rejectedSubmissionsByDate", parameters);
	}
	
	public List<SubmissionEntity> findPresentedProposalByDate(
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Submission.presentedProposalsByDate", parameters);
	}

	public List<SubmissionEntity> findSubmissionsBySource(String source,
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("source", source);
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		return super.findSomeResults("Submission.submissionsBySource", parameters);
	}
	
}
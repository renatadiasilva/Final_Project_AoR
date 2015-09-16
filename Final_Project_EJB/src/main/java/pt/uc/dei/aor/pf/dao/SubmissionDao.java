package pt.uc.dei.aor.pf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class SubmissionDao extends GenericDao<SubmissionEntity> {
	
	public SubmissionDao() {
		super(SubmissionEntity.class);
	}
	
	public void delete(SubmissionEntity submission) {
		
		// the submission has interviews
		List<InterviewEntity> ilist = submission.getInterviews();
		if (ilist != null) {
			// CASCADE APAGA LOGO??
			// ou avisar admin que há entrevistas e ele apaga-as à mão
			// ou dá autorização para se apagar automaticamente
		}
		else delete(submission, SubmissionEntity.class);
		// remove the data not the submission
//		submission.setPosition(null);
//		submission.setSpontaneous(false);
//		submission.setAssociatedBy(null);
//		submission.setMotivationLetter(null);
//		submission.setSources(null);
//		Calendar cal = Calendar.getInstance();
//		cal.set(1900, 1, 1);
//		submission.setStatus(SubmissionEntity.STATUS_REJECTED);;
//		submission.setRejectReason(REMOVED_DATA);
//		submission.setHiredDate(null);
//		update(submission);
	}	
	
	public void addPositionToSpontaneous(SubmissionEntity submission,
			PositionEntity position, UserEntity user) {
		// clone submission
		SubmissionEntity newSubmission = new SubmissionEntity(
				submission.getCandidate(), submission.getMotivationLetter(),
				submission.getSources(), false);
		newSubmission.setPosition(position);
		newSubmission.setAssociatedBy(user);
		newSubmission.setDate(submission.getDate());
		save(newSubmission);
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
		parameters.put("status", SubmissionEntity.STATUS_REJECTED);
		return super.findSomeResults("Submission.findRejectedSubmissionsByDate",
			parameters);
	}
	
	public List<SubmissionEntity> findPresentedProposalByDate(
			Date date1, Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);
		parameters.put("status", SubmissionEntity.STATUS_PROPOSAL);
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
	
}
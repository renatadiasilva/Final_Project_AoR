package pt.uc.dei.aor.pf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

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

}
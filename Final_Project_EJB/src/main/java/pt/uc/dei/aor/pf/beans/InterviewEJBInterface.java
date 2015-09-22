package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface InterviewEJBInterface {
	
	public abstract void save(InterviewEntity interview);
	public abstract void update(InterviewEntity interview);
	public abstract boolean delete(InterviewEntity interview);
	public abstract InterviewEntity find(Long id);
	public abstract List<InterviewEntity> findAll();
	public abstract List<InterviewEntity> findCarriedOutInterviewsByUser(
			UserEntity interviewer);
	public abstract List<InterviewEntity> findScheduledInterviewsByUser(
			UserEntity interviewer);
	public abstract List<InterviewEntity> findCarriedOutInterviews(Date date1, 
			Date date2);
	public abstract List<InterviewEntity> findScheduledInterviewsByCandidate(
			UserEntity candidate);
	public abstract List<InterviewEntity> findInterviewsByPosition(
			PositionEntity position);
	public abstract boolean interviewerHasDateConflict(Date date, 
			UserEntity interviewer);
	public abstract boolean candidateHasDateConflict(Date date, 
			UserEntity candidate);
	public abstract List<InterviewEntity> findCarriedOutInterviewsByPosition(
			PositionEntity position);
	public abstract List<InterviewEntity> findScheduledInterviewsByPosition(
			PositionEntity position);
	public abstract List<InterviewEntity> findInterviewsOfUser(
			UserEntity interviewer);
	public abstract List<InterviewEntity> findInterviewsOfSubmission(
			SubmissionEntity submission);
	public abstract List<InterviewEntity> findCarriedOutInterviewsWithScript(
			ScriptEntity script);
	public abstract List<InterviewEntity> findScheduledInterviewsWithScript(
			ScriptEntity script);
	public abstract List<Object[]> averageTimeToFirstInterview(Date date1,
			Date date2, char period);
	public abstract Long findTotalCarriedOutInterviews(Date date1, Date date2);
	public abstract Double overallAverageTimeToFirstInterview(Date date1,
			Date date2);
}
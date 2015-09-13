package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface InterviewEJBInterface {
	
	public abstract void save(InterviewEntity interview);
	public abstract void update(InterviewEntity interview);
	public abstract void delete(InterviewEntity interview);
	public abstract InterviewEntity find(Long id);
	public abstract List<InterviewEntity> findAll();
	public abstract List<InterviewEntity> findCarriedOutInterviewsByUser(UserEntity interviewer);
	public abstract List<InterviewEntity> findScheduledInterviewsByUser(UserEntity interviewer);
	public abstract List<InterviewEntity> findCarriedOutInterviews(Date date1, Date date2);
	public abstract List<InterviewEntity> findScheduledInterviewsByCandidate(UserEntity candidate);
	public abstract List<InterviewEntity> findInterviewsByPosition(PositionEntity position); //needed?? gestor
	public abstract boolean interviewerHasDateConflict(Date date, UserEntity interviewer);
	public abstract boolean candidateHasDateConflict(Date date, UserEntity candidate);
	public abstract List<InterviewEntity> findCarriedOutInterviewsByPosition(
			PositionEntity position);
	public abstract List<InterviewEntity> findScheduledInterviewsByPosition(
			PositionEntity position);

}
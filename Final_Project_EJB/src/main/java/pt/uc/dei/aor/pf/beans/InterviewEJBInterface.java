package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface InterviewEJBInterface {
	
	public abstract void save(InterviewEntity interview);
	public abstract void update(InterviewEntity interview);
	public abstract void delete(InterviewEntity interview);
	public abstract InterviewEntity find(Long id);
	public abstract List<InterviewEntity> findAll();
	public abstract List<InterviewEntity> findCarriedOutInterviews(Date date1, Date date2, String period);  //relatório
	public abstract List<InterviewEntity> findInterviewsByUser(UserEntity interviewer);
	public abstract List<InterviewEntity> findCarriedOutInterviewsByUser(UserEntity interviewer);
	public abstract List<InterviewEntity> findInterviewsBySubmission(SubmissionEntity submission);
	public abstract List<InterviewEntity> findInterviewsByDate(Date date1, Date date2, String period); // daily/monthly/yearly
	public abstract List<InterviewEntity> findInterviewsByPosition(PositionEntity position); //?? gestor...
	
	// ver melhor...

}
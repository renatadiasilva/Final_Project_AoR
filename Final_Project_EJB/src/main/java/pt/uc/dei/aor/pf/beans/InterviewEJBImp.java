package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public class InterviewEJBImp implements InterviewEJBInterface {

	@Override
	public void save(InterviewEntity interview) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(InterviewEntity interview) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(InterviewEntity interview) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InterviewEntity find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findCarriedOutInterviews(Date date1,
			Date date2, String period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findInterviewsByUser(UserEntity interviewer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findCarriedOutInterviewsByUser(
			UserEntity interviewer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findInterviewsBySubmission(
			SubmissionEntity submission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findInterviewsByDate(Date date1, Date date2,
			String period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findInterviewsByPosition(
			PositionEntity position) {
		// TODO Auto-generated method stub
		return null;
	}

}

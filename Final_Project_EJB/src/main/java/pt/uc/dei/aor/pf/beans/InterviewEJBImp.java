package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.InterviewDAO;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class InterviewEJBImp implements InterviewEJBInterface {

	private static final Logger log = LoggerFactory.getLogger(InterviewEJBImp.class);
	
	@EJB
	private InterviewDAO interviewDAO;
	
	@Override
	public void save(InterviewEntity interview) {
		log.info("Saving interview in DB");
		interviewDAO.save(interview);
	}

	@Override
	public void update(InterviewEntity interview) {
		//validations here????
		log.info("Updating interview of DB");
		interviewDAO.update(interview);
	}

	@Override
	public void delete(InterviewEntity interview) {
		log.info("Deleting interview from DB");
		interviewDAO.delete(interview.getId(), InterviewEntity.class);		
	}

	@Override
	public InterviewEntity find(Long id) {
		log.info("Finding interview by ID");
		return interviewDAO.find(id);
	}

	@Override
	public List<InterviewEntity> findAll() {
		log.info("Creating Query for all interviews");
		return interviewDAO.findAll();
	}

	@Override
	public List<InterviewEntity> findCarriedOutInterviews(Date date1,
			Date date2, String period) {
		log.info("Finding all carried out interviews between two dates");
		//muitas validações...
//		switch (period) {
//		case "daily": break;
//		case "monthly": break;
//		case "yearly": break;
//		}
			
		return interviewDAO.findCarriedOutInterviews(date1, date2);
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

	@Override
	public List<InterviewEntity> findScheduledInterviewsByUser(
			UserEntity interviewer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findInterviewsByScheduler(UserEntity scheduler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findScheduledInterviewsByManager(
			UserEntity manager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findCarriedOutInterviewsByManager(
			UserEntity manager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewEntity> findScheduledInterviewsByCanditate(
			UserEntity candidate) {
		// TODO Auto-generated method stub
		return null;
	}

	
	// verificar tudo ver MusicaBean
}

package pt.uc.dei.aor.pf.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.InterviewDao;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class InterviewEJBImp implements InterviewEJBInterface {

	private static final Logger log = LoggerFactory.getLogger(InterviewEJBImp.class);
	
	@EJB
	private InterviewDao interviewDAO;
	
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
	public List<InterviewEntity> findCarriedOutInterviews(Date date1, Date date2) {
		log.info("Finding all carried out interviews between two dates");
		return interviewDAO.findCarriedOutInterviews(date1, date2);
	}

	@Override
	public List<InterviewEntity> findCarriedOutInterviewsByUser(
			UserEntity interviewer) {
		log.info("Finding all carried out interviews of a interviewer");
		return interviewDAO.findCarriedOutInterviewsByUser(interviewer);
	}

	@Override
	public List<InterviewEntity> findScheduledInterviewsByUser(
			UserEntity interviewer) {
		log.info("Finding all scheduled interviews of a interviewer");
		return interviewDAO.findScheduledInterviewsByUser(interviewer);
	}

	@Override
	public List<InterviewEntity> findInterviewsByPosition(
			PositionEntity position) {
		
		// needed????
		
		// carried out and scheduled
		
		// dá???? (meter no CDIBean)
		List<InterviewEntity> l = new ArrayList<InterviewEntity>();
		
		// submission list of position
		List<SubmissionEntity> list = position.getSubmissions();
		
		// collect all interviews for all submissions
		for (SubmissionEntity s : list) {
			List<InterviewEntity> listI = s.getInterviews();
			l.addAll(listI);
		}
		
		return l; // order by???
		
	}

	@Override
	public List<InterviewEntity> findScheduledInterviewsByCandidate(
			UserEntity candidate) {
		log.info("Finding all scheduled interviews of a candidate");
		return interviewDAO.findScheduledInterviewsByCandidate(candidate);
	}

	private void isInterviewComplete(InterviewEntity interview) {
		boolean hasError = false;
		
		// is empty??? more anotations? more validations?
		if (interview == null) hasError = true;
		else if (interview.getDate() == null) hasError = true;
		else if (interview.getInterviewers() == null) hasError = true;
		else if (interview.getScheduledBy() == null) hasError = true;
		else if (interview.getScript() == null) hasError = true;
		else if (interview.getSubmission() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The interview is missing data. "
					+ "Check the notnull attributes.");
	}

}

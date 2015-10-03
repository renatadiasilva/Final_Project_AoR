package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.InterviewDao;
import pt.uc.dei.aor.pf.dao.PositionDao;
import pt.uc.dei.aor.pf.dao.SubmissionDao;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class InterviewEJBImp implements InterviewEJBInterface {

	private static final Logger log = 
			LoggerFactory.getLogger(InterviewEJBImp.class);
	
	@EJB
	private InterviewDao interviewDAO;
	
	@EJB
	private SubmissionDao submissionDAO;
	
	@EJB
	private PositionDao positionDAO;
	
	@Override
	public void save(InterviewEntity interview) {
		interview.setMiscKey(RandomStringUtils.randomAlphanumeric(8));
		log.info("Saving interview in DB");
		isInterviewComplete(interview);
		interviewDAO.save(interview);
	}

	@Override
	public void update(InterviewEntity interview) {
		log.info("Updating interview of DB");
		isInterviewComplete(interview);
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
			Date date2) {
		log.info("Finding all carried out interviews between two dates");
		return interviewDAO.findCarriedOutInterviews(date1, date2);
	}

	@Override
	public List<InterviewEntity> findAllCarriedOutInterviews() {
		log.info("Finding all carried out interviews");
		return interviewDAO.findAllCarriedOutInterviews();
	}

	@Override
	public List<InterviewEntity> findAllScheduledInterviews() {
		log.info("Finding all scheduled interviews");
		return interviewDAO.findAllScheduledInterviews();
	}
	
	@Override
	public List<InterviewEntity> findCarriedOutInterviewsOfManager(
			UserEntity manager) {
		log.info("Finding all carried out interviews of a manager");
		return interviewDAO.findCarriedOutInterviewsOfManager(manager);
	}

	@Override
	public List<InterviewEntity> findScheduledInterviewsOfManager(
			UserEntity manager) {
		log.info("Finding all scheduled interviews of a manager");
		return interviewDAO.findScheduledInterviewsOfManager(manager);
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
	public boolean interviewerHasDateConflict(Date date, 
			UserEntity interviewer) {
		log.info("Checking if interviewer has date conflict");
		List<InterviewEntity> inter = 
				interviewDAO.findByDateAndInterviewer(date, interviewer);
		if (inter == null) return false;
		if (inter.size() == 1) return true;
		return false;
	}

	@Override
	public boolean candidateHasDateConflict(Date date, UserEntity candidate) {
		log.info("Checking if candidate has date conflit");
		List<InterviewEntity> inter = interviewDAO.findByDateAndCandidate(date,
				candidate);
		if (inter == null) return false;
		if (inter.size() == 1) return true;
		return false;
	}
	
	@Override
	public List<InterviewEntity> findInterviewsByPosition(
			PositionEntity position) {
		log.info("Finding all interviews of a position");
		return interviewDAO.findInterviewByPosition(position);	
	}

	@Override
	public List<InterviewEntity> findCarriedOutInterviewsByPosition(
			PositionEntity position) {
		log.info("Finding all interviews of a position");
		return interviewDAO.findCarriedOutInterviewByPosition(position);	
	}

	@Override
	public List<InterviewEntity> findScheduledInterviewsByPosition(
			PositionEntity position) {
		log.info("Finding all interviews of a position");
		return interviewDAO.findScheduledInterviewByPosition(position);	
	}

	@Override
	public List<InterviewEntity> findScheduledInterviewsByCandidate(
			UserEntity candidate) {
		log.info("Finding all scheduled interviews of a candidate");
		return interviewDAO.findScheduledInterviewsByCandidate(candidate);
	}

	@Override
	public List<InterviewEntity> findInterviewsOfUser(UserEntity interviewer) {
		log.info("Finding all interviews of an interviewer");
		return interviewDAO.findInterviewsOfUser(interviewer);
	}

	@Override
	public List<InterviewEntity> findInterviewsOfSubmission(
			SubmissionEntity submission) {
		log.info("Finding all interviews of a submission");
		return interviewDAO.findInterviewsOfSubmission(submission);
	}

	@Override
	public List<InterviewEntity> findInterviewsWithScript(
			ScriptEntity script) {
		log.info("Finding all interviews using a script");
		return interviewDAO.findInterviewsWithScript(script);
	}

	@Override
	public List<Object[]> averageTimeToFirstInterview(Date date1, Date date2,
			char period) {
		log.info("Computing the average time to first interview"
				+ " between two dates by period");
		return interviewDAO.averageTimeToFirstInterview(date1, date2, period);
	}

	@Override
	public Long findTotalCarriedOutInterviews(Date date1, Date date2) {
		log.info("Finding total of carried out interviews of a position");
		return interviewDAO.findTotalCarriedOutInterviews(date1, date2);
	}

	@Override
	public List<InterviewEntity> findCarriedOutInterviewsByCandidate(
			UserEntity candidate) {
		log.info("Finding all carried out interviews of a candidate");
		return interviewDAO.findCarriedOutInterviewsByCandidate(candidate);
	}

	@Override
	public Double overallAverageTimeToFirstInterview(Date date1, Date date2) {
		log.info("Computing the overall average time to first interview"
				+ " between two dates");
		return interviewDAO.overallAverageTimeToFirstInterview(date1, date2);
	}

	private void isInterviewComplete(InterviewEntity interview) {
		boolean hasError = false;
		
		if (interview == null) hasError = true;
		else if (interview.getScheduledBy() == null) hasError = true;
		else if (interview.getSubmission() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The interview is missing data. "
					+ "Check the notnull attributes.");
	}

}
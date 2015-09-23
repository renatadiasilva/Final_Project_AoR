package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.dao.InterviewDao;
import pt.uc.dei.aor.pf.dao.SubmissionDao;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class SubmissionEJBImp implements SubmissionEJBInterface {

	private static final Logger log = 
			LoggerFactory.getLogger(SubmissionEJBImp.class);
	
	@EJB
	private SubmissionDao submissionDAO;
	
	@EJB
	private InterviewDao interviewDAO;

	@Override
	public void save(SubmissionEntity submission) {
		log.info("Saving submission in DB");
		isSubmissionComplete(submission);
		submissionDAO.save(submission);
	}

	@Override
	public void update(SubmissionEntity submission) {
		log.info("Updating submission of DB");
		isSubmissionComplete(submission);
		submissionDAO.update(submission);
	}

	@Override
	public void delete(SubmissionEntity submission) {
		log.info("Deleting submission from DB");
		submissionDAO.delete(submission.getId(), SubmissionEntity.class);
	}

	@Override
	public void addPositionToSpontaneous(SubmissionEntity submission,
			PositionEntity position, UserEntity user) {
		log.info("Adding position to a spontaneous submission (cloning)");
		// clone submission: na web???
		SubmissionEntity newSubmission = new SubmissionEntity(
				submission.getCandidate(), Constants.STATUS_SUBMITED,
				submission.getMotivationLetter(),
				submission.getSources(), false);
		newSubmission.setPosition(position);
		newSubmission.setAssociatedBy(user);
		newSubmission.setDate(submission.getDate());
		save(newSubmission);
}

	@Override
	public SubmissionEntity find(Long id) {
		log.info("Finding submission by ID");
		return submissionDAO.find(id);
	}

	@Override
	public List<SubmissionEntity> findAll() {
		log.info("Creating Query for all submissions");
		return submissionDAO.findAll();
	}

	@Override
	public List<SubmissionEntity> findSpontaneousSubmissions() {
		log.info("Finding spontaneous submissions");
		return submissionDAO.findSpontaneousSubmissions();
	}

	@Override
	public List<SubmissionEntity> findSubmissionsByDate(Date date1,
			Date date2) {
		log.info("Finding submissions between two dates");
		return submissionDAO.findSubmissionsByDate(date1, date2);
	}

	@Override
	public List<SubmissionEntity> findSpontaneousSubmissionsByDate(Date date1,
			Date date2) {
		log.info("Finding spontaneous submissions between two dates");
		return submissionDAO.findSpontaneousSubmissionsByDate(date1, date2);
	}

	@Override
	public List<SubmissionEntity> findRejectedSubmissions(Date date1,
			Date date2) {
		log.info("Finding rejected submissions between two dates");
		return submissionDAO.findRejectSubmissionsByDate(date1, date2);
	}

	@Override
	public List<SubmissionEntity> findSubmissionsBySource(String source,
			Date date1, Date date2) {
		log.info("Finding submissions by source between two dates");
		return submissionDAO.findSubmissionsBySource(source, date1, date2);
	}

	@Override
	public List<SubmissionEntity> findPresentedProposals(Date date1,
			Date date2) {
		log.info("Finding presented proposals between two dates");
		return submissionDAO.findPresentedProposalByDate(date1, date2);
	}

	@Override
	public List<SubmissionEntity> findSubmissionsOfPosition(
			PositionEntity position) {
		log.info("Finding submissions of a position");
		return submissionDAO.findSubmissionsOfPosition(position);
	}

	@Override
	public List<SubmissionEntity> findSubmissionsOfCandidate(
			UserEntity candidate) {
		log.info("Finding submissions of a candidate");
		return submissionDAO.findSubmissionsOfCandidate(candidate);
	}
	
	@Override
	public List<Object[]> averageTimeToHired(Date date1, Date date2, char p) {
		log.info("Computing the average time to hired between two dates");
		return submissionDAO.averageTimeToHired(date1, date2, p);
	}

	@Override
	public List<Object[]> countSubmissionsByDate(Date date1, Date date2,
			char p, String restriction) {
		log.info("Computing number of submissions between two dates");
		return submissionDAO.countSubmissionsByDate(date1, date2, p,
				restriction);
	}

	@Override
	public Long countTotalSubmissions(Date date1,
			Date date2) {
		log.info("Counting total submissions between two dates");
		return submissionDAO.countTotalSubmissions(date1, date2);
	}

	@Override
	public Long countTotalSubmissionsPos(Date date1,
			Date date2) {
		log.info("Counting total submissions between two dates");
		return submissionDAO.countTotalSubmissionsPos(date1, date2);
	}

	@Override
	public Long countTotalSpontaneous(Date date1,
			Date date2) {
		log.info("Counting total spontaneous submissions between two dates");
		return submissionDAO.countTotalSpontaneous(date1, date2);
	}

	@Override
	public Long countTotalRejected(Date date1, Date date2) {
		log.info("Counting total rejected submissions between two dates");
		return submissionDAO.countTotalRejected(date1, date2);
	}
	
	@Override
	public Long countTotalProposals(Date date1, Date date2) {
		log.info("Counting total presented proposals between two dates");
		return submissionDAO.countTotalProposals(date1, date2);
	}
	
	@Override
	public Long countTotalHired(Date date1, Date date2) {
		log.info("Counting total hired people between two dates");
		return submissionDAO.countTotalHired(date1, date2);
	}
	
	@Override
	public Long countTotalRejectedPos(Date date1, Date date2) {
		log.info("Counting total rejected submissions between two dates"
				+ "(by position date)");
		return submissionDAO.countTotalRejectedPos(date1, date2);
	}
	
	@Override
	public Long countTotalProposalsPos(Date date1, Date date2) {
		log.info("Counting total presented proposals between two dates"
				+ "(by position date)");
		return submissionDAO.countTotalProposalsPos(date1, date2);
	}
	
	@Override
	public List<Object[]> countSubmissionsBySourceTable(Date date1, Date date2,
			List<String> sources) {
		log.info("Finding number of submissions by source between two dates");
		return submissionDAO.countSubmissionsBySourceTable(date1,
				date2, sources);
	}

	@Override
	public Double overallAverageTimeToHired(Date date1, Date date2) {
		log.info("Computing the overall average time to hired"
				+ " between two dates");
		return submissionDAO.overallAverageTimeToHired(date1, date2);
	}
	
	@Override
	public List<SubmissionEntity> findDetailOfPosition(PositionEntity position) {
		log.info("Finding submissions details of a position");
		return submissionDAO.findDetailOfPosition(position);
	}

	private void isSubmissionComplete(SubmissionEntity submission) {
		boolean hasError = false;
		
		if (submission == null) hasError = true;
		else if (submission.getCandidate() == null) hasError = true;
		else if (submission.getDate() == null) hasError = true;
		else if (submission.getStatus() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The submission is missing"
					+ " data. Check the notnull attributes.");
	}

}

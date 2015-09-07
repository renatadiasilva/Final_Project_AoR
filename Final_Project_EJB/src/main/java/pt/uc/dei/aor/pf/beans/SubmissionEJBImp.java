package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.SubmissionDao;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;

@Stateless
public class SubmissionEJBImp implements SubmissionEJBInterface {

	private static final Logger log = LoggerFactory.getLogger(InterviewEJBImp.class);
	
	@EJB
	private SubmissionDao submissionDAO;

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
	public List<SubmissionEntity> findSubmissionByPosition(
			PositionEntity position) {
		log.info("Finding submission by position");
		return submissionDAO.findSubmissionsByPosition(position);
	}

	@Override
	public List<SubmissionEntity> findSubmissionsByDate(Date date1, Date date2) {
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
	public List<SubmissionEntity> findPresentedProposals(Date date1, Date date2) {
		log.info("Finding presented proposals between two dates");
		return submissionDAO.findPresentedProposalByDate(date1, date2);
	}

	private void isSubmissionComplete(SubmissionEntity submission) {
		boolean hasError = false;
		
		if (submission == null) hasError = true;
		else if (submission.getCandidate() == null) hasError = true;
		else if (submission.getDate() == null) hasError = true;
		else if (submission.getStatus() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The submission is missing data. "
					+ "Check the notnull attributes.");
	}

}
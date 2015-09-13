package pt.uc.dei.aor.pf.admin;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;


@Named
@RequestScoped
public class InterviewSearchCDI {

	private static final Logger log = LoggerFactory.getLogger(InterviewSearchCDI.class);

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	// search fields
	private Date date1, date2;

	private Long id;   // Users id=? Position id=?

	private boolean result;

	private List<InterviewEntity> ilist;

	public InterviewSearchCDI() {
	}

	public void searchAllInterviews() {
		log.info("Searching for all interviews");
		this.ilist = interviewEJB.findAll();
	}

	public void searchCarriedOutInterviews() {
		log.info("Searching carried out interviews between two dates");
		this.ilist = interviewEJB.findCarriedOutInterviews(date1, date2);
	}

	public void searchCarriedOutInterviewsByUser() {
		log.info("Searching carried out interviews of a interviewer");
		UserEntity interviewer = userEJB.find(id);
//		if ( (interviewer != null) && (interviewer.getRoles().contains("INTERVIEWER"))) {
		if ( interviewer != null) {
			this.ilist = interviewEJB.findCarriedOutInterviewsByUser(interviewer);
		} else log.error("No interviewer with id "+id);
	}

	public void searchScheduledInterviewsByUser() {
		log.info("Searching scheduled interviews of interviewer");
		UserEntity interviewer = userEJB.find(id);
		if ( (interviewer != null) && (interviewer.getRoles().contains("INTERVIEWER"))) {
			this.ilist = interviewEJB.findScheduledInterviewsByUser(interviewer);
		} else log.error("No interviewer with id "+id);
	}

	public void checkInterviewerHasDateConflict() {
		log.info("Checking if interviewer has date conflict");
		UserEntity interviewer = userEJB.find(id);
		if ( (interviewer != null) && (interviewer.getRoles().contains("INTERVIEWER"))) {
			result = interviewEJB.interviewerHasDateConflict(date1, interviewer);
		} else log.error("No interviewer with id "+id);
	}

	public void checkCandidateHasDateConflict() {
		log.info("Checking if candidate has date conflict");
		UserEntity candidate = userEJB.find(id);
		if ( (candidate != null) && (candidate.getRoles().contains("CANDIDATE"))) {
			result = interviewEJB.candidateHasDateConflict(date1, candidate);
		} else log.error("No candidate with id "+id);
	}

	public void searchInterviewsByPosition() {
		log.info("Searching interviews of position");
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			this.ilist = interviewEJB.findInterviewsByPosition(position);
		} else log.error("No position with id "+id);
	}

	public void searchCarriedOutInterviewsByPosition() {
		log.info("Searching carried out interviews of position");
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			this.ilist = interviewEJB.findCarriedOutInterviewsByPosition(position);
		} else log.error("No position with id "+id);
	}

	public void searchScheduledInterviewsByPosition() {
		log.info("Searching scheduled interviews of position");
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			this.ilist = interviewEJB.findScheduledInterviewsByPosition(position);
		} else log.error("No position with id "+id);
	}

	public void searchScheduledInterviewsByCandidate() {
		log.info("Searching scheduled interviews of candidate");
		UserEntity candidate = userEJB.find(id);
		if ( (candidate != null) && (candidate.getRoles().contains("CANDIDATE"))) {
			this.ilist = interviewEJB.findScheduledInterviewsByCandidate(candidate);
		} else log.error("No candidate with id "+id);
	}

	// getters e setters

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<InterviewEntity> getIlist() {
		return ilist;
	}

	public void setIlist(List<InterviewEntity> ilist) {
		this.ilist = ilist;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
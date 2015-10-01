package pt.uc.dei.aor.pf.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;


@Named
@RequestScoped
public class InterviewSearchCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(InterviewSearchCDI.class);

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private ScriptEJBInterface scriptEJB;
	
	@Inject
	private UserSessionManagement userSession;
	
	// search fields
	private Date date1, date2;
	private Long id;
	private boolean result;
	private UserEntity candidate;

	private List<InterviewEntity> ilist;

	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 
	private SimpleDateFormat ftHour = new SimpleDateFormat ("HH:mm"); 

	public InterviewSearchCDI() {
	}
	
	public void searchCarriedOutInterviews() {
		log.info("Searching carried out interviews between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.ilist = interviewEJB.findCarriedOutInterviews(date1, date2);
	}

	public void searchAllInterviews() {
		log.info("Searching for all interviews");
		this.ilist = interviewEJB.findAll();
	}
	
	public void searchCarriedOutInterviewsByUser() {
		log.info("Searching carried out interviews of a interviewer");
		log.debug("Id "+id);
		UserEntity interviewer = userEJB.find(id);
//		if ( (interviewer != null) && 
//				(interviewer.getRoles().contains("INTERVIEWER"))) {
		if (interviewer != null) {
			this.ilist = interviewEJB.findCarriedOutInterviewsByUser(
					interviewer);
		} else log.error("No interviewer with id "+id);
	}

	public void searchScheduledInterviewsByUser() {
		log.info("Searching scheduled interviews of interviewer");
		log.debug("Id "+id);
		UserEntity interviewer = userEJB.find(id);
		if ( interviewer != null) {
			this.ilist = interviewEJB.findScheduledInterviewsByUser(
					interviewer);
		} else log.error("No interviewer with id "+id);
	}

	public void checkInterviewerHasDateConflict() {
		log.info("Checking if interviewer has date conflict");
		log.debug("Id "+id);
		log.debug("Date "+date1);
		UserEntity interviewer = userEJB.find(id);
		if ( interviewer != null) {
			result = interviewEJB.interviewerHasDateConflict(date1,
					interviewer);
		} else log.error("No interviewer with id "+id);
	}

	public void checkCandidateHasDateConflict() {
		log.info("Checking if candidate has date conflict");
		log.debug("Id "+id);
		log.debug("Date "+date1);
		UserEntity candidate = userEJB.find(id);
		if ( (candidate != null) && 
				(candidate.getRoles().contains("CANDIDATE"))) {
			result = interviewEJB.candidateHasDateConflict(date1, candidate);
		} else log.error("No candidate with id "+id);
	}

	public void searchInterviewsByPosition() {
		log.info("Searching interviews of position");
		log.debug("Id "+id);
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			this.ilist = interviewEJB.findInterviewsByPosition(position);
		} else log.error("No position with id "+id);
	}

	public void searchCarriedOutInterviewsByPosition() {
		log.info("Searching carried out interviews of position");
		log.debug("Id "+id);
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			this.ilist = interviewEJB.findCarriedOutInterviewsByPosition(
					position);
		} else log.error("No position with id "+id);
	}

	public void searchScheduledInterviewsByPosition() {
		log.info("Searching scheduled interviews of position");
		log.debug("Id "+id);
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			this.ilist = interviewEJB.findScheduledInterviewsByPosition(
					position);
		} else log.error("No position with id "+id);
	}

	public void searchScheduledInterviewsByCandidate() {
		log.info("Searching scheduled interviews of candidate");
		this.candidate = userEJB.find(userSession.getCurrentUserClone().getId());
		if ( (candidate != null) && 
				(candidate.getRoles().contains("CANDIDATE"))) {
			this.ilist = interviewEJB.findScheduledInterviewsByCandidate(
					candidate);
		} else log.error("No candidate?!??");
	}

	public void searchInterviewsOfUser() {
		log.info("Searching interviews of interviewer");
		log.debug("Id "+id);
		UserEntity interviewer = userEJB.find(id);
		if ( (interviewer != null) ) {
//				if ( (interviewer != null) && 
//				(interviewer.getRoles().contains("CANDIDATE"))) {
			this.ilist = interviewEJB.findInterviewsOfUser(interviewer);
		} else log.error("No interviewer with id "+id);
	}
	
	public void searchInterviewsOfSubmission() {
		log.info("Searching interviews of submission");
		log.debug("Id "+id);
		SubmissionEntity submission = submissionEJB.find(id);
		if (submission != null) {
			this.ilist = interviewEJB.findInterviewsOfSubmission(submission);
		} else log.error("No submission with id "+id);
	}

	public String getDay(InterviewEntity i) {
		return ftDate.format(i.getDate());	
	}
	
	public String getHour(InterviewEntity i) {
		return ftHour.format(i.getDate());	
	}
	
	// getters e setters

	public Date getDate1() {
		if (date1 != null) return date1;
		return new Date();
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		if (date2 != null) return date2;
		return new Date();
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

	public UserEntity getCandidate() {
		return candidate;
	}

	public void setCandidate(UserEntity candidate) {
		this.candidate = candidate;
	}

}
package pt.uc.dei.aor.pf.admin;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;


@Named
@RequestScoped
public class InterviewSearchCDI {

	private static final Logger log = LoggerFactory.getLogger(InterviewSearchCDI.class);

	@EJB
	private InterviewEJBInterface interviewEJB;
	
	@EJB
	private UserEJBInterface userEJB;

	// search fields
	private Date date1, date2;

	private Long idU;   // id=?
	
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
		UserEntity interviewer = userEJB.find(idU);
		if ( (interviewer != null) && (interviewer.getRoles().contains("INTERVIEWER"))) {
			this.ilist = interviewEJB.findCarriedOutInterviewsByUser(interviewer);
		} else log.error("No interviewer with id "+idU);
	}

	public void searchScheduledInterviewsByUser() {
		log.info("Searching schedule interviews of a interviewer");
		UserEntity interviewer = userEJB.find(idU);
		if ( (interviewer != null) && (interviewer.getRoles().contains("INTERVIEWER"))) {
			this.ilist = interviewEJB.findScheduledInterviewsByUser(interviewer);
		} else log.error("No interviewer with id "+idU);
	}

	public void checkInterviewerHasDateConflit() {
		log.info("Checking if interviewer has date conflict");
		UserEntity interviewer = userEJB.find(idU);
		if ( (interviewer != null) && (interviewer.getRoles().contains("INTERVIEWER"))) {
			this.ilist = interviewEJB.interviewerHasDateConflict(date1, interviewer)
		} else log.error("No interviewer with id "+idU);
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

	public Long getIdU() {
		return idU;
	}

	public void setIdU(Long idU) {
		this.idU = idU;
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
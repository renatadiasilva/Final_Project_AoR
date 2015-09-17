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
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;


@Named
@RequestScoped
public class SubmissionSearchCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(SubmissionSearchCDI.class);

	@EJB
	private SubmissionEJBInterface submissionEJB;
	
	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private InterviewEJBInterface interviewEJB;

	// search fields
	private Date date1, date2;
	private String source;
	private Long id;

	private List<SubmissionEntity> slist;

	public SubmissionSearchCDI() {
	}

	public void remove() {
		log.info("Removing submission by id");
		log.debug("Id "+id);
		SubmissionEntity submission = submissionEJB.find(id);
		if (submission != null) {
			List<InterviewEntity> ilist = 
					interviewEJB.findInterviewsOfSubmission(submission);
			if (ilist != null && !ilist.isEmpty()) {
				System.out.println("A candidatura tem entrevistas."
						+ "Quer mesmo removê-la?");
				boolean delSub = true; // pedir resposta
				if (delSub) submissionEJB.delete(submission);
			}
		submissionEJB.delete(submission);
		} else log.error("No submission with id "+id);
	}

	public void searchAll() {
		log.info("Searching for all submissions");
		this.slist = submissionEJB.findAll();
	}
	
	public void searchAllSponteanous() {
		log.info("Searching for all spontaneouse submissions");
		this.slist = submissionEJB.findSpontaneousSubmissions();
	}
	
	public void searchByDate() {
		log.info("Searching for submissions between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findSubmissionsByDate(date1, date2);
	}	

	public void searchSpontanouseByDate() {
		log.info("Searching for spontaneous submissions between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findSpontaneousSubmissionsByDate(date1,
				date2);
	}	

	public void searchRejectedByDate() {
		log.info("Searching for rejected submissions between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findRejectedSubmissions(date1, date2);
	}	

	public void searchProposalByDate() {
		log.info("Searching for proposals presented between two dates");
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findPresentedProposals(date1, date2);
	}	

	public void searchBySourceAndDate() {
		log.info("Searching for submissions by source between two dates");
		log.debug("Source "+source);
		log.debug("Dates between "+date1+" and "+date2);
		this.slist = submissionEJB.findSubmissionsBySource(source, date1,
				date2);
	}	

	public void searchSubmissionsOfPosition() {
		log.info("Searching for submissions by position");
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			log.debug("Script "+position.getPositionCode());
			this.slist = submissionEJB.findSubmissionsOfPosition(position);
		} else log.error("No position with id "+id);
	}	

	public void searchSubmissionsOfCandidate() {
		log.info("Searching for submissions by candidate");
		UserEntity user = userEJB.find(id);
		if (user != null && user.getRoles().contains("CANDIDATE")) {
			log.debug("Script "+user.getFirstName());
			this.slist = submissionEJB.findSubmissionsOfCandidate(user);
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

	public List<SubmissionEntity> getSlist() {
		return slist;
	}

	public void setSlist(List<SubmissionEntity> slist) {
		this.slist = slist;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
package pt.uc.dei.aor.pf.admin;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;


@Named
@RequestScoped
public class SubmissionSearchCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(SubmissionSearchCDI.class);

	@EJB
	private SubmissionEJBInterface submissionEJB;

	// search fields
	private Date date1, date2;
	private String source;

	private List<SubmissionEntity> slist;

	public SubmissionSearchCDI() {
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

}
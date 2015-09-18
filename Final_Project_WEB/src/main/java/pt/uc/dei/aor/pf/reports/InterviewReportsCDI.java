package pt.uc.dei.aor.pf.reports;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;

@Named
@RequestScoped
public class InterviewReportsCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(InterviewReportsCDI.class);

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private SubmissionEJBInterface submissionEJB;
	
	@Inject
	private ReportCDI report;
	
	// data input fields
	private Date date1, date2;

	// interview counts and results by period between two dates (file?)
	public List<Integer> interviewCountResults(Date d1, Date d2, 
			String period) {
		log.info("Creating report with interview countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		return report.reportCounting(d1, d2, period, 1, null);
		// if 0, no interviews, no report!
	}

	// average time to first interview by period between two dates (file?)
	public List<Integer> averageTimeToInterview(Date d1, Date d2,
			String period) {
		log.info("Creating report with average time to first interview");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		return report.reportCounting(d1, d2, period, 2, null);
		// if -1, no valid submissions, no report!
	}

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

}
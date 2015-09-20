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
import pt.uc.dei.aor.pf.constants.Constants;

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
	private ReportManager report;
	
	// data input fields
	private Date d1, d2;
	private String period;
	private Long id;

	// interview counts and results by period between two dates (file?)
	public void interviewCountResults() {
		log.info("Creating report with interview countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_INT_CNTINTER, null);
		// if 0, no interviews, no report!
	}

	// average time to first interview by period between two dates (file?)
	public void averageTimeToInterview(Date d1, Date d2,
			String period) {
		log.info("Creating report with average time to first interview");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_INT_AVGINTER, null);
		// if -1, no valid submissions, no report!
	}

	public void interviewDetailOfCandidate() {
		log.info("Creating report with detaild interview info of candidate");
		List<Object[]> list = report.reportCounting(null, null, "noperiod",
				Constants.REPORT_INT_INTCANDI, null);
		// if 0, no interviews, no report!
	}

	public Date getD1() {
		return d1;
	}

	public void setD1(Date d1) {
		this.d1 = d1;
	}

	public Date getD2() {
		return d2;
	}

	public void setD2(Date d2) {
		this.d2 = d2;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
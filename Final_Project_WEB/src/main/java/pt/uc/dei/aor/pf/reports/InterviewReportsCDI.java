package pt.uc.dei.aor.pf.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

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

	// needed here??? FILE!!!
	public int submissionsByPosition(PositionEntity position) {
		log.info("Creating report with submissions of a position");
		log.debug("Position ", position.getPositionCode());

		List<SubmissionEntity> listS = position.getSubmissions();
		System.out.println("\n\nPosição :"+position.getTitle()+
				" ("+position.getPositionCode()+")"); //truncate title??"
		for (SubmissionEntity s : listS) 			
			report.printCandidateInfo(s, listS.indexOf(s), false);
		return listS.size();  //print also... LIST é para gráficos
	}

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

	// average time to be hired by period between two dates (file?)
	public List<Integer> averageTimeToBeHired(Date d1, Date d2,
			String period) {
		log.info("Creating report with average time to be hired");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		return report.reportCounting(d1, d2, period, 3, null);
		// if -1, no valid submissions, no report!
	}

	// candidate counts by period between two dates (file?)
	public List<Integer> candidatesCountResults(Date d1, Date d2,
			String period) {
		log.info("Creating report with candidate countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		return report.reportCounting(d1, d2, period, 4, null);
		// if 0, no candidates, no report!
	}

	// spontaneous submission counts by period between two dates (file?)
	public List<Integer> spontaneousCountResults(Date d1, Date d2,
			String period) {
		log.info("Creating report with spontaneous submissions");
		log.debug("From "+d1+" to "+d2+" with period "+period);		
		return report.reportCounting(d1, d2, period, 5, null);
		// if 0, no spontaneous submissions, no report!
	}

	// reject submission counts/rejected reasons by period between two dates
	public List<Integer> rejectedCountResults(Date d1, Date d2,
			String period) {
		log.info("Creating report with reject candidates countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		return report.reportCounting(d1, d2, period, 6, null);
		// if 0, no rejected submitions, no report!
	}

	// presented proposal counts/results by period between two dates (file?)
	public List<Integer> proposalCountResults(Date d1, Date d2,
			String period) {
		log.info("Creating report with presented proposal countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);		
		return report.reportCounting(d1, d2, period, 7, null);
		// if 0, no presented proposals, no report!
	}

	// submission source counts by period between two dates (file?)
	public List<Integer> sourceCount(Date d1, Date d2, String period,
			List<String> sources) {
		log.info("Creating report with submission's source countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);				
		return report.reportCounting(d1, d2, period, 8, sources);
		// if 0, no submissions, no report!
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
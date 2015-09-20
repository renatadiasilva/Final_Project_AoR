package pt.uc.dei.aor.pf.reports;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.entities.PositionEntity;

@Named
@RequestScoped
public class SubmissionReportsCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(SubmissionReportsCDI.class);

	@EJB
	private SubmissionEJBInterface submissionEJB;
	
	@EJB
	private PositionEJBInterface positionEJB;
	
	@Inject
	private ReportCDI report;
	
	private List<SubmissionReport> sreports = new ArrayList<SubmissionReport>();
	
	// data input fields
	private Date date1, date2;
	private Long id;
	private String period;

	public void submissionsByPosition() {
		log.info("Creating report with number of submissions by position");
		
		
		//period
		List<Object[]> result = submissionEJB.countSubmissionsByPosition(
				date1, date2);
		List<SubmissionReportItem> resultItems =
				new ArrayList<SubmissionReportItem>();
		
		for (Object[] ele : result)
			resultItems.add(new SubmissionReportItem(
					positionEJB.find((Long) ele[0]), (Long) ele[1]));
		
		SubmissionReport sreport = 
				new SubmissionReport("Candidatos por posição", resultItems);
		
		sreports.add(sreport);

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

	public List<SubmissionReport> getSreports() {
		return sreports;
	}

	public void setSreports(List<SubmissionReport> sreports) {
		this.sreports = sreports;
	}
	
	private Long bigIntToLong(BigInteger value) {
		return new Long(value.intValue());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

}

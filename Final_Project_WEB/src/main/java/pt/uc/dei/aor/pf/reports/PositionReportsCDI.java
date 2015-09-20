package pt.uc.dei.aor.pf.reports;

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
import pt.uc.dei.aor.pf.constants.Constants;

@Named
@RequestScoped
public class PositionReportsCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(PositionReportsCDI.class);

	@EJB
	private PositionEJBInterface positionEJB;

	@Inject
	private ReportManager report;

	private List<SubmissionReport> sreports = new ArrayList<SubmissionReport>();

	// data input fields
	private Date d1, d2;
	private Long id;
	private String period;

	// PositionReports.java
	public void submissionsByPosition() {
		log.info("Creating report with number of submissions by position");

		//period
		//		List<Object[]> result = submissionEJB.countSubmissionsByPosition(
		//				date1, date2);
		//		List<SubmissionReportItem> resultItems =
		//				new ArrayList<SubmissionReportItem>();
		//
		//		for (Object[] ele : result)
		//			resultItems.add(new SubmissionReportItem(
		//					positionEJB.find((Long) ele[0]), (Long) ele[1]));
		//
		//		SubmissionReport sreport = 
		//				new SubmissionReport("Candidatos por posição", resultItems);
		//
		//		sreports.add(sreport);

		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_POS_SUBMIPOS, null);
		// if -1, no valid submissions, no report!	
	}


	// pôr na PositionReports
	public void rejectedCountByPosition() {
		log.info("Creating report with number of rejected submissions"
				+ " by position");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_POS_REJECPOS, null);
		// if -1, no valid submissions, no report!	
	}

	public void proposalCountByPosition() {
		log.info("Creating report with number of presented proposal"
				+ " by position");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_POS_PROPOPOS, null);
		// if -1, no valid submissions, no report!	
	}

	//on hold e open??
	public void averageTimeToClose() {
		log.info("Creating report with average time to close a positions");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_POS_AVGCLOSE, null);
		// if -1, no valid submissions, no report!	
	}

	// getters e setters

	public List<SubmissionReport> getSreports() {
		return sreports;
	}

	public void setSreports(List<SubmissionReport> sreports) {
		this.sreports = sreports;
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

}

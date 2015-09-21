package pt.uc.dei.aor.pf.reports;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;

@Named
@RequestScoped
public class PositionReportsCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(PositionReportsCDI.class);

	@EJB
	private PositionEJBInterface positionEJB;

	// results
	private String tableHeader;
	private String totalResult;
	private String periodHeader;
	private List<PositionReport> preports = new ArrayList<PositionReport>();

	// data input fields
	private Date d1, d2;
	private Long id;
	private String period;

	public void submissionsByPosition() {
		log.info("Creating report with number of submissions by position");

		tableHeader = "Número de candidaturas por posição (posições "
				+ "abertas entre "+d1+" e "+d2+")";

		List<Object[]> result = positionEJB.countSubmissionsByPosition(
				d1, d2);
		List<PositionReportItem> resultItems =
				new ArrayList<PositionReportItem>();

		for (Object[] ele : result)
			resultItems.add(new PositionReportItem(
					positionEJB.find((Long) ele[0]), (Long) ele[1]));

		preports.add(new PositionReport(resultItems));

		totalResult = summing(preports.get(0).getItems())+"";
	}


	public void rejectedCountByPosition() {
		log.info("Creating report with number of rejected submissions"
				+ " by position");
		log.debug("From "+d1+" to "+d2+" with period "+period);
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_POS_REJECPOS, null);
		// if -1, no valid submissions, no report!	
	}

	public void proposalCountByPosition() {
		log.info("Creating report with number of presented proposal"
				+ " by position");
		log.debug("From "+d1+" to "+d2+" with period "+period);
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_POS_PROPOPOS, null);
		// if -1, no valid submissions, no report!	
	}

	//on hold e open??
	public void averageTimeToClose() {
		log.info("Creating report with average time to close a positions");
		log.debug("From "+d1+" to "+d2+" with period "+period);
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_POS_AVGCLOSE, null);
		// if -1, no valid submissions, no report!	
	}
	
	// private methods
	private int summing(List<PositionReportItem> list) {
		// compute sum of all quantities
		int sum = 0;
		for(PositionReportItem item : list) {
			sum += item.getCounting();
		}
		return sum;
	}

	// getters e setters

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


	public String getTableHeader() {
		return tableHeader;
	}


	public void setTableHeader(String tableHeader) {
		this.tableHeader = tableHeader;
	}


	public String getTotalResult() {
		return totalResult;
	}


	public void setTotalResult(String totalResult) {
		this.totalResult = totalResult;
	}


	public String getPeriodHeader() {
		return periodHeader;
	}


	public void setPeriodHeader(String periodHeader) {
		this.periodHeader = periodHeader;
	}


	public List<PositionReport> getPreports() {
		return preports;
	}


	public void setPreports(List<PositionReport> preports) {
		this.preports = preports;
	}

}

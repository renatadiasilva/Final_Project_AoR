package pt.uc.dei.aor.pf.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;

@Named
@RequestScoped
public class ReportsCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(ReportsCDI.class);

//	@Inject
//	private ReportManager report;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	// data input fields
	private Date d1, d2;
	private Long id;
	private String period;

	// results
	private String measureHeader;
	private String tableHeader;
	private String totalResult;
	private String periodHeader;
	private String measureFooter;
	private List<ReportItem> report = new ArrayList<ReportItem>();

	// counts submission by position between two dates
	public void submissionsByPosition() {
		log.info("Creating report with number of submissions by position");

		tableHeader = "Número de candidaturas por posição (posições "
				+ "abertas entre "+d1+" e "+d2+")";

		List<Object[]> result = positionEJB.countSubmissionsByPosition(
				d1, d2);

		for (Object[] ele : result)
			report.add(new ReportItem(positionEJB.find((Long) ele[0]),
					"", longToInt((Long) ele[1]), ""));
//			preport.add(new PositionReportItem(
//					positionEJB.find((Long) ele[0]), (Long) ele[1]));

		totalResult = summing(report)+"";
	}

	// average time to be hired by period between two dates
	public void averageTimeToBeHired() {
		log.info("Creating report with average time to be hired");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		
		prepareDates();
		tableHeader = "Tempo Médio para Contratação (candidaturas "
				+ "submetidas entre "+d1+" e "+d2+")";
		measureHeader = "Tempo médio (em dias)";
		measureFooter = "Tempo médio total: ";
		
		// only periods monthly and yearly
		char p = periodShort(period);
		p = (p == Constants.DAILY)? p = Constants.MONTHLY : p;
		List<Object[]> list = submissionEJB.averageTimeToHired(d1, d2, p);

		//Nota: só são apresentados resultados quando há candidaturas
		
		if ( p == Constants.MONTHLY) periodHeader = Constants.PERIOD_MHEADER;
		else periodHeader = Constants.PERIOD_YHEADER;

		// extract date header and average times
		for (Object[] o: list) {
			String dateH = "";
			if (p == Constants.MONTHLY) { 
				Calendar cal = Calendar.getInstance();
				cal.set(2015, doubleToInt((Double) o[2])-1, 1);
				dateH = cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
						Locale.getDefault())+" ";
			}
			//			if (p == Constants.DAILY) dateH = ((Date) o[1])+"";
			dateH += doubleToInt((Double) o[1]);
			report.add(new ReportItem(null, dateH,
					doubleToInt((Double) o[0]), ""));
		}
		int avg = average(report);
		totalResult = (avg >= 0)? avg+"" : Constants.REPORT_NO_HIRED;

	}

	// candidate counts by period between two dates (file?)
	public void submisssionCountResults() {
		log.info("Creating report with candidate countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_SUB_CNTSUBMI, null);
		// if 0, no candidates, no report!
	}

	// spontaneous submission counts by period between two dates (file?)
	public void spontaneousCountResults() {
		log.info("Creating report with spontaneous submissions");
		log.debug("From "+d1+" to "+d2+" with period "+period);		
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_SUB_CNTSPONT, null);
		// if 0, no spontaneous submissions, no report!
	}

	// reject submission counts/rejected reasons by period between two dates
	public void rejectedCountResults() {
		log.info("Creating report with reject candidates countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_SUB_CNTREJEC, null);
		// if 0, no rejected submitions, no report!
	}

	// presented proposal counts/results by period between two dates (file?)
	public void proposalCountResults() {
		log.info("Creating report with presented proposal countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);		
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_SUB_CNTPROPO, null);
		// if 0, no presented proposals, no report!
	}

	// complicado??
	// submission source counts by period between two dates (file?)
	public void sourceCount() {
		log.info("Creating report with submissions source countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
//		List<String> sources = Arrays.asList(Constants.SOURCE_EXPRESSO,
//				Constants.SOURCE_FACEBOOK, Constants.SOURCE_LINKEDIN, 
//				Constants.SOURCE_NETEMPREGO); // more?
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_SUB_CNTSOURC, sources);
		// if 0, no submissions, no report!
	}

	public void hiredCountResuls() {
		log.info("Creating report with hired people countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);				
//		List<Object[]> list = report.reportCounting(d1, d2, period,
//				Constants.REPORT_SUB_CNTHIRED, null);
		// if 0, no submissions, no report!
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

	// complicado?
	public void submissionDetailsOfPosition() {
		log.info("Creating report with detailed submissions info "
				+ "of a given positions");
		// choose from the list of positions
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			// ver se é para usar... cuidado data null
			log.debug("Position "+position.getPositionCode());				
//			List<Object[]> list = report.reportCounting(null, null, "noperiod",
//					Constants.REPORT_POS_SUBMIPOS, null);
			// if 0, no submissions, no report!
		} else log.info("No position with id "+id);
	}

	// private methods
	private int summing(List<ReportItem> list) {
		// compute sum of all quantities
		int sum = 0;
		for(ReportItem item : list) {
			sum += item.getMeasure();
		}
		return sum;
	}

	private int longToInt(Long value) {
		return value.intValue();
	}

	// always look for info in the hole month if period is montly
	// and look for info in the hole year if period is year
	private void prepareDates() {
		if (period.equals(Constants.PERIOD_DAILY)) return;
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(d1);
		end.setTime(d2);
		
		if (period.equals(Constants.PERIOD_MONTHLY)) {
			// first day of the month of date 1 
			start.set(Calendar.DAY_OF_MONTH, 1);
			// last day of the month of date 2 
			end.set(Calendar.DAY_OF_MONTH, 
					end.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
			// first day of the year of date 1 
			start.set(Calendar.DAY_OF_YEAR, 1);
			// last day of the year of date 2 
			end.set(Calendar.DAY_OF_YEAR,
					end.getActualMaximum(Calendar.DAY_OF_YEAR));
			
		}
		
		// update dates
		d1 = start.getTime();
		d2 = end.getTime();

	}

	// private methods
	private int average(List<ReportItem> list) {
		// compute total average
		double avg = 0.0;
		int count = 0;
		for(ReportItem item : list) {
			avg += item.getMeasure();
			count++;
		}

		if (count != 0) return (int) Math.round(avg / count);
		return -1;
	}

	private char periodShort(String period) {
		char p = 'm';
		if (period != null && !period.isEmpty()) {
			long ndays = daysBetween(d1, d2);
			// if dates are no sorted, exchange them
			if (ndays < 0) {
				Date aux = d1;
				d1 = d2;
				d2 = aux;
			}

			// limit day for periods - aviso ao utilizador!!
			if (ndays > Constants.LIMITMONTH) p = Constants.YEARLY;
			else if (ndays > Constants.LIMITDAY && 
					period.equals(Constants.PERIOD_DAILY))
				p = Constants.MONTHLY;
			else p = period.toLowerCase().charAt(0);
		}
		return p;
	}

	private long daysBetween(Date d1, Date d2) {

		Calendar dateStartCal = Calendar.getInstance();
		Calendar dateEndCal = Calendar.getInstance();
		dateStartCal.setTime(d1);
		dateEndCal.setTime(d2);

		dateStartCal.set(Calendar.HOUR_OF_DAY, 0);
		dateStartCal.set(Calendar.MINUTE, 0);
		dateStartCal.set(Calendar.SECOND, 0);
		dateStartCal.set(Calendar.MILLISECOND, 0);

		dateEndCal.set(Calendar.HOUR_OF_DAY, 0);
		dateEndCal.set(Calendar.MINUTE, 0);
		dateEndCal.set(Calendar.SECOND, 0);
		dateEndCal.set(Calendar.MILLISECOND, 0);

		return (dateEndCal.getTimeInMillis() - 
				dateStartCal.getTimeInMillis()) / Constants.MSPERDAY;
	}

	private int doubleToInt(Double d) {
		return (int) Math.round(d);
	}

	// getters and setters

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

	public String getPeriodHeader() {
		return periodHeader;
	}

	public String getMeasureHeader() {
		return measureHeader;
	}

	public void setMeasureHeader(String measureHeader) {
		this.measureHeader = measureHeader;
	}

	public void setPeriodHeader(String header) {
		this.periodHeader = header;
	}

	public String getMeasureFooter() {
		return measureFooter;
	}

	public void setMeasureFooter(String measureFooter) {
		this.measureFooter = measureFooter;
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

	public List<ReportItem> getReport() {
		return report;
	}

	public void setReport(List<ReportItem> report) {
		this.report = report;
	}

}
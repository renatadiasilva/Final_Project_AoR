package pt.uc.dei.aor.pf.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;

import java.io.Serializable;
import java.math.BigInteger;

@Named
@SessionScoped
public class ReportsCDI implements Serializable {

	private static final long serialVersionUID = -8997358625403246665L;

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
	private String periodHeader;
	private String measureFooter;
	private String resultHeader = "Por resultado";
	private String totalResult;
	private List<ReportItem> report = new ArrayList<ReportItem>();
	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 

	// counts submission by position between two dates
	public void submissionsByPosition() {
		log.info("Creating report with number of submissions by position");

		tableHeader = "Número de candidaturas por posição (posições "
				+ "criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";

		List<Object[]> result = positionEJB.countSubmissionsByPosition(
				d1, d2);

		report.clear();
		for (Object[] ele : result)
			report.add(new ReportItem(positionEJB.find((Long) ele[0]),
					"", longToInt((Long) ele[1]), ""));

		//other headers
		totalResult = summing(report)+"";
	}

	// average time to be hired by period between two dates
	public void averageTimeToBeHired() {
		log.info("Creating report with average time to be hired");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// only periods monthly and yearly
		char p = periodShort();
		p = (p == Constants.DAILY)? p = Constants.MONTHLY : p;

		// text for tables
		if (p == Constants.MONTHLY) periodHeader = Constants.PERIOD_MHEADER;
		else periodHeader = Constants.PERIOD_YHEADER;
		prepareDates();
		tableHeader = "Tempo Médio para Contratação por "
				+periodHeader.substring(0, 3)+" (candidaturas "
				+"submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Tempo médio (em dias)";
		measureFooter = "Tempo médio total: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.averageTimeToHired(d1, d2, p);

		// extract date header and average times
		report.clear();
		for (Object[] o: list) {
			report.add(new ReportItem(null, makeDateHeader(p, o),
					doubleToInt((Double) o[0]), ""));
		}
		int avg = average(report);
		totalResult = (avg >= 0)? avg+"" : Constants.REPORT_NO_HIRED;

	}

	// candidate counts by period between two dates
	public void submissionCountResults() {
		
		log.info("Creating report with submission countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// all periods: daily, monthly, or yearly
		char p = periodShort();
		
		// text for tables
		switch(p) {
		case Constants.DAILY:
			periodHeader = Constants.PERIOD_DHEADER;
			break;
		case Constants.MONTHLY:
			periodHeader = Constants.PERIOD_MHEADER;
			break;
		case Constants.YEARLY:
			periodHeader = Constants.PERIOD_YHEADER;
			break;
		}
		prepareDates();
		tableHeader = "Número de candidaturas "
				+"submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Candidaturas submetidas";
		measureFooter = "Total Candidaturas: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				"", "");

		// extract date header and average times
		report.clear();
		for (Object[] o: list)
			report.add(new ReportItem(null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));
		
		// compute overall average
		totalResult = summing(report)+"";

	}

	// spontaneous submission counts by period between two dates
	public void spontaneousCountResults() {
		log.info("Creating report with spontaneous submissions");
		log.debug("From "+d1+" to "+d2+" with period "+period);		

		// all periods: daily, monthly, or yearly
		char p = periodShort();
		
		// text for tables
		switch(p) {
		case Constants.DAILY:
			periodHeader = Constants.PERIOD_DHEADER;
			break;
		case Constants.MONTHLY:
			periodHeader = Constants.PERIOD_MHEADER;
			break;
		case Constants.YEARLY:
			periodHeader = Constants.PERIOD_YHEADER;
			break;
		}
		prepareDates();
		tableHeader = "Número de Candidaturas Espontâneas "
				+"submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Candidaturas Espontâneas";
		measureFooter = "Total Candidaturas Espontâneas: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				"", " AND spontaneous = TRUE");

		// extract date header and average times
		report.clear();
		for (Object[] o: list)
			report.add(new ReportItem(null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));
		
		// compute overall average
		totalResult = summing(report)+"";
	}

	// reject submission counts/rejected reasons by period between two dates
	public void rejectedCountResults() {
		log.info("Creating report with reject candidates countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// all periods: daily, monthly, or yearly
		char p = periodShort();
		
		// text for tables
		switch(p) {
		case Constants.DAILY:
			periodHeader = Constants.PERIOD_DHEADER;
			break;
		case Constants.MONTHLY:
			periodHeader = Constants.PERIOD_MHEADER;
			break;
		case Constants.YEARLY:
			periodHeader = Constants.PERIOD_YHEADER;
			break;
		}
		prepareDates();
		tableHeader = "Número de Candidaturas Rejeitadas "
				+"entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Candidaturas Rejeitadas";
		measureFooter = "Total Candidaturas Rejeitadas: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				", rejected_reason", " AND rejected_reason IS NOT NULL");

		// extract date header and average times
		report.clear();
		int size = 0;
		if (list != null) size = list.get(0).length;
		for (Object[] o: list)
			report.add(new ReportItem(null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), (String) o[size-1]));
		
		// compute overall average
		totalResult = summing(report)+"";
	}

	// presented proposal counts/results by period between two dates
	public void proposalCountResults() {
		log.info("Creating report with presented proposal countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);		

		// all periods: daily, monthly, or yearly
		char p = periodShort();
		
		// text for tables
		switch(p) {
		case Constants.DAILY:
			periodHeader = Constants.PERIOD_DHEADER;
			break;
		case Constants.MONTHLY:
			periodHeader = Constants.PERIOD_MHEADER;
			break;
		case Constants.YEARLY:
			periodHeader = Constants.PERIOD_YHEADER;
			break;
		}
		prepareDates();
		tableHeader = "Número de Propostas Apresentadas "
				+"entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Propostas Apresentadas";
		measureFooter = "Total Propostas Apresentadas: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				", status", " AND proposal_date IS NOT NULL");

		// extract date header and average times
		report.clear();
		int size = 0;
		if (list != null) size = list.get(0).length;
		for (Object[] o: list)
			report.add(new ReportItem(null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), (String) o[size-1]));
		
		// compute overall average
		totalResult = summing(report)+"";
	}

	// complicado??
	// submission source counts by period between two dates (file?)
	public void sourceCount() {
//		d1 = ftDate.parse("2015-07-01");
//		d2 = ftDate.parse("2015-09-30");
		
		
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

		// all periods: daily, monthly, or yearly
		char p = periodShort();
		
		// text for tables
		switch(p) {
		case Constants.DAILY:
			periodHeader = Constants.PERIOD_DHEADER;
			break;
		case Constants.MONTHLY:
			periodHeader = Constants.PERIOD_MHEADER;
			break;
		case Constants.YEARLY:
			periodHeader = Constants.PERIOD_YHEADER;
			break;
		}
		prepareDates();
		tableHeader = "Número de contratações "
				+"entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Contratações";
		measureFooter = "Total Contratações: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				"", " AND hired_date IS NOT NULL");

		// extract date header and average times
		report.clear();
		for (Object[] o: list)
			report.add(new ReportItem(null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));
		
		// compute overall average
		totalResult = summing(report)+"";
	}

	// ver primeiro
	public void rejectedCountByPosition() {
		log.info("Creating report with number of rejected submissions"
				+ " by position");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		//		List<Object[]> list = report.reportCounting(d1, d2, period,
		//				Constants.REPORT_POS_REJECPOS, null);
		// if -1, no valid submissions, no report!	
	}

	// ver primeiro
	public void proposalCountByPosition() {
		log.info("Creating report with number of presented proposal"
				+ " by position");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		//		List<Object[]> list = report.reportCounting(d1, d2, period,
		//				Constants.REPORT_POS_PROPOPOS, null);
		// if -1, no valid submissions, no report!	
	}

	// ver segundo
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

	// amanhã
	// interview counts and results by period between two dates
	public void interviewCountResults() {
		log.info("Creating report with interview countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		//		List<Object[]> list = report.reportCounting(d1, d2, period,
		//				Constants.REPORT_INT_CNTINTER, null);
		// if 0, no interviews, no report!
	}

	// amanhã
	// average time to first interview by period between two dates (file?)
	public void averageTimeToInterview(Date d1, Date d2,
			String period) {
		log.info("Creating report with average time to first interview");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		//		List<Object[]> list = report.reportCounting(d1, d2, period,
		//				Constants.REPORT_INT_AVGINTER, null);
		// if -1, no valid submissions, no report!
	}

	// complicado??
	public void interviewDetailOfCandidate() {
		log.info("Creating report with detaild interview info of candidate");
		//		List<Object[]> list = report.reportCounting(null, null, "noperiod",
		//				Constants.REPORT_INT_INTCANDI, null);
		// if 0, no interviews, no report!
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

	private char periodShort() {
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
	
	private int bigIntToInt(BigInteger value) {
		return value.intValue();
	}
	
	private String makeDateHeader(char p, Object[] o) {
		String dateH = "";
		if (p == Constants.MONTHLY) { 
			Calendar cal = Calendar.getInstance();
			cal.set(2015, doubleToInt((Double) o[2])-1, 1);
			dateH = cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
					Locale.getDefault())+" ";
		}
		if (p == Constants.DAILY) dateH = ((Date) o[1])+"";
		else dateH += doubleToInt((Double) o[1]);
		return dateH;
	}
	
	public void report() {
		
		// all periods: daily, monthly, or yearly
		char p = periodShort();
		
		// text for tables
		switch(p) {
		case Constants.DAILY:
			periodHeader = Constants.PERIOD_DHEADER;
			break;
		case Constants.MONTHLY:
			periodHeader = Constants.PERIOD_MHEADER;
			break;
		case Constants.YEARLY:
			periodHeader = Constants.PERIOD_YHEADER;
			break;
		}
		prepareDates();
		tableHeader = "Número de candidaturas "
				+"submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Candidaturas submetidas";
		measureFooter = "Total Candidaturas: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				"", "");

		// extract date header and average times
		report.clear();
		for (Object[] o: list)
			report.add(new ReportItem(null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));
		
		// compute overall average
		totalResult = summing(report)+"";
		
	}

	// getters and setters

	public Date getD1() {
		if (d1 != null) return d1;
		return new Date();
	}

	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}

	public void setD1(Date d1) {
		this.d1 = d1;
	}

	public Date getD2() {
		if (d2 != null) return d2;
		return new Date();
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
package pt.uc.dei.aor.pf.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;

import java.io.Serializable;
import java.math.BigInteger;

@Named
@SessionScoped
public class ReportsCDI implements Serializable {

	private static final long serialVersionUID = -8997358625403246665L;

	private static final Logger log = 
			LoggerFactory.getLogger(ReportsCDI.class);

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private InterviewEJBInterface interviewEJB;

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

	// counting submissions by position between two dates
	public void submissionsByPosition() {
		log.info("Creating report with number of submissions by position");
		log.debug("From "+d1+" to "+d2);

		tableHeader = "Número de candidaturas por posição (posições "
				+ "criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Nº Candidaturas submetidas";
		measureFooter = "Total Candidaturas: ";

		List<Object[]> result = positionEJB.countSubmissionsByPosition(
				d1, d2);

		report.clear();
		for (Object[] ele : result)
			report.add(new ReportItem(positionEJB.find((Long) ele[0]), null,
					"", longToInt((Long) ele[1]), ""));

		totalResult = submissionEJB.countTotalSubmissionsPos(d1, d2)+"";
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

		List<Object[]> list = submissionEJB.averageTimeToHired(d1, d2, p);

		// extract date header and average times
		report.clear();
		for (Object[] o: list) {
			report.add(new ReportItem(null, null, makeDateHeader(p, o),
					doubleToInt((Double) o[0]), ""));
		}
		
		// compute overall average between date1 and date2
		int avg = (int) Math.round(
				submissionEJB.overallAverageTimeToHired(d1, d2));
		totalResult = (avg >= 0)? avg+"" : Constants.REPORT_NO_HIRED;

	}

	// submission countings by period between two dates
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

		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				"", "");

		// extract date header and average times
		report.clear();
		for (Object[] o: list)
			report.add(new ReportItem(null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));
		
		// compute overall average
		totalResult = submissionEJB.countTotalSubmissions(d1, d2)+"";

	}

	// spontaneous submission countings by period between two dates
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
			report.add(new ReportItem(null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));
		
		// compute overall average
		totalResult = ((Long) submissionEJB.countTotalSpontaneous(d1, d2))
				+"";
	}
	
//	public void printCandidateInfo(SubmissionEntity s, int index,
//			boolean printPosition) {
//		UserEntity cand = s.getCandidate();
//		System.out.print("\nCandidato "+ index+" ");
//		System.out.println("\nNome: "+cand.getFirstName()+" "
//				+cand.getLastName());					
//		UserInfoEntity info = cand.getUserInfo();
//		System.out.println("\nCidade: "+info.getCity()+", "
//				+info.getCountry());
//		System.out.println("\nCurso: "+info.getCourse()+" ("
//				+info.getSchool()+")");
//		System.out.println("\nContactos: "+cand.getEmail()+" "
//				+info.getMobilePhone()+" "+info.getHomePhone());
//
//		if (printPosition) {
//			PositionEntity pos = s.getPosition();
//			if (!s.isSpontaneous()) 
//				System.out.println("\n\nConcorreu à posição :"+pos.getTitle()+
//						" ("+pos.getPositionCode()+")"); //truncate title??
//			else { // spontaneous submission
//				if (pos != null) 
//					System.out.println("\n\nFoi associado à posição :"
//							+pos.getTitle()+" ("+pos.getPositionCode()+")");
//				else System.out.println("\n\nCandidatura espontânea");
//			}
//		}
//
//	} reject???


	// reject submission countings/rejected reasons by period between two dates
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
			report.add(new ReportItem(null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), (String) o[size-1]));
		
		// compute overall average
		totalResult = submissionEJB.countTotalRejected(d1, d2)+"";

	}

	// presented proposal countings/results by period between two dates
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
			report.add(new ReportItem(null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), (String) o[size-1]));
		
		// compute overall average
		totalResult = submissionEJB.countTotalProposals(d1, d2)+"";
	}

	// submission source countings by period between two dates (file?)
	public void sourceCount() {		
		
		log.info("Creating report with submissions source countings");
		log.debug("From "+d1+" to "+d2);
		List<String> sources = Arrays.asList(Constants.SOURCE_EXPRESSO,
				Constants.SOURCE_FACEBOOK, Constants.SOURCE_LINKEDIN, 
				Constants.SOURCE_NETEMPREGO); // more?
		int ns = 0;
		if (sources != null) ns = sources.size();
		List<Long> totalS = new ArrayList<Long>(ns);
		for (int i = 0; i < ns; i++) totalS.add(0L);
		int index = 0;
		// count submissions for each source of the list of sources
		for (String so : sources) {
			// get list of submissions by a source
			// of the day, month, or year
			List<SubmissionEntity> slist = 
					submissionEJB.findSubmissionsBySource(so, d1, d2);

			// count the number of submissions by a source
			// of the day, month, or year
			int n;
			if (slist != null) n = slist.size();
			else n = 0; // no submissions
//			counts.add(n);

			// update overall number of submissions by source
			Long value = totalS.get(index);
			totalS.set(index, value+n);
			index++;
			
			for(int i = ns-1; i >= 0; i--);
//				counts.add(0, totalS.get(i));

		}

//		"Número de Candidaturas por Fonte"
	}

	// hired people countings by period between two dates
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
			report.add(new ReportItem(null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));
		
		// compute overall average
		totalResult = submissionEJB.countTotalHired(d1, d2)+"";
	}

	// rejected submission countings by position between two dates
	public void rejectedCountByPosition() {
		log.info("Creating report with number of rejected submissions"
				+ " by position");
		log.debug("From "+d1+" to "+d2);

		tableHeader = "Número de candidaturas rejeitadas por posição"
				+ " (posições criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Nº Candidaturas rejeitadas";
		measureFooter = "Total Candidaturas rejeitadas: ";

		List<Object[]> result = positionEJB.countRejectedByPosition(
				d1, d2);

		report.clear();
		for (Object[] ele : result)
			report.add(new ReportItem(positionEJB.find((Long) ele[0]), null,
					"", longToInt((Long) ele[1]), ""));

		//other headers
		totalResult = submissionEJB.countTotalRejectedPos(d1, d2)+"";
	}

	// presented proposal countings by position between two dates
	public void proposalCountByPosition() {
		log.info("Creating report with number of presented proposal"
				+ " by position");
		log.debug("From "+d1+" to "+d2);

		tableHeader = "Número propostas apresentadas por posição (posições "
				+ "criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Nº Propostas Apresentadas";
		measureFooter = "Total Propostas Apresentadas: ";

		List<Object[]> result = positionEJB.countProposalsByPosition(d1, d2);

		report.clear();
		for (Object[] ele : result)
			report.add(new ReportItem(positionEJB.find((Long) ele[0]), null,
					"", longToInt((Long) ele[1]), ""));

		//other headers
		totalResult = submissionEJB.countTotalProposalsPos(d1, d2)+"";
	}

	// average time to close a position between two dates
	public void averageTimeToClose() {
		log.info("Creating report with average time to close a positions");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// only periods monthly and yearly
		char p = periodShort();
		p = (p == Constants.DAILY)? p = Constants.MONTHLY : p;

		// text for tables
		if (p == Constants.MONTHLY) periodHeader = Constants.PERIOD_MHEADER;
		else periodHeader = Constants.PERIOD_YHEADER;
		prepareDates();
		tableHeader = "Tempo Médio para fecho de uma posição por "
				+periodHeader.substring(0, 3)+" (posições "
				+"criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Tempo médio (em dias)";
		measureFooter = "Tempo médio total: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = positionEJB.averageTimeToClose(d1, d2, p);

		// extract date header and average times
		report.clear();
		for (Object[] o: list) {
			report.add(new ReportItem(null, null, makeDateHeader(p, o),
					doubleToInt((Double) o[0]), ""));
		}

		// compute overall average between date1 and date2
		int avg = (int) Math.round(
				positionEJB.overallAverageTimeToClose(d1, d2));
		totalResult = (avg >= 0)? avg+"" : Constants.REPORT_NO_HIRED;

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
		
//		"Detalhes de candidaturas da posição"
	}

	// interview countings/results by period between two dates
	public void interviewCountResults() {
		log.info("Creating report with interview countings");
		log.debug("From "+d1+" to "+d2);

		tableHeader = "Número de entrevistas "
				+"realizadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2);
		measureHeader = "Resultado";
		measureFooter = "Total Entrevistas: ";

		//Nota: só são apresentados resultados quando há candidaturas
		List<InterviewEntity> list = 
				interviewEJB.findCarriedOutInterviews(d1, d2);
				
		// extract date header and average times
		report.clear();
		for (InterviewEntity i: list)
			report.add(new ReportItem(null, i, "", 0, ""));
		
		// compute overall average
		totalResult = interviewEJB.findTotalCarriedOutInterviews(d1, d2)+"";

	}

	// average time to first interview by period between two dates (file?)
	public void averageTimeToInterview() {
		log.info("Creating report with average time to first interview");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// only periods monthly and yearly
		char p = periodShort();
		p = (p == Constants.DAILY)? p = Constants.MONTHLY : p;

		// text for tables
		if (p == Constants.MONTHLY) periodHeader = Constants.PERIOD_MHEADER;
		else periodHeader = Constants.PERIOD_YHEADER;
		prepareDates();
		tableHeader = "Tempo Médio para 1ª Entrevista por "
				+periodHeader.substring(0, 3)+" (candidaturas "
				+"submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Tempo médio (em dias)";
		measureFooter = "Tempo médio total: ";

		List<Object[]> list = 
				interviewEJB.averageTimeToFirstInterview(d1, d2, p);

		// extract date header and average times
		report.clear();
		for (Object[] o: list) {
			report.add(new ReportItem(null, null, makeDateHeader(p, o),
					doubleToInt((Double) o[0]), ""));
		}

		// compute overall average between date1 and date2
		int avg = (int) Math.round(
				interviewEJB.overallAverageTimeToFirstInterview(d1, d2));
		totalResult = (avg >= 0)? avg+"" : Constants.REPORT_NO_HIRED;

	}

	// complicado??
	public void interviewDetailOfCandidate() {
		log.info("Creating report with detaild interview info of candidate");
		//		List<Object[]> list = report.reportCounting(null, null, "noperiod",
		//				Constants.REPORT_INT_INTCANDI, null);
		// if 0, no interviews, no report!
//		"Detalhes de entrevistas do candidato"
	}

	// private methods
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
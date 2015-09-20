package pt.uc.dei.aor.pf.reports;

import java.util.ArrayList;
import java.util.Arrays;
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
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;

@Named
@RequestScoped
public class SubmissionReportsCDI {

	private static final Logger log = 
			LoggerFactory.getLogger(SubmissionReportsCDI.class);

	@Inject
	private ReportManager report;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	// data input fields
	private Date d1, d2;
	private Long id;
	private String period; // mudar?

	// results
	private String tableHeader;
	private Long totalResult;
	private String periodHeader;
	private List<SubmissionReport> sreports = new ArrayList<SubmissionReport>();
	private List<PositionReport> preports = new ArrayList<PositionReport>();
	private List<TimeReportItem> treport = new ArrayList<TimeReportItem>();

	// tirar (detalhar por período??)
	public void submissionsByPosition() {
		log.info("Creating report with number of submissions by position");

		tableHeader = "Número de Candidaturas por posição";
		//period
		List<Object[]> result = submissionEJB.countSubmissionsByPosition(
				d1, d2);
		List<PositionReportItem> resultItems =
				new ArrayList<PositionReportItem>();

		for (Object[] ele : result)
			resultItems.add(new PositionReportItem(
					positionEJB.find((Long) ele[0]), (Long) ele[1]));

		preports.add(new PositionReport(resultItems));

		//do better
		int sum = 0;
		for(PositionReportItem item : preports.get(0).getItems()) {
			sum += item.getCounting();
		}
		totalResult = report.intToLong(sum);
	}

	// average time to be hired by period between two dates
	public void averageTimeToBeHired() {
		log.info("Creating report with average time to be hired");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		periodHeader = "Tempo Médio para Contratação";
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_SUB_AVGHIRED, null);

		tableHeader = "Tempo Médio para Contratação";
		periodHeader = (String) list.get(0)[0];
		totalResult = (Long) list.get(0)[1];

		int n = list.size();
		for (int i = 1; i < n; i++) {
			Long ntime = (Long) list.get(i)[1];
			String time = (ntime >= 0)? ntime+"" : "Sem contratações"; 
			treport.add(new TimeReportItem((String) list.get(i)[0],
					time));
		}

	}

	// candidate counts by period between two dates (file?)
	public void submisssionCountResults() {
		log.info("Creating report with candidate countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_SUB_CNTSUBMI, null);
		// if 0, no candidates, no report!
	}

	// spontaneous submission counts by period between two dates (file?)
	public void spontaneousCountResults() {
		log.info("Creating report with spontaneous submissions");
		log.debug("From "+d1+" to "+d2+" with period "+period);		
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_SUB_CNTSPONT, null);
		// if 0, no spontaneous submissions, no report!
	}

	// reject submission counts/rejected reasons by period between two dates
	public void rejectedCountResults() {
		log.info("Creating report with reject candidates countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_SUB_CNTREJEC, null);
		// if 0, no rejected submitions, no report!
	}

	// presented proposal counts/results by period between two dates (file?)
	public void proposalCountResults() {
		log.info("Creating report with presented proposal countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);		
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_SUB_CNTPROPO, null);
		// if 0, no presented proposals, no report!
	}

	// submission source counts by period between two dates (file?)
	public void sourceCount() {
		log.info("Creating report with submissions source countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);
		List<String> sources = Arrays.asList(Constants.SOURCE_EXPRESSO,
				Constants.SOURCE_FACEBOOK, Constants.SOURCE_LINKEDIN, 
				Constants.SOURCE_NETEMPREGO); // more?
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_SUB_CNTSOURC, sources);
		// if 0, no submissions, no report!
	}

	public void hiredCountResuls() {
		log.info("Creating report with hired people countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);				
		List<Object[]> list = report.reportCounting(d1, d2, period,
				Constants.REPORT_SUB_CNTHIRED, null);
		// if 0, no submissions, no report!
	}

	public void submissionDetailsOfPosition() {
		log.info("Creating report with detailed submissions info "
				+ "of a given positions");
		// choose from the list of positions
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			// ver se é para usar... cuidado data null
			log.debug("Position "+position.getPositionCode());				
			List<Object[]> list = report.reportCounting(null, null, "noperiod",
					Constants.REPORT_POS_SUBMIPOS, null);
			// if 0, no submissions, no report!
		} else log.info("No position with id "+id);
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

	public String getPeriodHeader() {
		return periodHeader;
	}

	public void setPeriodHeader(String header) {
		this.periodHeader = header;
	}

	public List<PositionReport> getPreports() {
		return preports;
	}

	public void setPreports(List<PositionReport> preports) {
		this.preports = preports;
	}

	public String getTableHeader() {
		return tableHeader;
	}

	public void setTableHeader(String tableHeader) {
		this.tableHeader = tableHeader;
	}

	public Long getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(Long totalResult) {
		this.totalResult = totalResult;
	}

	public List<TimeReportItem> getTreport() {
		return treport;
	}

	public void setTreport(List<TimeReportItem> treport) {
		this.treport = treport;
	}

}
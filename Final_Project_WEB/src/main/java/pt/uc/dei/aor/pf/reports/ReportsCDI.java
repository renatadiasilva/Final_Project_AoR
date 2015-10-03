package pt.uc.dei.aor.pf.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

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

	@EJB
	private UserEJBInterface userEJB;

	// data input fields
	private Date d1, d2;
	private String period, keyword;
	private UserEntity candidate;
	private List<UserEntity> clist;
	private PositionEntity position;
	private List<PositionEntity> plist;

	// results
	private String measureHeader;
	private String tableHeader, fileTitle;
	private String periodHeader;
	private String measureFooter;
	private String totalResult;
	private String emptyMessage;
	private List<ReportItem> report;
	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 
	private SimpleDateFormat ftDateFile = new SimpleDateFormat ("yyyyMMdd"); 

	// rendered flags
	private boolean submissionPos;
	private boolean rejectedPos;
	private boolean proposalPos;

	private boolean interviewDetail;
	private boolean interviewChoose;

	private boolean timeToHired;
	private boolean submissionCount;
	private boolean spontaneousCount;
	private boolean hiredCount;
	private boolean timeToClose;
	private boolean timeTo1stInt;
	private boolean submissionsBySource;

	private boolean rejectSubm;

	private boolean positionDetail;
	private boolean positionChoose;

	// present table only after hiting button
	public boolean checkIfNotNull() {
		return report != null;
	}

	// clean all data when enter the page
	public void clean() {
		period = Constants.PERIOD_MONTHLY;
		measureHeader = tableHeader = periodHeader = measureFooter = "";
		totalResult = emptyMessage = "";
		report = null;
		d1 = d2 = new Date();
		candidate = null;
		position = null;
		keyword = "";
		if (interviewDetail) {
			interviewChoose = true;
			clist = getAllCandidates();
		}
		if (positionDetail) {
			positionChoose = true;
			plist = getAllClosedPositions();
		}
	}

	// counting submissions by position between two dates
	public void submissionsByPosition() {

		long ndays = daysBetween(d1, d2);
		sortDates(ndays);

		log.info("Creating report with number of submissions by position");
		log.debug("From "+d1+" to "+d2);

		fileTitle = "Candidaturas_posicao_"+ftDateFile.format(d1)+"_"
				+ftDateFile.format(d2);		
		tableHeader = "Número de candidaturas por posição (posições "
				+ "criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Nº Candidaturas submetidas";
		measureFooter = "Total Candidaturas: ";
		emptyMessage = "Sem candidaturas.";

		List<Object[]> result = positionEJB.countSubmissionsByPosition(
				d1, d2);

		report = new ArrayList<ReportItem>();
		for (Object[] ele : result)
			report.add(new ReportItem(positionEJB.find((Long) ele[0]), null,
					null, "", longToInt((Long) ele[1]), ""));

		totalResult = submissionEJB.countTotalSubmissionsPos(d1, d2)+"";
	}

	// average time to be hired by period between two dates
	public void averageTimeToBeHired() {
		log.info("Creating report with average time to be hired");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// only periods monthly and yearly
		long ndays = daysBetween(d1, d2);
		char p = periodShort(ndays);
		p = (p == Constants.DAILY)? p = Constants.MONTHLY : p;

		// text for tables
		if (p == Constants.MONTHLY) periodHeader = Constants.PERIOD_MHEADER;
		else periodHeader = Constants.PERIOD_YHEADER;
		prepareDates();
		tableHeader = "Tempo Médio para Contratação por "
				+periodHeader.substring(0, 3)+" (candidaturas "
				+"submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		fileTitle = "Tempo_medio_contratacao_"
				+periodHeader.substring(0, 1)+"_"+ftDateFile.format(d1)+"_"
				+ftDateFile.format(d2);
		measureHeader = "Tempo médio (em dias)";
		measureFooter = "Tempo médio total: ";
		emptyMessage = "Sem contratações.";

		List<Object[]> list = submissionEJB.averageTimeToHired(d1, d2, p);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		for (Object[] o: list) {
			report.add(new ReportItem(null, null, null, makeDateHeader(p, o),
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
		long ndays = daysBetween(d1, d2);
		char p = periodShort(ndays);

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
		fileTitle = "Candidaturas_total_"+periodHeader.substring(0, 1)
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		measureHeader = "Nº Candidaturas submetidas";
		measureFooter = "Total Candidaturas: ";
		emptyMessage = "Sem candidaturas.";

		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				"");

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		for (Object[] o: list)
			report.add(new ReportItem(null, null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));

		// compute overall average
		totalResult = submissionEJB.countTotalSubmissions(d1, d2)+"";

	}

	// spontaneous submission countings by period between two dates
	public void spontaneousCountResults() {
		log.info("Creating report with spontaneous submissions");
		log.debug("From "+d1+" to "+d2+" with period "+period);		

		// all periods: daily, monthly, or yearly
		long ndays = daysBetween(d1, d2);
		char p = periodShort(ndays);

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
		fileTitle = "Candidaturas_espontaneas_"+periodHeader.substring(0, 1)
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		tableHeader = "Número de Candidaturas Espontâneas "
				+"submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Candidaturas Espontâneas";
		measureFooter = "Total Candidaturas Espontâneas: ";
		emptyMessage = "Sem candidaturas espontâneas.";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				Constants.QUERY_SPONT);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		for (Object[] o: list)
			report.add(new ReportItem(null, null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));

		// compute overall average
		totalResult = ((Long) submissionEJB.countTotalSpontaneous(d1, d2))
				+"";
	}

	// reject submission countings/rejected reasons by period between two dates
	public void rejectedCountResults() {
		log.info("Creating report with reject candidates countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// all periods: daily, monthly, or yearly
		long ndays = daysBetween(d1, d2);
		char p = periodShort(ndays);

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
		fileTitle = "Candidaturas_rejeitadas_"+periodHeader.substring(0, 1)
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		tableHeader = "Número de Candidaturas Rejeitadas "
				+"entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Candidaturas Rejeitadas";
		measureFooter = "Total Candidaturas Rejeitadas: ";
		emptyMessage = "Sem candidaturas rejeitadas.";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				Constants.QUERY_REJEC);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		int size = 0;
		if (list != null && !list.isEmpty()) size = list.get(0).length;
		for (Object[] o: list)
			report.add(new ReportItem(null, null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), (String) o[size-1]));

		// compute overall average
		totalResult = submissionEJB.countTotalRejected(d1, d2)+"";

	}

	// presented proposal countings/results by period between two dates
	public void proposalCountResults() {
		log.info("Creating report with presented proposal countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);		

		// all periods: daily, monthly, or yearly
		long ndays = daysBetween(d1, d2);
		char p = periodShort(ndays);

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
		fileTitle = "Propostas_apresentadas_"+periodHeader.substring(0, 1)
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		tableHeader = "Número de Propostas Apresentadas "
				+"entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Propostas Apresentadas";
		measureFooter = "Total Propostas Apresentadas: ";
		emptyMessage = "Sem propostas apresentadas.";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				Constants.QUERY_PROPO);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		int size = 0;
		if (list != null && !list.isEmpty()) size = list.get(0).length;
		for (Object[] o: list)
			report.add(new ReportItem(null, null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), (String) o[size-1]));

		// compute overall average
		totalResult = submissionEJB.countTotalProposals(d1, d2)+"";
	}

	// submission source countings by period between two dates (file?)
	public void sourceCount() {		

		log.info("Creating report with submissions source countings");
		log.debug("From "+d1+" to "+d2);

		long ndays = daysBetween(d1, d2);
		sortDates(ndays);

		fileTitle = "Candidaturas_fonte_"
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		tableHeader = "Número de Candidaturas por Fonte "
				+"(submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Nº Candidaturas";
		measureFooter = "Total Candidaturas do período"
				+ " (cada candidatura pode"
				+ " ter várias fontes): ";
		emptyMessage = "Nada a apresentar.";

		List<Object[]> list = submissionEJB.countSubmissionsBySourceTable(d1,
				d2, Constants.ALL_SOURCES);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		for (Object[] o: list)
			report.add(new ReportItem(null, null, null, (String) o[0],
					bigIntToInt((BigInteger) o[1]), ""));

		// compute overall average
		totalResult = submissionEJB.countTotalSubmissions(d1, d2)+"";

	}

	// hired people countings by period between two dates
	public void hiredCountResuls() {
		log.info("Creating report with hired people countings");
		log.debug("From "+d1+" to "+d2+" with period "+period);				

		// all periods: daily, monthly, or yearly
		long ndays = daysBetween(d1, d2);
		char p = periodShort(ndays);

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
		fileTitle = "Contratacoes_"+periodHeader.substring(0, 1)
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		tableHeader = "Número de contratações "
				+"entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+" (por "
				+periodHeader.substring(0, 3)+")";
		measureHeader = "Nº Contratações";
		measureFooter = "Total Contratações: ";
		emptyMessage = "Sem contratações.";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = submissionEJB.countSubmissionsByDate(d1, d2, p,
				Constants.QUERY_HIRED);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		for (Object[] o: list)
			report.add(new ReportItem(null, null, null, makeDateHeader(p, o),
					bigIntToInt((BigInteger) o[0]), ""));

		// compute overall average
		totalResult = submissionEJB.countTotalHired(d1, d2)+"";
	}

	// rejected submission countings by position between two dates
	public void rejectedCountByPosition() {
		log.info("Creating report with number of rejected submissions"
				+ " by position");
		log.debug("From "+d1+" to "+d2);

		long ndays = daysBetween(d1, d2);
		sortDates(ndays);

		tableHeader = "Número de candidaturas rejeitadas por posição"
				+ " (posições criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		fileTitle = "Candidaturas_rejeitadas_posicao"+periodHeader.substring(0, 1)
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		measureHeader = "Nº Candidaturas rejeitadas";
		measureFooter = "Total Candidaturas rejeitadas: ";
		emptyMessage = "Sem candidaturas rejeitadas.";

		List<Object[]> result = positionEJB.countRejectedByPosition(
				d1, d2);

		report = new ArrayList<ReportItem>();
		for (Object[] ele : result)
			report.add(new ReportItem(positionEJB.find((Long) ele[0]), null,
					null, "", longToInt((Long) ele[1]), ""));

		//other headers
		totalResult = submissionEJB.countTotalRejectedPos(d1, d2)+"";
	}

	// presented proposal countings by position between two dates
	public void proposalCountByPosition() {
		log.info("Creating report with number of presented proposal"
				+ " by position");
		log.debug("From "+d1+" to "+d2);

		long ndays = daysBetween(d1, d2);
		sortDates(ndays);

		fileTitle = "Propostas_apresentadas_posicao"+periodHeader.substring(0, 1)
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		tableHeader = "Número propostas apresentadas por posição (posições "
				+ "criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Nº Propostas Apresentadas";
		measureFooter = "Total Propostas Apresentadas: ";
		emptyMessage = "Sem propostas apresentadas.";

		List<Object[]> result = positionEJB.countProposalsByPosition(d1, d2);

		report = new ArrayList<ReportItem>();
		for (Object[] ele : result)
			report.add(new ReportItem(positionEJB.find((Long) ele[0]), null,
					null, "", longToInt((Long) ele[1]), ""));

		//other headers
		totalResult = submissionEJB.countTotalProposalsPos(d1, d2)+"";
	}

	// average time to close a position between two dates
	public void averageTimeToClose() {
		log.info("Creating report with average time to close a positions");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// only periods monthly and yearly
		long ndays = daysBetween(d1, d2);
		char p = periodShort(ndays);
		p = (p == Constants.DAILY)? p = Constants.MONTHLY : p;

		// text for tables
		if (p == Constants.MONTHLY) periodHeader = Constants.PERIOD_MHEADER;
		else periodHeader = Constants.PERIOD_YHEADER;
		prepareDates();
		tableHeader = "Tempo Médio para fecho de uma posição por "
				+periodHeader.substring(0, 3)+" (posições "
				+"criadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		fileTitle = "Tempo_medio_fecho_"
				+periodHeader.substring(0, 1)+"_"+ftDateFile.format(d1)+"_"
				+ftDateFile.format(d2);
		measureHeader = "Tempo médio (em dias)";
		measureFooter = "Tempo médio total: ";
		emptyMessage = "Nada a apresentar.";

		//Nota: só são apresentados resultados quando há candidaturas
		List<Object[]> list = positionEJB.averageTimeToClose(d1, d2, p);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		for (Object[] o: list) {
			report.add(new ReportItem(null, null, null, makeDateHeader(p, o),
					doubleToInt((Double) o[0]), ""));
		}

		// compute overall average between date1 and date2
		int avg = (int) Math.round(
				positionEJB.overallAverageTimeToClose(d1, d2));
		totalResult = (avg >= 0)? avg+"" : Constants.REPORT_NO_HIRED;

	}

	// print details of submissions of a given closed position
	public void submissionDetailsOfPosition() {
		log.info("Creating report with detailed submissions info "
				+ "of a given closed position");

		// choose from the list of positions
		if (position != null) {
			if (position.getStatus().equals(Constants.STATUS_CLOSED)) {
				log.debug("Position "+position.getPositionCode());				

				tableHeader = "Detalhes de candidaturas da posição "
						+position.getPositionCode()+" (aberta de "
						+ftDate.format(position.getOpeningDate())
						+" até "+ftDate.format(position.getClosingDate())+")";
				fileTitle = "Detalhes_posicao_"+position.getPositionCode()
						+ftDateFile.format(position.getOpeningDate())
						+" até "+ftDateFile.format(position.getClosingDate());
				measureFooter = "Total Candidaturas: ";
				emptyMessage = "Sem candidaturas.";

				List<SubmissionEntity> list = 
						submissionEJB.findDetailOfPosition(position);

				report = new ArrayList<ReportItem>();
				for (SubmissionEntity s: list) {
					report.add(new ReportItem(null, null, s, " ", 0, ""));
				}

				// compute overall submissions of position
				totalResult = list.size()+"";

				//hide position table
				positionChoose = false;

			} else log.info("Error: the chosen closed position"
					+ " is not closed?!?!");
		} else {
			errorMessage("Escolha uma posição fechada");
			log.info("No chosen closed position");
		}

	}

	// interview countings/results by period between two dates
	public void interviewCountResults() {
		log.info("Creating report with interview countings");
		log.debug("From "+d1+" to "+d2);

		long ndays = daysBetween(d1, d2);
		sortDates(ndays);

		tableHeader = "Número de entrevistas "
				+"realizadas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2);
		fileTitle = "Entrevistas_realizadas_"
				+ftDateFile.format(d1)+"_"+ftDateFile.format(d2);
		measureHeader = "Resultado";
		measureFooter = "Total Entrevistas: ";
		emptyMessage = "Sem entrevistas.";

		//Nota: só são apresentados resultados quando há candidaturas
		List<InterviewEntity> list = 
				interviewEJB.findCarriedOutInterviews(d1, d2);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		for (InterviewEntity i: list)
			report.add(new ReportItem(null, i, null, "", 0, ""));

		// compute overall average
		totalResult = list.size()+"";

	}

	// average time to first interview by period between two dates
	public void averageTimeToInterview() {
		log.info("Creating report with average time to first interview");
		log.debug("From "+d1+" to "+d2+" with period "+period);

		// only periods monthly and yearly
		long ndays = daysBetween(d1, d2);
		char p = periodShort(ndays);
		p = (p == Constants.DAILY)? p = Constants.MONTHLY : p;

		// text for tables
		if (p == Constants.MONTHLY) periodHeader = Constants.PERIOD_MHEADER;
		else periodHeader = Constants.PERIOD_YHEADER;
		prepareDates();
		fileTitle = "Tempo_medio_entrevista_"
				+periodHeader.substring(0, 1)+"_"+ftDateFile.format(d1)+"_"
				+ftDateFile.format(d2);
		tableHeader = "Tempo Médio para 1ª Entrevista por "
				+periodHeader.substring(0, 3)+" (candidaturas "
				+"submetidas entre "+ftDate.format(d1)+" e "
				+ftDate.format(d2)+")";
		measureHeader = "Tempo médio (em dias)";
		measureFooter = "Tempo médio total: ";
		emptyMessage = "Nada a apresentar.";

		List<Object[]> list = 
				interviewEJB.averageTimeToFirstInterview(d1, d2, p);

		// extract date header and average times
		report = new ArrayList<ReportItem>();
		for (Object[] o: list) {
			report.add(new ReportItem(null, null, null, makeDateHeader(p, o),
					doubleToInt((Double) o[0]), ""));
		}

		// compute overall average between date1 and date2
		int avg = (int) Math.round(
				interviewEJB.overallAverageTimeToFirstInterview(d1, d2));
		totalResult = (avg >= 0)? avg+"" : Constants.REPORT_NO_HIRED;

	}

	// print past interviews of candidate
	public void interviewDetailOfCandidate() {
		log.info("Creating report with detaild interview info of candidate");

		// choose from the list of candidates
		if (this.candidate!=null) {
			if (candidate.getRoles().contains(Constants.ROLE_CANDIDATE)) {
				log.debug("Candidate "+candidate.getFirstName()+" "
						+candidate.getLastName());				

				tableHeader = "Detalhes de entrevistas feitas ao candidato "
						+candidate.getFirstName()+" "
						+candidate.getLastName()+" ("+candidate.getEmail()+")";
				fileTitle = "Detalhes_entrevistas_"
						+candidate.getFirstName()+""
						+candidate.getLastName();
				measureHeader = "Resultado";
				measureFooter = "Total Entrevistas: ";
				emptyMessage = "Sem entrevistas.";

				List<InterviewEntity> list = 
						interviewEJB.findCarriedOutInterviewsByCandidate(candidate);

				report = new ArrayList<ReportItem>();
				for (InterviewEntity i: list) {
					report.add(new ReportItem(null, i, null, " ", 0, ""));
				}

				// compute overall interviews of candidate
				totalResult = list.size()+"";

				//hide candidate table
				interviewChoose = false;
			} else 
				log.info("Error: the chosen candidate is not a candidate?!?!");
		} else {
			errorMessage("Escolha um candidato");
			log.info("No chosen candidate");
		}
	}

	// redirects

	public String returnToReportsHome() {
		return "CreateReport.xhtml?faces-redirect=true";
	}

	public String goToReportSubByPos() {
		submissionPos = true;
		rejectedPos = proposalPos = false;
		return "PositionReports.xhtml?faces-redirect=true";
	}

	public String goToReportRejByPos() {
		rejectedPos = true;
		submissionPos = proposalPos = false;
		return "PositionReports?faces-redirect=true";
	}

	public String goToReportProByPos() {
		proposalPos = true;
		submissionPos = rejectedPos = false;
		return "PositionReports?faces-redirect=true";
	}

	public String goToReportIntCount() {
		interviewDetail = false;
		return "InterviewReports?faces-redirect=true";
	}

	public String goToReportIntDetail() {
		interviewDetail = true;
		return "InterviewReports?faces-redirect=true";
	}

	public String goToTimeToHired() {
		timeToHired = true;
		submissionCount = spontaneousCount = hiredCount = false;
		timeToClose = timeTo1stInt = submissionsBySource = false;
		return "NumberReports?faces-redirect=true";
	}

	public String goToSubCnt() {
		submissionCount = true;
		timeToHired = spontaneousCount = hiredCount = false;
		timeToClose = timeTo1stInt = submissionsBySource = false;
		return "NumberReports?faces-redirect=true";
	}

	public String goToSponCnt() {
		spontaneousCount = true;
		timeToHired = submissionCount = hiredCount = false;
		timeToClose = timeTo1stInt = submissionsBySource = false;
		return "NumberReports?faces-redirect=true";
	}

	public String goToHiredCnt() {
		hiredCount = true;
		timeToHired = spontaneousCount = submissionCount = false;
		timeToClose = timeTo1stInt = submissionsBySource = false;
		return "NumberReports?faces-redirect=true";
	}

	public String goToTimeToClose() {
		timeToClose = true;
		timeToHired = spontaneousCount = hiredCount = false;
		submissionCount = timeTo1stInt = submissionsBySource = false;
		return "NumberReports?faces-redirect=true";
	}

	public String goToTimeTo1stInt() {
		timeTo1stInt = true;
		timeToHired = spontaneousCount = hiredCount = false;
		timeToClose = submissionCount = submissionsBySource = false;
		return "NumberReports?faces-redirect=true";
	}

	public String goToSubBySource() {
		submissionsBySource = true;
		timeToHired = spontaneousCount = hiredCount = false;
		timeToClose = timeTo1stInt = submissionCount = false;
		return "NumberReports?faces-redirect=true";
	}

	public String goToReportRejectSub() {
		rejectSubm = true;
		return "ResultReports?faces-redirect=true";
	}

	public String goToReportPropoSub() {
		rejectSubm = false;
		return "ResultReports?faces-redirect=true";
	}

	public String goToReportPosDetail() {
		positionDetail = true;
		return "SubmissionReports?faces-redirect=true";

	}

	// methods for interview reports

	public List<UserEntity> getAllCandidates() {
		log.info("Listing all candidates");
		return this.userEJB.findAllCandidates();
	}

	public void getCandidatesByKeyword() {
		log.info("Listing candidates by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.clist = userEJB.findCandidatesByKeywordShort(pattern);
	}

	public boolean checkCandidate(UserEntity candidate){
		if(this.candidate==null)return false;
		if(this.candidate.getId()==candidate.getId())return true;
		return false;
	}

	public boolean interviewDetailStart() {
		return interviewDetail && interviewChoose;
	}

	public boolean interviewDetailEnd() {
		return interviewDetail && !interviewChoose;
	}

	public void returnInterviewDetail() {
		interviewChoose = true;
		clist = getAllCandidates();
		report = null;
		candidate = null;
		keyword = "";
	}

	// methods for numbers report

	public boolean averageTime() {
		return timeTo1stInt || timeToClose || timeToHired;
	}

	// methods for position detail

	public List<PositionEntity> getAllClosedPositions() {
		log.info("Listing all closed positions");
		return this.positionEJB.findClosedPositions();
	}


	public void getClosedPositionsByKeyword() {
		log.info("Listing position by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByKeywordShort(pattern, 
				Constants.STATUS_CLOSED);
	}

	public boolean checkPosition(PositionEntity position){
		if(this.position==null)return false;
		if(this.position.getId()==position.getId())return true;
		return false;
	}

	public boolean positionDetailStart() {
		return positionDetail && positionChoose;
	}

	public boolean positionDetailEnd() {
		return positionDetail && !positionChoose;
	}

	public void returnPositionDetail() {
		positionChoose = true;
		plist = getAllClosedPositions();
		report = null;
		position = null;
		keyword = "";
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

	private char periodShort(long ndays) {
		char p = 'm';
		if (period != null && !period.isEmpty()) {
			// if dates are no sorted, exchange them
			sortDates(ndays);

			p = period.toLowerCase().charAt(0);
		}
		return p;
	}

	private void sortDates(long ndays) {
		// if dates are no sorted, exchange them
		if (ndays < 0) {
			Date aux = d1;
			d1 = d2;
			d2 = aux;
		}		
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
		if (p == Constants.DAILY) {
			dateH = ftDate.format((Date) o[1]);
		} else dateH += doubleToInt((Double) o[1]);
		return dateH;
	}

	private void errorMessage(String message){
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}
	
	// getters and setters

	public Date getD1() {
		if (d1 != null) return d1;
		return new Date();
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

	public boolean isSubmissionPos() {
		return submissionPos;
	}

	public void setSubmissionPos(boolean submissionPos) {
		this.submissionPos = submissionPos;
	}

	public boolean isRejectedPos() {
		return rejectedPos;
	}

	public void setRejectedPos(boolean rejectedPos) {
		this.rejectedPos = rejectedPos;
	}

	public boolean isProposalPos() {
		return proposalPos;
	}

	public void setProposalPos(boolean proposalPos) {
		this.proposalPos = proposalPos;
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

	public String getEmptyMessage() {
		return emptyMessage;
	}

	public void setEmptyMessage(String emptyMessage) {
		this.emptyMessage = emptyMessage;
	}

	public UserEntity getCandidate() {
		return candidate;
	}

	public void setCandidate(UserEntity candidate) {
		this.candidate = candidate;
	}

	public boolean isInterviewDetail() {
		return interviewDetail;
	}

	public void setInterviewDetail(boolean interviewDetail) {
		this.interviewDetail = interviewDetail;
	}	

	public boolean isInterviewChoose() {
		return interviewChoose;
	}

	public void setInterviewChoose(boolean interviewChoose) {
		this.interviewChoose = interviewChoose;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<UserEntity> getClist() {
		return clist;
	}

	public void setClist(List<UserEntity> clist) {
		this.clist = clist;
	}

	public boolean isTimeToHired() {
		return timeToHired;
	}

	public void setTimeToHired(boolean timeToHired) {
		this.timeToHired = timeToHired;
	}

	public boolean isSubmissionCount() {
		return submissionCount;
	}

	public void setSubmissionCount(boolean submissionCount) {
		this.submissionCount = submissionCount;
	}

	public boolean isSpontaneousCount() {
		return spontaneousCount;
	}

	public void setSpontaneousCount(boolean spontaneousCount) {
		this.spontaneousCount = spontaneousCount;
	}

	public boolean isHiredCount() {
		return hiredCount;
	}

	public void setHiredCount(boolean hiredCount) {
		this.hiredCount = hiredCount;
	}

	public boolean isTimeToClose() {
		return timeToClose;
	}

	public void setTimeToClose(boolean timeToClose) {
		this.timeToClose = timeToClose;
	}

	public boolean isTimeTo1stInt() {
		return timeTo1stInt;
	}

	public void setTimeTo1stInt(boolean timeTo1stInt) {
		this.timeTo1stInt = timeTo1stInt;
	}

	public boolean isSubmissionsBySource() {
		return submissionsBySource;
	}

	public void setSubmissionsBySource(boolean submissionsBySource) {
		this.submissionsBySource = submissionsBySource;
	}

	public String daily() {
		return Constants.PERIOD_DAILY;
	}

	public String monthly() {
		return Constants.PERIOD_MONTHLY;
	}

	public String yearly() {
		return Constants.PERIOD_YEARLY;
	}

	public boolean isRejectSubm() {
		return rejectSubm;
	}

	public void setRejectSubm(boolean rejectSubm) {
		this.rejectSubm = rejectSubm;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public List<PositionEntity> getPlist() {
		return plist;
	}

	public void setPlist(List<PositionEntity> plist) {
		this.plist = plist;
	}

	public boolean isPositionChoose() {
		return positionChoose;
	}

	public void setPositionChoose(boolean positionChoose) {
		this.positionChoose = positionChoose;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

}
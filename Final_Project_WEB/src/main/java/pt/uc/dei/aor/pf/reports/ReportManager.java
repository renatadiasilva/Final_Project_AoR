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




import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Named
@RequestScoped
public class ReportManager {

	private static final Logger log = 
			LoggerFactory.getLogger(ReportManager.class);

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	// produces statistics by period between two dates
	public List<Object[]> reportCounting(Date d1, Date d2, String periodType,
			String reportType, List<String> sources) {

		if (reportType == null || reportType.isEmpty()) {
			log.error("ReportType is null.");
			return null;
		}

		if (periodType == null || periodType.isEmpty())
			periodType = Constants.PERIOD_MONTHLY;

		String header = " ";
		// different periods (daily -> d, monthly -> m, yearly -> y)
		char period = ' ';
		period = periodType.toLowerCase().charAt(0);

//		// different reports
		int report = 0;
//		--if (reportType.equals(Constants.REPORT_INT_CNTINTER)) report = 1;
//		--if (reportType.equals(Constants.REPORT_INT_AVGINTER)) report = 2;
//		--if (reportType.equals(Constants.REPORT_SUB_AVGHIRED)) report = 3;
//		--if (reportType.equals(Constants.REPORT_SUB_CNTSUBMI)) report = 4;
//		--if (reportType.equals(Constants.REPORT_SUB_CNTSPONT)) report = 5;
//		--if (reportType.equals(Constants.REPORT_SUB_CNTREJEC)) report = 6;
//		--if (reportType.equals(Constants.REPORT_SUB_CNTPROPO)) report = 7;
//		--if (reportType.equals(Constants.REPORT_SUB_CNTSOURC)) report = 8;
//
//		//new? TO IMPLEMENT
//		--if (reportType.equals(Constants.REPORT_SUB_CNTHIRED)) report = 9;
//		--if (reportType.equals(Constants.REPORT_INT_INTCANDI)) report = 10;
//		--if (reportType.equals(Constants.REPORT_POS_AVGCLOSE)) report = 11;
//		--if (reportType.equals(Constants.REPORT_POS_SUBMIPOS)) report = 12;
//		--if (reportType.equals(Constants.REPORT_POS_PROPOPOS)) report = 13;
//		--if (reportType.equals(Constants.REPORT_POS_REJECPOS)) report = 14;
//
		//tirar depois
		// different report headers
		switch (report) {
		case 1: header = "Número de Entrevistas: "; break;
		case 2: header = "Tempo Médio para Entrevista"; break;
		case 3: header = "Tempo Médio para Contratação"; break;
		case 4: header = "Número de Candidaturas";
		case 5: header = "Número de Candidaturas Espontâneas"; break;
		case 6: header = "Número de Candidaturas Rejeitadas"; break;
		case 7: header = "Número de Propostas Apresentadas"; break;
		case 8: header = "Número de Candidaturas por Fonte"; break;
		case 9: header = "Número de contratações"; break;
		case 10: header = "Detalhes de entrevistas do candidato"; break;
		// escrever nome do candidato
		case 11: header = "Tempo médio para Fecho de Posição"; break;
		case 12: header = "Número de Candidaturas por posição"; break;
		// escrever código da posição
		case 13: header = "Número de Propostas Apresentadas por posição"; break;
		// escrever código da posição
		case 14: header = "Número de Candidaturas Rejeitadas por posição"; break;
		// escrever código da posição
		default: 
			log.error("The reportType is not recognizable: "+reportType);
			return null;
		}

		// convert to calendar to easily manipulate
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.setTime(d1);
		endDate.setTime(d2);

		long ndays = daysBetween(startDate, endDate);
		// if dates are no sorted, exchange them
		if (ndays < 0) {
			Calendar aux = startDate;
			startDate = endDate;
			endDate = aux;
			ndays = -ndays;
		}

		// limit day for periods - aviso ao utilizador!!
		if (ndays > Constants.LIMITMONTH) period = Constants.YEARLY;
		else if ( ndays > Constants.LIMITDAY && period == Constants.DAILY)
			period = Constants.MONTHLY;

		Long n = 0L;
		List<Long> counts = new ArrayList<Long>();
		List<String> headers = new ArrayList<String>();

		//verificações fora
		switch (period) {
		case Constants.DAILY: headers.add("Dia"); break;
		case Constants.MONTHLY: headers.add("Mês"); break;
		case Constants.YEARLY: headers.add("Ano"); break;
		default:
		}
		switch (period) {
		case Constants.YEARLY:
			startDate.set(Calendar.MONTH, 0);
			endDate.set(Calendar.MONTH, 0);
		case Constants.MONTHLY:
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			endDate.set(Calendar.DAY_OF_MONTH, 1);
		}

		Long total = 0L;
		Long countTotal = 0L;
		double avg;
		int ns = 0;
		if (sources != null) ns = sources.size();
		List<Long> totalS = new ArrayList<Long>(ns);
		for (int i = 0; i < ns; i++) totalS.add(0L);

		// auxiliary lists
		List<InterviewEntity> ilist = new ArrayList<InterviewEntity>();
		//		List<PositionEntity> plist = new ArrayList<PositionEntity>();
		List<SubmissionEntity> slist = new ArrayList<SubmissionEntity>();

		// each WHILE iteration corresponds to
		// a single day, month, or year between d1 and d2
		while (!startDate.after(endDate)) {


			// compute temporary end date
			Calendar interDate = Calendar.getInstance();
			switch (period) {
			case Constants.DAILY: 
				// same day
				interDate.setTime(startDate.getTime());
				break;
			case Constants.MONTHLY:
				// last day in the current month 
				interDate.set(Calendar.DAY_OF_MONTH, 
						startDate.getActualMaximum(Calendar.DAY_OF_MONTH));
				interDate.set(Calendar.MONTH, 
						startDate.get(Calendar.MONTH));
				interDate.set(Calendar.YEAR, 
						startDate.get(Calendar.YEAR));
				break;
			case Constants.YEARLY:
				// last day in the current year (December, 31)
				interDate.set(Calendar.DAY_OF_MONTH, 31);
				interDate.set(Calendar.MONTH, 11);
				interDate.set(Calendar.YEAR, startDate.get(Calendar.YEAR));
				break;
			}

			switch (report) {
			case 1:
				// get list of carried out interviews of the day, month, or year
				ilist = interviewEJB.findCarriedOutInterviews(
						startDate.getTime(), interDate.getTime());

				// count the number of interviews of the day, month, or year
				if (ilist != null) n = intToLong(ilist.size());
				else n = 0L; // no interviews
				counts.add(n);

				// update overall number of interviews
				total += n;
				break;
			case 2:
				// get list of submissions of the day, month, or year
				slist = submissionEJB.findSubmissionsByDate(
						startDate.getTime(), interDate.getTime());

				// compute the average time to first interview 
				// (ignore submission without carried out interview)
				int countS = 0;
				avg = 0.0;
				for (SubmissionEntity s : slist) {
					// interviews?? queries???
					List<InterviewEntity> listSI = s.getInterviews();

					// submission has interviews
					if ( (listSI != null) && !listSI.isEmpty()) {

						// keep tomorrow date for comparisons
						Calendar tomorrow = Calendar.getInstance(); // today
						// day plus one: tomorrow
						tomorrow.add(Calendar.DAY_OF_YEAR, 1);

						// set initial older date as tomorrow (worse case)
						Calendar firstI = tomorrow;

						// find the first carried out interview of 
						// this submission, if any
						Calendar cDate = Calendar.getInstance();
						for (InterviewEntity i : listSI) {
							if (i.isCarriedOut()) {
								// date of interview
								cDate.setTime(i.getDate());

								// if interview date before older update
								if (cDate.before(firstI)) {
									firstI.setTime(cDate.getTime());
								}
							}
						}

						// submission has carried out interviews, compute time
						if (firstI.before(tomorrow)) {

							Date dS = s.getDate();
							// convert to calendar to easily manipulate
							cDate.setTime(dS);

							ndays = daysBetween(cDate, firstI);
							if (ndays < 0)  {
								// erro, datas não ordenadas
							}
							avg += ndays;
							countS++;

							// countings for overall average time
							total += ndays;
							countTotal++;
						}
					}
				}

				// average time of the day, month, or year
				if (countS != 0) avg /= countS;
				else avg = -1; // no submissions to compute average
				n = (Long) Math.round(avg);
				counts.add(n);
				break;
			case 3:
			case 4:
			case 5:
			case 6:
				// get list of rejected submissions of the day, month, or year
				slist = submissionEJB.findRejectedSubmissions(
						startDate.getTime(), interDate.getTime());

				// count the number of reject submissions
				// of the day, month, or year
				if (slist != null) n = intToLong(slist.size());
				else n = 0L; // no submissions
				counts.add(n);

				// update overall number of submissions
				total += n;
				break;
			case 7: // está mal pois a data é da candidatura e não da proposta!
				// get list of presented proposal of the day, month, or year
				slist = submissionEJB.findPresentedProposals(
						startDate.getTime(), interDate.getTime());

				// count the number of presented proposals
				// of the day, month, or year
				if (slist != null) n = intToLong(slist.size());
				else n = 0L; // no submissions
				counts.add(n);

				// update overall number of submissions
				total += n;
				break;
			case 8:
				int index = 0;
				// count submissions for each source of the list of sources
				for (String so : sources) {
					// get list of submissions by a source
					// of the day, month, or year
					slist = submissionEJB.findSubmissionsBySource(so,
							startDate.getTime(), interDate.getTime());

					// count the number of submissions by a source
					// of the day, month, or year
					if (slist != null) n = intToLong(slist.size());
					else n = 0L; // no submissions
					counts.add(n);

					// update overall number of submissions by source
					Long value = totalS.get(index);
					totalS.set(index, value+n);
					index++;
				}
				break;
			}

			// cabeçalho!!!
			switch (period) {
			case Constants.DAILY:
				String m = startDate.getDisplayName(Calendar.MONTH,
						Calendar.LONG, Locale.getDefault());
				headers.add(startDate.get(Calendar.DAY_OF_MONTH)+" "+m+" "
						+startDate.get(Calendar.YEAR));
				break;
			case Constants.MONTHLY:
				m = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG,
						Locale.getDefault());
				headers.add(m+" "+startDate.get(Calendar.YEAR));
				break;
			case Constants.YEARLY:
				headers.add(""+startDate.get(Calendar.YEAR));
				break;
			}

			//print more information
			switch (report) {
			case 1:
				// print info/results of interviews of the day, month, or year
				for (InterviewEntity i : ilist) {
					// PDF file
					System.out.println("\n\nEntrevista "+ ilist.indexOf(i));
					System.out.println("\nData: "+i.getDate());
					System.out.print("\nEntrevistadores: ");
					for (UserEntity u : i.getInterviewers())
						System.out.println(u.getFirstName()+" "+
								u.getLastName()+", ");
					System.out.print("\nCandidato: ");
					System.out.println(i.getSubmission().
							getCandidate().getFirstName()+" "
							+i.getSubmission().getCandidate().getLastName());
					System.out.println("\nFeedback: "+i.getFeedback());
					//path to excel questions&answers
				}
				break;
			case 4:
			case 5:
				// print info/results of submissions of the day, month, or year
				for (SubmissionEntity s : slist)
					printCandidateInfo(s, slist.indexOf(s), true);
				break;
			case 6:
				// print info/results of submissions of the day, month, or year
				for (SubmissionEntity s : slist) {
					printCandidateInfo(s, slist.indexOf(s), true);
					System.out.println("\nMotivo da Rejeição: "
							+s.getRejectReason());
				}
			case 7:
				// print info/results of submissions of the day, month, or year
				for (SubmissionEntity s : slist) {
					printCandidateInfo(s, slist.indexOf(s), true);

					String result = s.getStatus(); 
					if (result.equalsIgnoreCase(
							Constants.STATUS_PROPOSAL))
						System.out.println("\n\nProposta submetida"
								+ " (ainda sem resultado)");
					else if (result.equalsIgnoreCase(
							Constants.STATUS_NEGOTIATION))
						System.out.println("\n\nProposta em negociação.");
					else if (result.equalsIgnoreCase(
							Constants.STATUS_REJECTED))
						System.out.println("\n\nProposta recusada.");
					else if (result.equalsIgnoreCase(
							Constants.STATUS_HIRED))
						System.out.println("\n\nProposta aceite.");
				}
			}

			//move to next day, month, or year
			switch (period) {
			case Constants.DAILY:
				// move to next day
				startDate.add(Calendar.DAY_OF_YEAR, 1); // Calendar.DATE???
				break;
			case Constants.MONTHLY:
				// move to next month
				startDate.add(Calendar.MONTH, 1); // check
				break;
			case Constants.YEARLY:
				// move to next year
				startDate.add(Calendar.YEAR, 1); // check
			}

		}

		//fazer isso fora...
		// overall counts in the beginning of the list (count) to be returned
		switch (report) {
		case 1: case 4: case 5: case 6: case 7:
			// overall number of interviews/submissions between the two dates
			counts.add(0, total);
			break;
		case 2:	case 3:
			// overall average time to first interview/to be hired
			// between the two dates
			if (countTotal != 0) avg = total*1.0/countTotal;
			else avg = -1; // no submissions to compute average
			counts.add(0, (Long) Math.round(avg));  // tempo médio em double???
			break;	
		case 8:
			// keep also overall number of submissions for each source
			for(int i = ns-1; i >= 0; i--)
				counts.add(0, totalS.get(i));
		}

		List<Object[]> list = new ArrayList<Object[]>(counts.size());

		for (int i = 0; i < counts.size(); i++) {
			Object[] o = new Object[3];
			o[0] = headers.get(i);
			o[1] = counts.get(i);
			list.add(o);
		}

		return list;

	}

	public void printCandidateInfo(SubmissionEntity s, int index,
			boolean printPosition) {
		UserEntity cand = s.getCandidate();
		System.out.print("\nCandidato "+ index+" ");
		System.out.println("\nNome: "+cand.getFirstName()+" "
				+cand.getLastName());					
		UserInfoEntity info = cand.getUserInfo();
		System.out.println("\nCidade: "+info.getCity()+", "
				+info.getCountry());
		System.out.println("\nCurso: "+info.getCourse()+" ("
				+info.getSchool()+")");
		System.out.println("\nContactos: "+cand.getEmail()+" "
				+info.getMobilePhone()+" "+info.getHomePhone());

		if (printPosition) {
			PositionEntity pos = s.getPosition();
			if (!s.isSpontaneous()) 
				System.out.println("\n\nConcorreu à posição :"+pos.getTitle()+
						" ("+pos.getPositionCode()+")"); //truncate title??
			else { // spontaneous submission
				if (pos != null) 
					System.out.println("\n\nFoi associado à posição :"
							+pos.getTitle()+" ("+pos.getPositionCode()+")");
				else System.out.println("\n\nCandidatura espontânea");
			}
		}

	}

	private long daysBetween(Calendar dateStartCal,
			Calendar dateEndCal) {

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

	public Long intToLong(int value) {
		return Long.valueOf(value);
	}

}
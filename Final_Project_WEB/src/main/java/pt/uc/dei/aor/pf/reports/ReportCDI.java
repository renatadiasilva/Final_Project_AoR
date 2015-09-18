package pt.uc.dei.aor.pf.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

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
public class ReportCDI {

//	private static final Logger log = 
//			LoggerFactory.getLogger(ReportCDI.class);

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private SubmissionEJBInterface submissionEJB;
	
	// produces statistics by period between two dates
	public List<Integer> reportCounting(Date d1, Date d2, String periodType,
			int report, List<String> sources) {

		// auxiliary lists
		List<InterviewEntity> listI = new ArrayList<InterviewEntity>();
		List<SubmissionEntity> listS = new ArrayList<SubmissionEntity>();

		// different periods
		char period = ' ';
		String header = " ";

		if (periodType.equals(Constants.PERIOD_DAILY)) period = 'd';
		else if (periodType.equals(Constants.PERIOD_MONTHLY)) period = 'm';
		else if (periodType.equals(Constants.PERIOD_YEARLY)) period = 'y';
		else ;//error

		// different report headers
		switch (report) {
		case 1: header = "Número de Entrevistas: "; break;
		case 2: header = "Tempo médio para Entrevista"; break;
		case 3: header = "Tempo médio para Contratação"; break;
		}

		// convert to calendar to easily manipulate
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.setTime(d1);
		endDate.setTime(d2);

		// muitas validações... (limite dias/meses/etc)
		long ndays = daysBetween(startDate, endDate);
		if (ndays < 0) {
			// erro, datas não ordenadas
		}

		// limit day for periods - aviso ao utilizador!!
		if (ndays > Constants.LIMITMONTH) period = 'y';
		else if (ndays > Constants.LIMITDAY) period = 'm';

		// period para tempos médios??? ver melhor

		// contagens totais de meses/anos... aviso ao utilizador

		int n = 0;
		List<Integer> count = new ArrayList<Integer>();

		switch (period) {
		case 'm':
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			endDate.set(Calendar.DAY_OF_MONTH, 1);
		case 'y':
			startDate.set(Calendar.MONTH, 0);
			endDate.set(Calendar.MONTH, 0);
		}

// juntar listas?? não... 
//		List<InterviewEntity> allInterviews = new ArrayList<InterviewEntity>();

		int total = 0;
		int countTotal = 0;
		double avg;
		int ns = sources.size();
		List<Integer> totalS = new ArrayList<Integer>(ns);
		for (int i = 0; i < ns; i++) totalS.add(0);

		// each WHILE iteration corresponds to
		// a single day, month, or year between d1 and d2
		while (!startDate.after(endDate)) {

			// compute temporary end date
			Calendar interDate = Calendar.getInstance();
			switch (period) {
			case 'd': 
				// same day
				interDate.setTime(startDate.getTime());
				break;
			case 'm':
				// last day in the current month 
				interDate.set(Calendar.DAY_OF_MONTH, 
						startDate.getActualMaximum(Calendar.DAY_OF_MONTH));
				interDate.set(Calendar.MONTH, 
						startDate.get(Calendar.MONTH));
				interDate.set(Calendar.YEAR, 
						startDate.get(Calendar.YEAR));
				break;
			case 'y':
				// last day in the current year (December, 31)
				interDate.set(Calendar.DAY_OF_MONTH, 31);
				interDate.set(Calendar.MONTH, 11);
				interDate.set(Calendar.YEAR, startDate.get(Calendar.YEAR));
				break;
			}

			switch (report) {
			case 1:
				// get list of carried out interviews of the day, month, or year
				listI = interviewEJB.findCarriedOutInterviews(
						startDate.getTime(), interDate.getTime());

//				allInterviews.addAll(list);

				// count the number of interviews of the day, month, or year
				if (listI != null) n = listI.size();
				else n = 0; // no interviews
				count.add(n);

				// update overall number of interviews
				total += n;
				break;
			case 2:
				// get list of submissions of the day, month, or year
				listS = submissionEJB.findSubmissionsByDate(
						startDate.getTime(), interDate.getTime());

				// compute the average time to first interview 
				// (ignore submission without carried out interview)
				int countS = 0;
				avg = 0.0;
				for (SubmissionEntity s : listS) {
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
				count.add((int) Math.round(avg));  // tempo médio em double???
				break;
			case 3:
				// get list of submissions of the day, month, or year
				listS = submissionEJB.findSubmissionsByDate(
						startDate.getTime(), interDate.getTime());

				// compute the average time to be hired 
				// (ignore submission without hired status)
				countS = 0;
				avg = 0.0;
				for (SubmissionEntity s : listS) {
					//query??
					// submission status is hired
					if (s.getStatus().equalsIgnoreCase(
							Constants.STATUS_HIRED)) {

						// colect submission date and hired date
						Calendar sDate = Calendar.getInstance();
						Calendar hDate = Calendar.getInstance();
						sDate.setTime(s.getDate());
						//esta não --------------------------------!
						hDate.setTime(s.getPosition().getClosingDate());
						//esta sim
						hDate.setTime(s.getHiredDate());
						
						ndays = daysBetween(sDate, hDate);
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
				// average time of the day, month, or year
				if (countS != 0) avg /= countS;
				else avg = -1; // no submissions to compute average
				count.add((int) Math.round(avg));  // tempo médio em double???
				break;
			case 4:
				// get list of submissions of the day, month, or year
				listS = submissionEJB.findSubmissionsByDate(
						startDate.getTime(), interDate.getTime());

				// count the number of submissions of the day, month, or year
				if (listS != null) n = listS.size();
				else n = 0; // no submissions
				count.add(n);

				// update overall number of submissions
				total += n;
				break;
			case 5:
				// get list of spontaneous submissions 
				// of the day, month, or year
				listS = submissionEJB.findSpontaneousSubmissionsByDate(
						startDate.getTime(), interDate.getTime());

				// count the number of spontaneous submissions
				// of the day, month, or year
				if (listS != null) n = listS.size();
				else n = 0; // no submissions
				count.add(n);

				// update overall number of submissions
				total += n;
				break;
			case 6:
				// get list of rejected submissions of the day, month, or year
				listS = submissionEJB.findRejectedSubmissions(
						startDate.getTime(), interDate.getTime());

				// count the number of reject submissions
				// of the day, month, or year
				if (listS != null) n = listS.size();
				else n = 0; // no submissions
				count.add(n);

				// update overall number of submissions
				total += n;
				break;
			case 7: // está mal pois a data é da candidatura e não da proposta!
				// get list of presented proposal of the day, month, or year
				listS = submissionEJB.findPresentedProposals(
						startDate.getTime(), interDate.getTime());

				// count the number of presented proposals
				// of the day, month, or year
				if (listS != null) n = listS.size();
				else n = 0; // no submissions
				count.add(n);

				// update overall number of submissions
				total += n;
				break;
			case 8:
				int index = 0;
				// count submissions for each source of the list of sources
				for (String so : sources) {
					// get list of submissions by a source
					// of the day, month, or year
					listS = submissionEJB.findSubmissionsBySource(so,
							startDate.getTime(), interDate.getTime());

					// count the number of submissions by a source
					// of the day, month, or year
					if (listS != null) n = listS.size();
					else n = 0; // no submissions
					count.add(n);

					// update overall number of submissions by source
					Integer value = totalS.get(index);
					totalS.set(index, value+n);
					index++;
				}
				break;
			}

			switch (period) {
			case 'd':
				String m = startDate.getDisplayName(Calendar.MONTH,
						Calendar.LONG, Locale.getDefault());
				System.out.println("\n"+header+" (DIA: "+
						startDate.get(Calendar.DAY_OF_MONTH)+" "+m+" "
						+startDate.get(Calendar.YEAR)+"): "+n);
				break;
			case 'm':
				m = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG,
						Locale.getDefault());
				System.out.println("\n"+header+" (MÊS: "+m+" "+
						startDate.get(Calendar.YEAR)+"): "+n);
				break;
			case 'y':
				System.out.println("\n"+header+" (ANO: "+
						startDate.get(Calendar.YEAR)+"): "+n);
				break;
			}

			//print more information
			switch (report) {
			case 1:
				// print info/results of interviews of the day, month, or year
				for (InterviewEntity i : listI) {
					// PDF file
					System.out.println("\n\nEntrevista "+ listI.indexOf(i));
					System.out.println("\nData: "+i.getDate());
					System.out.print("\nEntrevistadores: ");
					for (UserEntity u : i.getInterviewers())
						System.out.println(u.getFirstName()+" "+
								u.getLastName()+", ");
					System.out.print("\nCandidato: ");
					System.out.println(i.getSubmission().
							getCandidate().getFirstName()+" "
							+i.getSubmission().getCandidate().getLastName());
					System.out.println("\n\nAprovado: "+
							(i.isApproved()?"SIM":"NÃO"));
					System.out.println("\nFeedback: "+i.getFeedback());
				}
				break;
			case 4:
			case 5:
				// print info/results of submissions of the day, month, or year
				for (SubmissionEntity s : listS)
					printCandidateInfo(s, listS.indexOf(s), true);
				break;
			case 6:
				// print info/results of submissions of the day, month, or year
				for (SubmissionEntity s : listS) {
					printCandidateInfo(s, listS.indexOf(s), true);
					System.out.println("\nMotivo da Rejeição: "
							+s.getRejectReason());
				}
			case 7:
				// print info/results of submissions of the day, month, or year
				for (SubmissionEntity s : listS) {
					printCandidateInfo(s, listS.indexOf(s), true);
					String result = s.getStatus(); 
					if (result.equalsIgnoreCase(
							Constants.STATUS_SPROPOSAL))
						System.out.println("\n\nProposta recebida"
								+ " (ainda não avaliada)");
					else if (result.equalsIgnoreCase(
							Constants.STATUS_OPROPOSAL))
						System.out.println("\n\nProposta em negociação.");
					else if (result.equalsIgnoreCase(
							Constants.STATUS_RPROPOSAL))
						System.out.println("\n\nProposta recusada.");
					else if (result.equalsIgnoreCase(
							Constants.STATUS_APROPOSAL))
						System.out.println("\n\nProposta aceite.");
				}
			}

			//move to next day, month, or year
			switch (period) {
			case 'd':
				// move to next day
				startDate.add(Calendar.DAY_OF_YEAR, 1); // Calendar.DATE???
				break;
			case 'm':
				// move to next month
				startDate.add(Calendar.MONTH, 1); // check
				break;
			case 'y':
				// move to next year
				startDate.add(Calendar.YEAR, 1); // check
			}
		}

		// overall counts in the beginning of the list (count) to be returned
		switch (report) {
		case 1: case 4: case 5: case 6: case 7:
			// overall number of interviews/submissions between the two dates
			count.add(0, total);
			break;
		case 2:	case 3:
			// overall average time to first interview/to be hired
			// between the two dates
			if (countTotal != 0) avg = total*1.0/countTotal;
			else avg = -1; // no submissions to compute average
			count.add(0, (int) Math.round(avg));  // tempo médio em double???
			break;	
		case 8:
			// keep also overall number of submissions for each source
			for(int i = ns-1; i >= 0; i--)
				count.add(0, totalS.get(i));
		}

		return count;

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

	private long daysBetween(Calendar dateStartCal, Calendar dateEndCal) {

		dateStartCal.set(Calendar.HOUR_OF_DAY, 0);
		dateStartCal.set(Calendar.MINUTE, 0);
		dateStartCal.set(Calendar.SECOND, 0);
		dateStartCal.set(Calendar.MILLISECOND, 0);

		dateEndCal.set(Calendar.HOUR_OF_DAY, 0);
		dateEndCal.set(Calendar.MINUTE, 0);
		dateEndCal.set(Calendar.SECOND, 0);
		dateEndCal.set(Calendar.MILLISECOND, 0);

		return (dateStartCal.getTimeInMillis() - 
					dateEndCal.getTimeInMillis()) / Constants.MSPERDAY;
	}
}

package pt.uc.dei.aor.pf.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

// to put in WEB (CDI Bean)??
public class InterviewsReport {

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	// list of interview counts and results by period between two dates (file?)
	public List<Integer> interviewCountResults(Date d1, Date d2, String period) {
		return reportCounting(d1, d2, period, 1);  // if 0, no interviews, no report!
	}

	// average time to first interview by period between two dates (file?)
	public List<Integer> averageTimeToInterview(Date d1, Date d2, String period) {
		return reportCounting(d1, d2, period, 2); // if -1, no valid submissions, no report!
	}

	// produces statistics by period between two dates
	public List<Integer> reportCounting(Date d1, Date d2, String periodType,
			int report) {

		// limit searching days by period
		// ver valores...
		final long LIMITDAY = 100;
		final long LIMITMONTH = 1000;

		// auxiliary lists
		List<InterviewEntity> listI = new ArrayList<InterviewEntity>();
		List<SubmissionEntity> listS = new ArrayList<SubmissionEntity>();

		// different periods
		char period = ' ';
		String header = " ";

		if (periodType == "daily") period = 'd';
		else if (periodType == "monthly") period = 'm';
		else if (periodType == "yearly") period = 'y';
		else ;//error

		// different report headers
		switch (report) {
		case 1: header = "Número de Entrevistas: "; break;
		case 2: header = "Tempo médio para Entrevista"; break;
		}

		// convert to calendar to easily manipulate
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);

		// muitas validações... (limite dias/meses/etc)
		long ndays = daysBetween(cal1, cal2);
		if (ndays < 0) {
			// erro, datas não ordenadas
		}

		// limit day for periods - aviso ao utilizador!!
		if (ndays > LIMITMONTH) period = 'y';
		else if (ndays > LIMITDAY) period = 'm';

		// period para tempos médios??? ver melhor
		
		// contagens totais de meses/anos... aviso ao utilizador

		int n = 0;
		List<Integer> count = new ArrayList<Integer>();

		switch (period) {
		case 'm':
			cal1.set(Calendar.DAY_OF_MONTH, 1);
			cal2.set(Calendar.DAY_OF_MONTH, 1);
		case 'y':
			cal1.set(Calendar.MONTH, 0);
			cal2.set(Calendar.MONTH, 0);
		}

		// juntar listas?? não... 
		//		List<InterviewEntity> allInterviews = new ArrayList<InterviewEntity>();

		int total = 0;
		int countTotal = 0;
		double avg;
		// each WHILE iteration corresponds to a single day, month, or year between d1 and d2
		while (!cal1.after(cal2)) {

			// compute temporary end date
			Calendar cal3 = Calendar.getInstance();
			switch (period) {
			case 'd': 
				// same day
				cal3.setTime(cal1.getTime());
				break;
			case 'm':
				// last day in the current month 
				cal3.set(Calendar.DAY_OF_MONTH, cal1.getActualMaximum(Calendar.DAY_OF_MONTH));
				cal3.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
				cal3.set(Calendar.YEAR, cal1.get(Calendar.YEAR));
				break;
			case 'y':
				// last day in the current year (December, 31)
				cal3.set(Calendar.DAY_OF_MONTH, 31);
				cal3.set(Calendar.MONTH, 11);
				cal3.set(Calendar.YEAR, cal1.get(Calendar.YEAR));
				break;
			}

			switch (report) {
			case 1:
				// get list of carried out interviews of the day, month, or year
				listI = interviewEJB.findCarriedOutInterviews(cal1.getTime(), cal3.getTime());

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
				listS = submissionEJB.findSubmissionsByDate(cal1.getTime(), cal3.getTime());

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
						tomorrow.add(Calendar.DAY_OF_YEAR, 1); // day plus one: tomorrow
						
						// set initial older date as tomorrow (worse case)
						Calendar firstI = tomorrow;

						// find the first carried out interview of this submission, if any
						Calendar cal5 = Calendar.getInstance();
						for (InterviewEntity i : listSI) {
							if (i.isCarriedOut()) {
								// date of interview
								cal5.setTime(i.getDate());
								
								// if interview date before older update
								if (cal5.before(firstI)) {
									firstI.setTime(cal5.getTime());
								}
							}
						}
						
						// submission has carried out interviews, compute time
						if (firstI.before(tomorrow)) {
						
							Date dS = s.getDate();
							// convert to calendar to easily manipulate
							cal5.setTime(dS);

							ndays = daysBetween(cal5, firstI);
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
			}

			switch (period) {
			case 'd':
				String m = cal1.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				System.out.println("\n"+header+" (DIA: "+
						cal1.get(Calendar.DAY_OF_MONTH)+" "+m+" "+cal1.get(Calendar.YEAR)+"): "+n);
				break;
			case 'm':
				m = cal1.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				System.out.println("\n"+header+" (MÊS: "+m+" "+cal1.get(Calendar.YEAR)+"): "+n);
				break;
			case 'y':
				System.out.println("\n"+header+" (ANO: "+cal1.get(Calendar.YEAR)+"): "+n);
				break;
			}

			//print more information
			switch (report) {
			case 1:
				// print results of interviews of the day, month, or year
				for (InterviewEntity i : listI) {
					// PDF file
					System.out.println("\n\nEntrevista "+ listI.indexOf(i));
					System.out.println("\nData: "+i.getDate());
					System.out.println("\nEntrevistadores: ");
					for (UserEntity u : i.getInterviewers())
						System.out.println(u.getFirstName()+" "+u.getLastName()+", ");
					System.out.println("\nCandidato: ");
					System.out.println(i.getSubmission().getCandidate().getFirstName()+" "
							+i.getSubmission().getCandidate().getLastName());
					System.out.println("\n\nAprovado: "+(i.isApproved()?"SIM":"NÃO"));
					System.out.println("\nFeedback: "+i.getFeedback());
				}
				break;
			}

			//move to next day, month, or year
			switch (period) {
			case 'd':
				// move to next day
				cal1.add(Calendar.DAY_OF_YEAR, 1); // Calendar.DATE???
				break;
			case 'm':
				// move to next month
				cal1.add(Calendar.MONTH, 1); // check
				break;
			case 'y':
				// move to next year
				cal1.add(Calendar.YEAR, 1); // check
			}
		}

		switch (report) {
		case 1:
			// keep also overall number of interviews between the two dates
			count.add(0, total);
			break;
		case 2:
			// keep also overall average time to first interview between the two dates
			if (countTotal != 0) avg = total*1.0/countTotal;
			else avg = -1; // no submissions to compute average
			count.add(0, (int) Math.round(avg));  // tempo médio em double???
			break;			
		}
			
		return count;

	}

	private long daysBetween(Calendar dateStartCal, Calendar dateEndCal) {
		final long MSPERDAY = 60 * 60 * 24 * 1000;

		dateStartCal.set(Calendar.HOUR_OF_DAY, 0);
		dateStartCal.set(Calendar.MINUTE, 0);
		dateStartCal.set(Calendar.SECOND, 0);
		dateStartCal.set(Calendar.MILLISECOND, 0);

		dateEndCal.set(Calendar.HOUR_OF_DAY, 0);
		dateEndCal.set(Calendar.MINUTE, 0);
		dateEndCal.set(Calendar.SECOND, 0);
		dateEndCal.set(Calendar.MILLISECOND, 0);

		return (dateStartCal.getTimeInMillis() - dateEndCal.getTimeInMillis()) / MSPERDAY;
	}
}

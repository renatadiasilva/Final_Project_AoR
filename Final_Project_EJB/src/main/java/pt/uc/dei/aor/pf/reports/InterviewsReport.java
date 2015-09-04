package pt.uc.dei.aor.pf.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

// to put in WEB (CDI Bean)??
public class InterviewsReport {

	@EJB
	private InterviewEJBInterface interviewEJB;

	// List of interview counts and results by period between two dates
	public List<Integer> interviewCountResults(Date d1, Date d2, char period) {

		// ver valores...
		final long LIMITDAY = 100;
		final long LIMITMONTH = 1000;

		// convert to calendar to easily manipulate
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);

		// muitas validações... (limite dias/meses/etc)
		long nday = daysBetween(cal1, cal2);

		// aviso ao utilizador!!
		if (nday > LIMITMONTH) period = 'y';
		else if (nday > LIMITDAY) period = 'm';

		// contagens totais de meses/anos... aviso ao utilizador

		int n = 0;
		List<Integer> countInterviews = new ArrayList<Integer>();

		switch (period) {
		case 'm':
			cal1.set(Calendar.DAY_OF_MONTH, 1);
			cal2.set(Calendar.DAY_OF_MONTH, 1);
		case 'y':
			cal1.set(Calendar.MONTH, 0);
			cal2.set(Calendar.MONTH, 0);
		}

		// juntar listas?? 
		//		List<InterviewEntity> allInterviews = new ArrayList<InterviewEntity>();

		while (!cal1.after(cal2)) {
			// end date
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
			// get list of interviews of the day, month, or year
			List<InterviewEntity> list = 
					interviewEJB.findCarriedOutInterviews(cal1.getTime(), cal3.getTime());
			//			allInterviews.addAll(list);
			// count the number of interviews of the day, month, or year
			n = list.size();
			countInterviews.add(n);

			switch (period) {
			case 'd':
				String m = cal1.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				System.out.println("\n Número de entrevistas (DIA: "+
						cal1.get(Calendar.DAY_OF_MONTH)+" "+m+" "+cal1.get(Calendar.YEAR)+"): "+n);
				break;
			case 'm':
				m = cal1.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				System.out.println("\n Número de entrevistas (MÊS: "+m+" "+cal1.get(Calendar.YEAR)+"): "+n);
				break;
			case 'y':
				System.out.println("\n Número de entrevistas (ANO: "+cal1.get(Calendar.YEAR)+"): "+n);
				break;
			}
			// print results of interviews of the day, month, or year
			for (InterviewEntity i : list) {
				// PDF file
				System.out.println("\n\nEntrevista "+ list.indexOf(i));
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
		
		return countInterviews;

	}

	// cuidados, as duas datas têm de estar ordenadas
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

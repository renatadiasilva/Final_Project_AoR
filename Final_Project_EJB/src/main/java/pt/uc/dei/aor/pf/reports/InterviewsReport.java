package pt.uc.dei.aor.pf.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;

// to put in WEB (CDI Bean)
public class InterviewsReport {

	@EJB
	private InterviewEJBInterface interviewEJB;

	// List of interview counts by period between two dates
	public List<Integer> interviewCount(Date d1, Date d2, char period) {
		
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
		
		int y1 = 0, m1 = 0, day1 = 0;
		List<Integer> countInterviews = new ArrayList<Integer>();

		switch (period) {
		case 'd': 		
			// day by day
			while (!cal1.after(cal2)) {
				y1 = cal1.get(Calendar.YEAR);
				m1 = cal1.get(Calendar.MONTH);
				day1 = cal1.get(Calendar.DAY_OF_MONTH);
				// get list of interviews of the day
				List<InterviewEntity> list = 
						interviewEJB.findCarriedOutInterviews(cal1.getTime(), cal1.getTime());  
				// count the number of interviews of the day
				countInterviews.add(list.size());
				// print results of interviews of the day
				for (InterviewEntity i : list) {
					// PDF file
					System.out.println();
					System.out.println("Feedback:"+i.getFeedback()+" ");
				}
				// move to next day
				cal1.add(Calendar.DAY_OF_YEAR, 1); // Calendar.DATE???
			}
			break;
		case 'm': 		
			// month by month
			cal1.set(Calendar.DAY_OF_MONTH, 1);
			cal2.set(Calendar.DAY_OF_MONTH, 1);
			while (!cal1.after(cal2)) {
				y1 = cal1.get(Calendar.YEAR);
				m1 = cal1.get(Calendar.MONTH);				
				Calendar cal3 = Calendar.getInstance();
				cal3.set(Calendar.DAY_OF_MONTH, cal1.getActualMaximum(Calendar.DAY_OF_MONTH));
				cal3.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
				cal3.set(Calendar.YEAR, cal1.get(Calendar.YEAR));
				l.add(interviewEJB.findCarriedOutInterviews(cal1.getTime(), cal3.getTime()).size());
				// move to next month
				cal1.add(Calendar.MONTH, 1); // check
			}
			break;
		case 'y': 
			// year by year
			cal1.set(Calendar.DAY_OF_MONTH, 1);
			cal2.set(Calendar.DAY_OF_MONTH, 1);
			cal1.set(Calendar.MONTH, 0);
			cal2.set(Calendar.MONTH, 0);
			while (!cal1.after(cal2)) {
				y1 = cal1.get(Calendar.YEAR);
				l.add(interviewEJB.findCarriedOutInterviews(0, 0, y1).size());
				// move to next year
				cal1.add(Calendar.YEAR, 1); // check
			}
			break;
		}
		
		return l;

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

package pt.uc.dei.aor.pf.reports;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public class ReportItem implements Serializable {

	private static final long serialVersionUID = 2087758556006248256L;

	private PositionEntity position;

	private InterviewEntity interview;

	private SubmissionEntity submission;
	
	private String dateHeader;
	
	private int measure;
	
	private String result;

	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 
	private SimpleDateFormat ftHour = new SimpleDateFormat ("HH:mm"); 

	public ReportItem(PositionEntity position, InterviewEntity interview,
			SubmissionEntity submission,
			String dateHeader, int measure, String result) {
		this.interview = interview;
		this.position = position;
		this.submission = submission;
		this.dateHeader = dateHeader;
		this.measure = measure;
		this.result = result;
	}

	public String getDateHeader() {
		return dateHeader;
	}

	public void setDateHeader(String dateHeader) {
		this.dateHeader = dateHeader;
	}

	public int getMeasure() {
		return measure;
	}

	public void setMeasure(int measure) {
		this.measure = measure;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public InterviewEntity getInterview() {
		return interview;
	}

	public void setInterview(InterviewEntity interview) {
		this.interview = interview;
	}
	
	public String getDateInterview() {
		if (interview == null) return "";
		return ftDate.format(interview.getDate());
	}

	public String getHourInterview() {
		if (interview == null) return "";
		return ftHour.format(interview.getDate());
	}

	public String getInterviewers() {
		String s = "";
		if (interview == null) return "";
		List<UserEntity> interviewers = interview.getInterviewers();
		int limit = (int) Math.min(interviewers.size(), 3)-1;
		for(int i = 0; i < limit; i++)
			s += interviewers.get(i).getFirstName()+" "
					+interviewers.get(i).getLastName()+", ";
		s += interviewers.get(limit).getFirstName()+" "
				+interviewers.get(limit).getLastName();
		return s;
	}

	public SubmissionEntity getSubmission() {
		return submission;
	}

	public void setSubmission(SubmissionEntity submission) {
		this.submission = submission;
	}
}
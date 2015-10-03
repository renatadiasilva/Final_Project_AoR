package pt.uc.dei.aor.pf.reports;

import java.io.Serializable;
import java.text.Normalizer;
import java.text.SimpleDateFormat;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;

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

	public String getDateSubmission() {
		if (submission == null) return "";
		return ftDate.format(submission.getDate());
	}

	public String getHourInterview() {
		if (interview == null) return "";
		return ftHour.format(interview.getDate());
	}

	public String getInterviewers() {
		return interview.first3Interviewers();
	}

	public SubmissionEntity getSubmission() {
		return submission;
	}

	public void setSubmission(SubmissionEntity submission) {
		this.submission = submission;
	}
	
	public String interviewCandidate() {
		return removeAccents(interview.getSubmission().getCandidate().getFirstName()
				+" "+interview.getSubmission().getCandidate().getLastName());
	}

	public String submissionCandidate() {
		return removeAccents(submission.getCandidate().getFirstName()
				+" "+submission.getCandidate().getLastName());
	}

	public String submissionStatus() {
		if (submission.getStatus().equals(Constants.STATUS_SUBMITED))
			return "Submited";
		if (submission.getStatus().equals(Constants.STATUS_ACCEPTED))
			return "Accepted To Interview";
		if (submission.getStatus().equals(Constants.STATUS_NEGOTIATION))
			return "Proposal On Negotiation";
		if (submission.getStatus().equals(Constants.STATUS_PROPOSAL))
			return "Presented Proposal";
		if (submission.getStatus().equals(Constants.STATUS_REJECTED))
			return "Rejected";
		return "Hired";
	}

	public String positionStatus() {
		if (position.getStatus().equals(Constants.STATUS_OPEN))
			return "Open";
		if (position.getStatus().equals(Constants.STATUS_ONHOLD))
			return "On Hold";
		return "Closed";
	}

	private String removeAccents(String name) {

		// separates all of the accent marks from the characters
		String pattern = Normalizer.normalize(name, Normalizer.Form.NFD);

		// compares each character against being a letter and 
		// throw out the ones that aren't.
		pattern = pattern.replaceAll("\\p{M}", "");

		return pattern;
	}

}
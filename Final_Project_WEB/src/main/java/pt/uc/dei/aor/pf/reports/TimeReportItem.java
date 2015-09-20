package pt.uc.dei.aor.pf.reports;

import java.io.Serializable;

public class TimeReportItem implements Serializable {

	private static final long serialVersionUID = 2087758556006248256L;

	private String dateHeader;
	
	private String time;

	public TimeReportItem(String dateHeader, String time) {
		this.dateHeader = dateHeader;
		this.time = time;
	}

	public String getDateHeader() {
		return dateHeader;
	}

	public void setDateHeader(String dateHeader) {
		this.dateHeader = dateHeader;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
package pt.uc.dei.aor.pf.reports;

import java.io.Serializable;
import java.util.List;

public class SubmissionReport implements Serializable {

	private static final long serialVersionUID = 2087758556006248256L;

	private String dateHeader;
	
	private List<SubmissionReportItem> subrep;

    public int getTotalCountings() {
        int sum = 0;
         
        for(SubmissionReportItem s : subrep) {
            sum += s.getCounting();
        }
         
        return sum;
    }

	public String getDateHeader() {
		return dateHeader;
	}

	public void setDateHeader(String dateHeader) {
		this.dateHeader = dateHeader;
	}

	public List<SubmissionReportItem> getSubrep() {
		return subrep;
	}

	public void setSubrep(List<SubmissionReportItem> subrep) {
		this.subrep = subrep;
	}

	public SubmissionReport(String dateHeader,
			List<SubmissionReportItem> subrep) {
		this.dateHeader = dateHeader;
		this.subrep = subrep;
	}
	
}

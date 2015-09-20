package pt.uc.dei.aor.pf.reports;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

public class PositionReport implements Serializable {

	private static final long serialVersionUID = 2087758556006248256L;

	@Inject
	private ReportManager report;
	
	private List<PositionReportItem> items;

	public PositionReport(List<PositionReportItem> items) {
		this.items = items;
	}

	public Long getTotalCountings() {
		int sum = 0;

		for(PositionReportItem s : items) {
			sum += s.getCounting();
		}

		return report.intToLong(sum);
	}

	public List<PositionReportItem> getItems() {
		return items;
	}

	public void setItems(List<PositionReportItem> items) {
		this.items = items;
	}

}
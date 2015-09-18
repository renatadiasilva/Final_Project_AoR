package pt.uc.dei.aor.pf.reports;

import java.io.Serializable;

import pt.uc.dei.aor.pf.entities.PositionEntity;

public class SubmissionReportItem implements Serializable {

	private static final long serialVersionUID = 2087758556006248256L;

	private PositionEntity position;
	
	private Long counting;

	public SubmissionReportItem(PositionEntity position, Long counting) {
		this.position = position;
		this.counting = counting;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public Long getCounting() {
		return counting;
	}

	public void setCounting(Long counting) {
		this.counting = counting;
	}

}

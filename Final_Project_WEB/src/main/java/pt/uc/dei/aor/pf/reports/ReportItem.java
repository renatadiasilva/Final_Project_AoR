package pt.uc.dei.aor.pf.reports;

import java.io.Serializable;

import pt.uc.dei.aor.pf.entities.PositionEntity;

public class ReportItem implements Serializable {

	private static final long serialVersionUID = 2087758556006248256L;

	private PositionEntity position;
	
	private String dateHeader;
	
	private int measure;
	
	private String result;

	public ReportItem(PositionEntity position,
			String dateHeader, int measure, String result) {
		this.position = position;
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

}
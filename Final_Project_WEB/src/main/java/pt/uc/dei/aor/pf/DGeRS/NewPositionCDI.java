package pt.uc.dei.aor.pf.DGeRS;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@ViewScoped
public class NewPositionCDI implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7268951775497698179L;
	
	private Date openingDate;
	
	private String positionCode, location;
	
	private Map<String, String> state;
	
	private String currentState;
	
	private int openings;
	
	private Date closingDate;
	
	private String sla;
	
	private String responsible;
	
	private String company;
	
	private List<String> technicalArea;
	
	private String description;
	
	private List<String> advertisingChannels;	

	public NewPositionCDI() {
	}

	public void done() {
		System.out.println(openingDate);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Done!"));
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Map<String, String> getState() {
		
		return state;
	}

	public void setState(Map<String, String> state) {
		this.state = state;
	}
	
	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public int getOpenings() {
		return openings;
	}

	public void setOpenings(int openings) {
		this.openings = openings;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public List<String> getTechnicalArea() {
		return technicalArea;
	}

	public void setTechnicalArea(List<String> technicalArea) {
		this.technicalArea = technicalArea;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getAdvertisingChannels() {
		return advertisingChannels;
	}

	public void setAdvertisingChannels(List<String> advertisingChannels) {
		this.advertisingChannels = advertisingChannels;
	}
	
	@PostConstruct
    public void init() {
        //cities
        state = new HashMap<String, String>();
        state.put("Aberto", "Aberto");
        state.put("Fechado","Fechado");
        state.put("on Hold","on Hold");
    }

}

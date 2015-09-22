package pt.uc.dei.aor.pf.admin;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.webManagement.UserManagementInterface;

import java.util.Date;
import java.util.List;
import java.io.Serializable;

@Named
@SessionScoped
public class NewPositionCDI implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5067846817471456766L;
	
	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private UserManagementInterface userManagement;
	
	private String managerEmail;

	private String positionCode;

	private String title;

	private List<String> locations;
	
	private String extraLocation;

	private int openings;

	private int slaDays;

	private UserEntity positionManager;

	private String company;

	private String technicalArea;
	
	private boolean sspa, dotnet, java, safety, management, integration;

	private String description;

	private List<String> advertisingChannels;
	
	private boolean critical, linkedin, glassdoor, facebook;

	private ScriptEntity defaultScript;
	
	private boolean lisboa, porto, coimbra;
	
	public NewPositionCDI() {
		this.extraLocation="";
	}

	public void createPosition(){
		
		// locations
		if(this.lisboa)this.locations.add(Constants.LOCATION_LISBOA);
		if(this.porto)this.locations.add(Constants.LOCATION_PORTO);
		if(this.coimbra)this.locations.add(Constants.LOCATION_COIMBRA);
		if(!this.extraLocation.isEmpty())this.locations.add(this.extraLocation);
		
		// advertisingChannels
		if(this.critical)this.advertisingChannels.add(Constants.SOCIAL_CRITICAL);
		if(this.linkedin)this.advertisingChannels.add(Constants.SOCIAL_LINKEDIN);
		if(this.glassdoor)this.advertisingChannels.add(Constants.SOCIAL_GLASSDOOR);
		if(this.facebook)this.advertisingChannels.add(Constants.SOCIAL_FACEBOOK);
		
		// technicalArea
		
		
		PositionEntity newPositionEntity=new PositionEntity(title, locations,
				openings, Constants.STATUS_OPEN, null, slaDays,
				this.userEJB.findUserByEmail(this.managerEmail),
				this.userEJB.findUserByEmail(this.userManagement.getUserEmail()),
				company, technicalArea, description, advertisingChannels, defaultScript);
	}
	
}

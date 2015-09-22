package pt.uc.dei.aor.pf.admin;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.util.ArrayList;
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
	private ScriptEJBInterface scriptEJB;

	@EJB
	private PositionEJBInterface positionEJB;
	
	private String creatorEmail;

	private UserEntity manager;

	private String title;

	private List<String> locations;

	private String extraLocation;

	private int openings;

	private int slaDays;

	private String company;

	private String technicalArea;

	private boolean sspa, dotnet, java, safety, management, integration;

	private String description;

	private List<String> advertisingChannels;
	
	private List<NewPositionCDIextraAd>altAdvertisingChannels;
	
	private String extraAdvertising;

	private boolean critical, linkedin, glassdoor, facebook;

	private ScriptEntity script;

	private boolean lisboa, porto, coimbra;

	public NewPositionCDI() {
		this.cleanBean();
	}

	@Asynchronous
	public void createPosition() {
		System.out.println("A criar posição");
		
		boolean valid=true;

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
		if(!this.altAdvertisingChannels.isEmpty())
			for(NewPositionCDIextraAd ad:this.altAdvertisingChannels)
				this.advertisingChannels.add(ad.getAd());

		// technicalArea
		if(this.sspa)this.technicalArea=Constants.TECH_SSPA;
		if(this.dotnet)this.technicalArea=Constants.TECH_DOTNET;
		if(this.java)this.technicalArea=Constants.TECH_JAVA;
		if(this.safety)this.technicalArea=Constants.TECH_SAFETY;
		if(this.management)this.technicalArea=Constants.TECH_MANAGEMENT;
		if(this.integration)this.technicalArea=Constants.TECH_INTEGRATION;

		if(this.locations.isEmpty()){
			valid=false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Escolha uma localização."));
		}
		
		if(this.advertisingChannels.isEmpty()){
			valid=false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Escolha os canais de publicidade."));
		}
		
		if(this.technicalArea.isEmpty()){
			valid=false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Escolha uma área técnica."));
		}
		
		if(this.script==null){
			valid=false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Escolha um guião padrão."));
		}
		
		if(this.manager==null){
			valid=false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Escolha um gestor."));
		}
		
		if(this.openings==0){
			valid=false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Defina o número de vagas."));
		}
		
		if(this.slaDays==0){
			valid=false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Defina o SLA."));
		}

		if(valid){
			UserEntity creator=this.userEJB.findUserByEmail(this.creatorEmail);
			
			PositionEntity newPositionEntity=new PositionEntity(title, locations,
					openings, Constants.STATUS_OPEN, null, slaDays, manager, creator,
					company, technicalArea, description, advertisingChannels, script);

			this.positionEJB.save(newPositionEntity);

			this.cleanBean();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nova posição criada."));
		}

	}

	public void cleanBean() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		this.creatorEmail=request.getRemoteUser();
		
		System.out.println(this.creatorEmail);
		
		this.manager=null;
		
		this.script=null;
		
		this.openings=this.slaDays=0;
		
		this.locations=new ArrayList<String>();
		
		this.advertisingChannels=new ArrayList<String>();
		this.altAdvertisingChannels=new ArrayList<NewPositionCDIextraAd>();
		this.extraAdvertising="";

		this.cleanTechnicalAreas();

		this.lisboa=this.porto=this.coimbra=false;
		this.extraLocation="";

		this.sspa=this.dotnet=this.java=this.safety=this.management=this.integration=false;
		
		this.script=null;
	}
	
	public boolean checkManager(UserEntity manager){
		if(this.manager==null)return false;
		if(this.manager.getId()==manager.getId())return true;
		return false;
	}
	
	public boolean checkScript(ScriptEntity script){
		if(this.script==null)return false;
		if(this.script.getId()==script.getId())return true;
		return false;
	}

	public List<NewPositionCDIextraAd> getAltAdvertisingChannels() {
		return altAdvertisingChannels;
	}

	public void addAltAdvertisingChannels() {
		this.altAdvertisingChannels.add(new NewPositionCDIextraAd(this.extraAdvertising));
		this.extraAdvertising="";
	}
	
	public void deleteAltAdvertisingChannels(NewPositionCDIextraAd ad) {
		this.altAdvertisingChannels.remove(ad);
	}
	
	public String getExtraAdvertising() {
		return extraAdvertising;
	}

	public void setExtraAdvertising(String extraAdvertising) {
		this.extraAdvertising = extraAdvertising;
	}

	public List<UserEntity> getManagers(){
		return this.userEJB.findAllManagers();
	}

	public List<ScriptEntity> getScripts(){
		return this.scriptEJB.findAll();
	}
	
	public void setScript(ScriptEntity script){
		System.out.println("Set Script" +script.getTitle());
		this.script=script;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ScriptEntity getDefaultScript() {
		return script;
	}

	public void setDefaultScript(ScriptEntity defaultScript) {
		this.script = defaultScript;
	}

	public int getSlaDays() {
		return slaDays;
	}

	public void setSlaDays(int slaDays) {
		this.slaDays = slaDays;
	}

	// advertising Channels Start
	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	public boolean isLinkedin() {
		return linkedin;
	}

	public void setLinkedin(boolean linkedin) {
		this.linkedin = linkedin;
	}

	public boolean isGlassdoor() {
		return glassdoor;
	}

	public void setGlassdoor(boolean glassdoor) {
		this.glassdoor = glassdoor;
	}

	public boolean isFacebook() {
		return facebook;
	}

	public void setFacebook(boolean facebook) {
		this.facebook = facebook;
	}
	// advertising Channels End

	public int getOpenings() {
		return openings;
	}

	public void setOpenings(int openings) {
		this.openings = openings;
	}

	public void setManager(UserEntity manager) {
		System.out.println("Set Manager "+manager.getEmail());
		this.manager = manager;
	}

	public String getExtraLocation() {
		return extraLocation;
	}

	public void setExtraLocation(String extraLocation) {
		this.extraLocation = extraLocation;
	}

	// technicalAreas Start
	private void cleanTechnicalAreas(){
		this.sspa=this.dotnet=this.java=this.safety=this.management=this.integration=false;
	}

	public boolean isSspa() {
		return sspa;
	}

	public void setSspa(boolean sspa) {
		this.cleanTechnicalAreas();
		this.sspa = sspa;
	}

	public boolean isDotnet() {
		return dotnet;
	}

	public void setDotnet(boolean dotnet) {
		this.cleanTechnicalAreas();
		this.dotnet = dotnet;
	}

	public boolean isJava() {
		return java;
	}

	public void setJava(boolean java) {
		this.cleanTechnicalAreas();
		this.java = java;
	}

	public boolean isSafety() {
		return safety;
	}

	public void setSafety(boolean safety) {
		this.cleanTechnicalAreas();
		this.safety = safety;
	}

	public boolean isManagement() {
		return management;
	}

	public void setManagement(boolean management) {
		this.cleanTechnicalAreas();
		this.management = management;
	}

	public boolean isIntegration() {
		return integration;
	}

	public void setIntegration(boolean integration) {
		this.cleanTechnicalAreas();
		this.integration = integration;
	}
	// technicalAreas End

	// locations Start
	public boolean isLisboa() {
		return lisboa;
	}

	public void setLisboa(boolean lisboa) {
		this.lisboa = lisboa;
	}

	public boolean isPorto() {
		return porto;
	}

	public void setPorto(boolean porto) {
		this.porto = porto;
	}

	public boolean isCoimbra() {
		return coimbra;
	}

	public void setCoimbra(boolean coimbra) {
		this.coimbra = coimbra;
	}
	// locations End

}
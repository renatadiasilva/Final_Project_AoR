package pt.uc.dei.aor.pf.admin;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

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

	@Inject
	private UserSessionManagement userManagement;
	
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

	private List<ScriptEntity>scripts;

	private ScriptEntity script;

	private ScriptEntity checkScript;

	private boolean lisboa, porto, coimbra;

	private boolean editPosition;

	private List<PositionEntity>openPositions;

	private PositionEntity position;

	public void createNewPosition(){
		this.editPosition=false;
		this.cleanBean();
	}

	public void editExistingPosition(){
		this.editPosition=true;
		this.cleanBean();
	}

	@PostConstruct
	public void cleanBean() {
		if(editPosition) this.openPositions=this.positionEJB.findAll();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		this.creatorEmail=request.getRemoteUser();

		this.title="";
		this.company="";
		this.description="";

		System.out.println(this.creatorEmail);

		this.manager=null;

		this.script=null;
		this.scripts=this.scriptEJB.findReusableScripts();

		this.openings=this.slaDays=0;

		this.locations=new ArrayList<String>();

		this.advertisingChannels=new ArrayList<String>();
		this.altAdvertisingChannels=new ArrayList<NewPositionCDIextraAd>();
		this.extraAdvertising="";

		this.cleanTechnicalAreas();
		this.technicalArea="";

		this.lisboa=this.porto=this.coimbra=false;
		this.extraLocation="";

		this.script=null;
	}

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

		if(this.title.isEmpty()){
			valid=false;
			this.error("Defina um título.");
		}

		if(this.company.isEmpty()){
			valid=false;
			this.error("Defina o nome da Empresa.");
		}

		if(this.description.isEmpty()){
			valid=false;
			this.error("Insira a descrição.");
		}

		if(this.locations.isEmpty()){
			valid=false;
			this.error("Escolha uma localização.");
		}

		if(this.advertisingChannels.isEmpty()){
			valid=false;
			this.error("Escolha os canais de publicidade.");
		}

		if(this.technicalArea.isEmpty()){
			valid=false;
			this.error("Escolha uma área técnica.");
		}

		if(this.script==null){
			valid=false;
			this.error("Defina um guião padrão.");
		}

		if(this.manager==null){
			valid=false;
			this.error("Escolha um gestor.");
		}

		if(this.openings==0){
			valid=false;
			this.error("Defina o número de vagas.");
		}

		if(this.slaDays==0){
			valid=false;
			this.error("Defina o SLA.");
		}

		if(valid){
			UserEntity creator=this.userEJB.findUserByEmail(this.creatorEmail);

			PositionEntity newPositionEntity=new PositionEntity(title, locations,
					openings, Constants.STATUS_OPEN, null, slaDays, manager, creator,
					company, technicalArea, description, advertisingChannels, script);

			this.positionEJB.save(newPositionEntity);

			this.cleanBean();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nova posição criada."));
		}else{
			this.locations.clear();
			this.advertisingChannels.clear();
		}

	}

	private void error(String message){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}
	
	public boolean checkPosition(PositionEntity position){
		if(this.position==null)return false;
		if(this.position.getId()==position.getId())return true;
		return false;
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

	public boolean isCurrentUserTheManager(){
		if(this.position==null)
			return false;
		
		if(this.userManagement.getUserMail().equals(this.position.getPositionManager().getEmail()))
			return true;
		
		return false;
	}
	
	public List<UserEntity> getManagers(){
		return this.userEJB.findAllManagers();
	}

	public void setScripts(List<ScriptEntity> scripts) {
		this.scripts = scripts;
	}

	public List<ScriptEntity> getScripts(){
		return this.scripts;
	}

	public void setScript(ScriptEntity script){
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
		this.advertisingChannels.clear();
		this.critical = critical;
	}

	public boolean isLinkedin() {
		return linkedin;
	}

	public void setLinkedin(boolean linkedin) {
		this.advertisingChannels.clear();
		this.linkedin = linkedin;
	}

	public boolean isGlassdoor() {
		return glassdoor;
	}

	public void setGlassdoor(boolean glassdoor) {
		this.advertisingChannels.clear();
		this.glassdoor = glassdoor;
	}

	public boolean isFacebook() {
		this.advertisingChannels.clear();
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

	// technicalAreas Start
	private void cleanTechnicalAreas(){
		this.sspa=this.dotnet=this.java=this.safety=this.management=this.integration=false;
		this.technicalArea="";
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

	public String getExtraLocation() {
		return extraLocation;
	}

	public void setExtraLocation(String extraLocation) {
		this.locations.clear();
		this.extraLocation = extraLocation;
	}

	public boolean isLisboa() {
		return lisboa;
	}

	public void setLisboa(boolean lisboa) {
		this.locations.clear();
		this.lisboa = lisboa;
	}

	public boolean isPorto() {
		return porto;
	}

	public void setPorto(boolean porto) {
		this.locations.clear();
		this.porto = porto;
	}

	public boolean isCoimbra() {
		return coimbra;
	}

	public void setCoimbra(boolean coimbra) {
		this.locations.clear();
		this.coimbra = coimbra;
	}
	// locations End

	public ScriptEntity getCheckScript() {
		return checkScript;
	}

	public void setCheckScript(ScriptEntity checkScript) {
		this.checkScript = checkScript;
	}

	public boolean isEditPosition() {
		return editPosition;
	}

	public void setEditPosition(boolean editPosition) {
		this.editPosition = editPosition;
	}

	public List<PositionEntity> getOpenPositions() {
		return openPositions;
	}

	public void setOpenPositions(List<PositionEntity> openPositions) {
		this.openPositions = openPositions;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
		this.loadPosition();
	}

	private void loadPosition() {
		this.cleanBean();
		
		this.title=this.position.getTitle();
		
		this.description=this.position.getDescription();
		
		for(String location:this.position.getLocations()){
			if(location.equals(Constants.LOCATION_COIMBRA))this.coimbra=true;
			else if(location.equals(Constants.LOCATION_LISBOA))this.lisboa=true;
			else if(location.equals(Constants.LOCATION_PORTO))this.porto=true;
			else this.extraLocation=location;
		}
		
		for(String ad:this.position.getAdvertisingChannels()){
			if(ad.equals(Constants.SOCIAL_CRITICAL))this.critical=true;
			else if(ad.equals(Constants.SOCIAL_LINKEDIN))this.linkedin=true;
			else if(ad.equals(Constants.SOCIAL_GLASSDOOR))this.glassdoor=true;
			else if(ad.equals(Constants.SOCIAL_LINKEDIN))this.linkedin=true;
			else this.altAdvertisingChannels.add(new NewPositionCDIextraAd(ad));
		}
		
		this.manager=this.position.getPositionManager();
		
		this.script=this.position.getDefaultScript();
	}
	
	public void unloadPosition(){
		this.position=null;
		this.cleanBean();
	}

}
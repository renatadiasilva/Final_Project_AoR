package pt.uc.dei.aor.pf.admin;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import pt.uc.dei.aor.pf.ExtraAd;
import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.mailManagement.SecureMailManagementImp;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

@Named
@SessionScoped
public class NewPositionCDI implements Serializable {

	private static final long serialVersionUID = -5067846817471456766L;

	@Inject
	private UserSessionManagement userManagement;

	@EJB
	private SecureMailManagementImp mail;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private ScriptEJBInterface scriptEJB;

	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private QuestionEJBInterface questionEJB;

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

	private List<ExtraAd>altAdvertisingChannels;

	private String extraAdvertising;

	private boolean critical, linkedin, glassdoor, facebook;

	private List<ScriptEntity>scripts;

	private ScriptEntity script;

	private ScriptEntity checkScript;

	private boolean lisboa, porto, coimbra;

	private boolean editPosition;

	private List<PositionEntity>positions;

	private PositionEntity position;

	private boolean managedPositions;

	private String status;  
	
	private String emptyTable;
	
	private String keyword;

	private Map<String,String> availableStatus=new HashMap<String, String>();

	public void searchPositionsByKeyword() {
		this.position=null;
		String pattern = SearchPattern.preparePattern(keyword);
		this.positions = positionEJB.findPositionsByKeywordShort(pattern,
				Constants.STATUS_OPEN);
	}

	public void searchPositionsByKeywordShort() {
		this.position=null;
		String pattern = SearchPattern.preparePattern(keyword);
		System.out.println("manager "+manager+" "+keyword+" "+pattern);
		this.positions = positionEJB.findPositionsByKeywordAndManager(pattern, manager);
	}

	public void createNewPosition(){
		this.managedPositions=false;
		this.editPosition=false;
		this.cleanBean();
	}

	public void editExistingPosition(){
		this.managedPositions=false;
		this.editPosition=true;
		this.emptyTable="Não foram encontradas posições";
		this.cleanBean();
		this.position=null;
	}

	public void editManagedPositions(){
		this.managedPositions=true;
		this.editPosition=true;
		this.emptyTable="Não foram encontradas posições abertas geridas por"
				+ " este gestor";
		this.cleanBean();
		this.position=null;
	}

	@PostConstruct
	public void cleanBean() {
		// Se é para edição vai buscar as posições
		if(this.editPosition){
			// Vai buscar os Status
			this.availableStatus.put(Constants.STATUS_OPEN, Constants.STATUS_OPEN);
			this.availableStatus.put(Constants.STATUS_ONHOLD, Constants.STATUS_ONHOLD);
			this.availableStatus.put(Constants.STATUS_CLOSED, Constants.STATUS_CLOSED);

			// Se é para um manager editar, só vai buscar as dele (open)
			if(this.managedPositions){
				this.manager=this.userEJB.findUserByEmail(this.userManagement.getUserMail());
				this.positions=this.positionEJB.findPositionsManagedByUser(this.manager);				
			}else this.positions=this.positionEJB.findAllOrderByCode();
		}

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		this.creatorEmail=request.getRemoteUser();

		this.title="";
		this.company="";
		this.description="";

		this.manager=null;

		this.script=null;
		this.scripts=this.scriptEJB.findReusableScripts();

		this.openings=this.slaDays=0;

		this.locations=new ArrayList<String>();

		this.advertisingChannels=new ArrayList<String>();
		this.altAdvertisingChannels=new ArrayList<ExtraAd>();
		this.extraAdvertising="";
		this.advertisingChannels.clear();
		this.critical=this.linkedin=this.glassdoor=this.facebook=false;

		this.cleanTechnicalAreas();
		this.technicalArea="";

		this.lisboa=this.porto=this.coimbra=false;
		this.extraLocation="";
		this.locations.clear();

		this.script=null;
	}

	public void createPosition() {
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
			for(ExtraAd ad:this.altAdvertisingChannels)
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

		if(this.company.isEmpty() && !this.editPosition){
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

		if(this.technicalArea.isEmpty() && !this.editPosition){
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

		if(this.slaDays==0 && !this.editPosition){
			valid=false;
			this.error("Defina o SLA.");
		}

		if(valid){

			if(this.editPosition){

				this.position.setTitle(this.title);

				this.position.setDescription(this.description);

				this.position.setLocations(this.locations);
				
				this.position.setOpenings(this.openings);

				this.position.setAdvertisingChannels(this.advertisingChannels);

				this.position.setPositionManager(this.manager);

				this.position.setDefaultScript(this.script);

				this.positionEJB.update(this.position);

				this.unloadPosition();

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Posição editada."));

			}else{

				UserEntity creator=this.userEJB.findUserByEmail(this.creatorEmail);

				PositionEntity newPositionEntity=new PositionEntity(title, locations,
						openings, Constants.STATUS_OPEN, null, slaDays, manager, creator,
						company, technicalArea, description, advertisingChannels, script);

				this.positionEJB.save(newPositionEntity);
				
				this.mail.newPositionWarning(newPositionEntity);

				this.cleanBean();

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nova posição criada."));

			}

		}else{
			this.locations.clear();
			this.advertisingChannels.clear();
			this.altAdvertisingChannels.clear();
		}

	}

	public boolean isManagedPositions() {
		return managedPositions;
	}

	public void setManagedPositions(boolean managedPositions) {
		this.managedPositions = managedPositions;
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

	public List<ExtraAd> getAltAdvertisingChannels() {
		return altAdvertisingChannels;
	}

	public void addAltAdvertisingChannels() {
		this.altAdvertisingChannels.add(new ExtraAd(this.extraAdvertising));
		this.extraAdvertising="";
	}

	public void deleteAltAdvertisingChannels(ExtraAd ad) {
		this.altAdvertisingChannels.remove(ad);
	}

	public String getExtraAdvertising() {
		return extraAdvertising;
	}

	public void setExtraAdvertising(String extraAdvertising) {
		this.extraAdvertising = extraAdvertising;
	}

	public boolean currentUserTheManager(){
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

	public List<PositionEntity> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionEntity> positions) {
		this.positions = positions;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
		this.loadPosition();
	}

	public boolean loadedPosition(){
		return this.position!=null;
	}

	public boolean loadedPositionAdmin(){
		return this.position!=null&&!this.managedPositions;
	}

	private void loadPosition() {
		this.status=this.position.getStatus();

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
			else if(ad.equals(Constants.SOCIAL_FACEBOOK))this.facebook=true;
			else if(ad.equals(Constants.SOCIAL_GLASSDOOR))this.glassdoor=true;
			else if(ad.equals(Constants.SOCIAL_LINKEDIN))this.linkedin=true;
			else this.altAdvertisingChannels.add(new ExtraAd(ad));
		}

		this.manager=this.position.getPositionManager();

		this.script=this.position.getDefaultScript();

		this.openings=this.position.getOpenings();

	}

	public void updateStatus(){
		// Se o status actual é CLOSED e o novo não, desmarca a closingDate
		if(this.position.getStatus().equals(Constants.STATUS_CLOSED)
		 &&!this.status.equals(Constants.STATUS_CLOSED)) {
			this.position.setClosingDate(null);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Atenção que abriu/pôr em hold"
						+ "uma posição fechada."));
		}

		// Se o novo status é CLOSED e o actual não, a closingDate é agora
		if(this.status.equals(Constants.STATUS_CLOSED)
		 &&!this.position.getStatus().equals(Constants.STATUS_CLOSED)){
			this.position.setStatus(status);
			this.position.setClosingDate(new Date());
		}
		else if(this.status.equals(Constants.STATUS_OPEN))
			this.position.setStatus(status);
		else if(this.status.equals(Constants.STATUS_ONHOLD))
			this.position.setStatus(status);

		this.positionEJB.update(this.position);

		this.position=null;

		this.cleanBean();

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Estado actualizado."));
	}
	
	public String getTypeTextOfQuestion(QuestionEntity question) {
		return questionEJB.getTypeText(question);
	}

	public void unloadPosition(){
		this.position=null;
		this.cleanBean();
	}

	public ScriptEntity getScript() {
		return script;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getAvailableStatus() {
		return availableStatus;
	}

	public void setAvailableStatus(Map<String, String> availableStatus) {
		this.availableStatus = availableStatus;
	}

	public String getEmptyTable() {
		return emptyTable;
	}

	public void setEmptyTable(String emptyTable) {
		this.emptyTable = emptyTable;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
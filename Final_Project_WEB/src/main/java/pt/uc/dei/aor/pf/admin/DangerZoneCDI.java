package pt.uc.dei.aor.pf.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.io.Serializable;

@Named
@SessionScoped
public class DangerZoneCDI implements Serializable {

	private static final long serialVersionUID = 5842976243972113872L;

	private static final Logger log = 
			LoggerFactory.getLogger(UserSearchCDI.class);

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private PositionEJBInterface positionEJB;

	@EJB
	private InterviewEJBInterface interviewEJB;

	@EJB
	private ScriptEJBInterface scriptEJB;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	// to choose
	private UserEntity userToRemove;
	private PositionEntity positionToRemove;
	private ScriptEntity scriptToRemove;

	private List<UserEntity> ulist;
	private List<PositionEntity> plist;
	private List<ScriptEntity> sclist;

	private boolean showInfo;
	private boolean removeTried;
	private String keyword;
	private String tableHeader;
	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 
	private SimpleDateFormat ftHour = new SimpleDateFormat ("HH:mm"); 

	private List<String> info = new ArrayList<String>();	

	// page manipulation methods

	public void clean () {
		userToRemove = null;
		positionToRemove = null;
		scriptToRemove = null;
		showInfo = false;
		removeTried = false;
		keyword = "";
		tableHeader = "";
		info = new ArrayList<String>();
	}

	public void removeUsersDataStart() {
		clean();
		ulist = userEJB.findAllNotRemoved();
	}

	public void removeScriptsStart() {
		clean();
		sclist = scriptEJB.findAll();
	}

	public void removePositionsStart() {
		clean();
		plist = positionEJB.findAll();
	}

	public boolean checkUser(UserEntity u){
		if(this.userToRemove==null)return false;
		if(this.userToRemove.getId()==u.getId())return true;
		return false;
	}

	public void getUsersByKeyword() {
		log.info("Listing users by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findUsersByKeyword(pattern);
	}

	public boolean checkScript(ScriptEntity script){
		if(this.scriptToRemove==null)return false;
		if(this.scriptToRemove.getId()==script.getId())return true;
		return false;
	}

	public void getScriptsByTitle() {
		log.info("Listing scripts by title");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.sclist = scriptEJB.findScriptsByTitle(pattern);
	}

	public boolean checkPosition(PositionEntity position){
		if(this.positionToRemove==null)return false;
		if(this.positionToRemove.getId()==position.getId())return true;
		return false;
	}

	public void getPositionsByKeyword() {
		log.info("Listing positions by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.plist = positionEJB.findPositionsByKeywordShort(pattern, "%");
	}

	// removal methods

	public void listRemovedEmails() {
		info.clear();
		List<UserEntity> list = userEJB.findRemovedEmails();
		for (UserEntity u : list) {
			String emailR = u.getEmail();
			int sizeOfRemovedData = Constants.REMOVED_DATA.length();
			info.add(emailR.substring(0, emailR.length()-sizeOfRemovedData));
		}
	}

	public void removeUser() {
		removeTried = true;
		log.info("Removing user");
		if (userToRemove != null) {
			String userToRemoveEmail = userToRemove.getEmail();
			log.debug("Email "+userToRemoveEmail);

			// protection: don't delete data of superAdmin!
			if (userToRemove.getEmail().equals(Constants.SUPER_ADMIN)) {
				errorMessage("Não é possível apagar"
						+ " os dados do superAdmin!");
				log.info("Failure in removing user: superAdmin");
				return;
			}

			// check if there are positions managed by the given user
			List<PositionEntity> plist =
					positionEJB.findPositionsManagedByUser(userToRemove);
			if (plist != null && !plist.isEmpty()) {
				errorMessage("Não é possível apagar os dados do"
						+ " utilizador.");
				errorMessage("O utilizador é gestor das seguintes"
						+ " posições:");
				tableHeader = "Posições geridas pelo utilizador "
						+userToRemoveEmail;
				showInfo = true;
				for (PositionEntity p : plist)
					info.add("Posição \'"+p.getTitle()+"\' ("+p.getPositionCode()
							+") aberta em "+ p.getOpeningDate());
				log.info("Failure in removing user");
				log.debug("Email "+userToRemoveEmail);
				return;
			}

			// check if there are interviews where user is interviewer
			List<InterviewEntity> ilist = 
					interviewEJB.findInterviewsOfUser(userToRemove);
			if (ilist != null && !ilist.isEmpty()) {
				errorMessage("Não é possível apagar os dados do"
						+ " utilizador.");
				errorMessage("O utilizador é entrevistador das"
						+ " seguintes entrevistas:");
				tableHeader = "Entrevistas em que o utilizador "
						+userToRemoveEmail+" é "
						+ "entrevistador";
				showInfo = true;
				for (InterviewEntity i : ilist)
					info.add("Entrevista do candidato "
							+i.getSubmission().getCandidate().getFirstName()
							+" "+i.getSubmission().getCandidate().getLastName()
							+" para a posição \'"
							+i.getSubmission().getPosition().getPositionCode()
							+"\' (data e hora: "+ftDate.format(i.getDate())
							+", "+ftHour.format(i.getDate())+")");
				log.info("Failure in removing user");
				log.debug("Email "+userToRemoveEmail);
				return;
			}

			// check if there are submissions of user
			List<SubmissionEntity> slist = 
					submissionEJB.findAllSubmissionsOfCandidate(userToRemove);
			if (slist != null && !slist.isEmpty()) {
				errorMessage("Não é possível apagar os dados do"
						+ " utilizador.");
				errorMessage("O utilizador submeteu as"
						+ " seguintes candidaturas:");
				tableHeader = "Candidaturas do utilizador "
						+userToRemoveEmail;
				showInfo = true;
				for (SubmissionEntity s : slist)
					if (s.isSpontaneous())
						info.add("Candidatura espontânea ("
								+"data: "+ftDate.format(s.getDate())+")");
					else
						info.add("Candidatura à posição \'"
							+s.getPosition().getPositionCode()
							+"\' (estado: "+s.getStatus()+")");
				log.info("Failure in removing user");
				log.debug("Email "+userToRemoveEmail);
				return;
			}

			userEJB.deleteData(userToRemove);

			log.info("User data removed");
			log.debug("Email "+userToRemoveEmail);

			removeTried = true;
			notErrorMessage("Os dados do utilizador "
					+ userToRemoveEmail+" foram removidos");

		} else {
			removeTried = false;
			log.error("No chosen user");
			errorMessage("Escolha um utilizador");
		}

	}

	public void removeScript() {
		removeTried = true;
		log.info("Removing script");
		if (scriptToRemove != null) {
			String scriptToRemoveTitle = scriptToRemove.getTitle();
			log.debug("Title "+scriptToRemoveTitle);

			// check if there are positions using the script
			List<PositionEntity> plist = 
					positionEJB.findPositionsByScript(scriptToRemove);
			if (plist != null && !plist.isEmpty()) {
				errorMessage("Não é possível apagar o guião.");
				errorMessage("O guião é guião por defeito nas seguintes"
						+ " posições:");
				tableHeader = "Posições com o guião "
						+scriptToRemoveTitle+" por defeito";
				showInfo = true;
				for (PositionEntity p : plist)
					info.add("Posição \'"+p.getTitle()+"\' ("
							+p.getPositionCode()
							+") aberta em "+ p.getOpeningDate());
				log.info("Failure in removing script");
				log.debug("Title "+scriptToRemoveTitle);
				return;
			}

			// check if there are interviews using script
			List<InterviewEntity> ilist = 
					interviewEJB.findInterviewsWithScript(scriptToRemove);
			if (ilist != null && !ilist.isEmpty()) {
				errorMessage("Não é possível apagar o guião.");
				errorMessage("O guião é/foi usado nas seguintes"
						+ " entrevistas:");
				tableHeader = "Entrevistas que usam o guião"
						+scriptToRemoveTitle;
				showInfo = true;
				for (InterviewEntity i : ilist)
					info.add("Entrevista do candidato "
							+i.getSubmission().getCandidate().getFirstName()
							+" "+i.getSubmission().getCandidate().getLastName()
							+" para a posição \'"
							+i.getSubmission().getPosition().getPositionCode()
							+"\' (data e hora: "+ftDate.format(i.getDate())
							+", "+ftHour.format(i.getDate())+")");
				log.info("Failure in removing script");
				log.debug("Título "+scriptToRemoveTitle);
				return;
			}

			// check if there are script childs
			List<ScriptEntity> sclist = 
					scriptEJB.findChildScripts(scriptToRemove);
			if (sclist != null && !sclist.isEmpty()) {
				errorMessage("Não é possível apagar o guião.");
				errorMessage("Os seguintes scripts são derivados deste guião:");
				tableHeader = "Guiões derivados do guião"
						+scriptToRemoveTitle;
				showInfo = true;
				for (ScriptEntity sc : sclist)
					info.add("Guião "+sc.getTitle()
							+" (criado a"+sc.getCreationDate()+")");
				log.info("Failure in removing script");
				log.debug("Título "+scriptToRemoveTitle);
				return;
			}
			
			scriptEJB.delete(scriptToRemove);

			log.info("Script removed");
			log.debug("Título "+scriptToRemoveTitle);

			removeTried = true;
			notErrorMessage("Guião "+ scriptToRemoveTitle+" removido");

		} else {
			removeTried = false;
			log.error("No chosen script");
			errorMessage("Escolha um guião");
		}
	}

	public void removePosition() {
		removeTried = true;
		log.info("Removing position");
		if (positionToRemove != null) {
			String positionToRemoveCode = positionToRemove.getPositionCode();
			log.debug("Code "+positionToRemoveCode);
			
			// check if there are submissions of position
			List<SubmissionEntity> slist = 
					submissionEJB.findSubmissionsOfPosition(positionToRemove);
			if (slist != null && !slist.isEmpty()) {
				errorMessage("Não é possível apagar a posição.");
				errorMessage("A posição tem as seguintes candidaturas:");
				tableHeader = "Candidaturas da posição "+positionToRemoveCode;
				showInfo = true;
				for (SubmissionEntity s : slist)
					info.add("Candidatura de "+s.getCandidate().getFirstName()
							+" "+s.getCandidate().getLastName()
							+" (estado: "+s.getStatus()+")");
				log.info("Failure in removing position");
				log.debug("Code "+positionToRemoveCode);
				return;
			}

			positionEJB.delete(positionToRemove);

			log.info("Position removed");
			log.debug("Código "+positionToRemoveCode);

			removeTried = true;
			notErrorMessage("Posição "+ positionToRemoveCode+" removida");

		} else {
			removeTried = false;
			log.error("No chosen position");
			errorMessage("Escolha uma posição");
		}
				
	}

	// private methods

	private void notErrorMessage(String message){
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(message));
	}

	private void errorMessage(String message){
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}


	// getters e setters

	public List<String> getInfo() {
		return info;
	}

	public void setInfo(List<String> info) {
		this.info = info;
	}

	public boolean isShowInfo() {
		return showInfo;
	}

	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public UserEntity getUserToRemove() {
		return userToRemove;
	}

	public void setUserToRemove(UserEntity userToRemove) {
		this.userToRemove = userToRemove;
	}

	public PositionEntity getPositionToRemove() {
		return positionToRemove;
	}

	public void setPositionToRemove(PositionEntity positionToRemove) {
		this.positionToRemove = positionToRemove;
	}

	public ScriptEntity getScriptToRemove() {
		return scriptToRemove;
	}

	public void setScriptToRemove(ScriptEntity scriptToRemove) {
		this.scriptToRemove = scriptToRemove;
	}

	public List<UserEntity> getUlist() {
		return ulist;
	}

	public void setUlist(List<UserEntity> ulist) {
		this.ulist = ulist;
	}

	public List<PositionEntity> getPlist() {
		return plist;
	}

	public void setPlist(List<PositionEntity> plist) {
		this.plist = plist;
	}

	public List<ScriptEntity> getSclist() {
		return sclist;
	}

	public void setSclist(List<ScriptEntity> sclist) {
		this.sclist = sclist;
	}

	public String getTableHeader() {
		return tableHeader;
	}

	public void setTableHeader(String tableHeader) {
		this.tableHeader = tableHeader;
	}

	public boolean isRemoveTried() {
		return removeTried;
	}

	public void setRemoveTried(boolean removeTried) {
		this.removeTried = removeTried;
	}

}
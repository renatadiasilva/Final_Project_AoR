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
	private SubmissionEntity submissionToRemove;
	private PositionEntity positionToRemove;
	private ScriptEntity scriptToRemove;
	private InterviewEntity interviewToRemove;

	private List<UserEntity> ulist;
	private List<PositionEntity> plist;
	private List<SubmissionEntity> slist;
	private List<ScriptEntity> sclist;
	private List<InterviewEntity> ilist;

	private boolean showInfo;
	private boolean removeTried;
	private String keyword;
	private String tableHeader;
	private SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 
	private SimpleDateFormat ftHour = new SimpleDateFormat ("HH:mm"); 

	// tirar
	private Long id;

	private List<String> info = new ArrayList<String>();	

	// page manipulation methods

	public void clean () {
		userToRemove = null;
		submissionToRemove = null;
		positionToRemove = null;
		scriptToRemove = null;
		interviewToRemove = null;
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
					submissionEJB.findSubmissionsOfCandidate(userToRemove);
			if (slist != null && !slist.isEmpty()) {
				errorMessage("Não é possível apagar os dados do"
						+ " utilizador.");
				errorMessage("O utilizador tem as"
						+ " seguintes candidaturas submetidas:");
				tableHeader = "Candidaturas do utilizador "
						+userToRemoveEmail;
				showInfo = true;
				for (SubmissionEntity s : slist)
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
		log.info("Removing script by id");
		log.debug("Id "+id);
		ScriptEntity script = scriptEJB.find(id);
		if (script != null) {
			int deleteCode = scriptEJB.delete(script);
			switch (deleteCode) {
			case -1:
				showInfo = true;
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Não pode apagar"
						+ " um guião usado por"
						+ " defeito em posições abertas"));
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Se quiser terá de ir"
						+ " alterar manualmente"
						+ " o guião por defeito de cada uma delas"));
				List<PositionEntity> plist = 
						positionEJB.findOpenPositionsByScript(script);
				tableHeader = "Posições que têm o guião por defeito";
				for (PositionEntity p : plist)
					info.add(p.getPositionCode());
				//adicionar código
				log.info("Failure in removing script");
				log.debug("Id "+id);
				break;
			case -2:
				showInfo = true;
				// verificar como faremos com os scripts...
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Não pode apagar"
						+ " um guião associado a"
						+ " entrevistas agendadas"));
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Se quiser terá de ir"
						+ " alterar manualmente"
						+ " o guião de cada uma delas"));
				tableHeader = "Entrevistas que usam o guião";
				List<InterviewEntity> ilist = 
						interviewEJB.findScheduledInterviewsWithScript(script);
				for (InterviewEntity i : ilist)
					info.add("Entrevista do candidato "
							+i.getSubmission().getCandidate().getFirstName()
							+" "+i.getSubmission().getCandidate().getLastName()
							+" para a posição "
							+i.getSubmission().getPosition().getPositionCode()
							+" (data e hora: "+ftDate.format(i.getDate())
							+", "+ftHour.format(i.getDate())+")");
				//adicionar código
				log.info("Failure in removing script");
				log.debug("Id "+id);
				break;
			default: 				
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Guião removido"));
				log.info("Script removed");
				log.debug("Id "+id);
			}	

		} else {
			log.error("No script with id "+id);
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Não existe guião com id "+id));
		}
	}

	public void removeInterview() {
		log.info("Removing interview by id");
		log.debug("Id "+id);
		InterviewEntity interview = interviewEJB.find(id);
		if (interview != null) {
			if (!interviewEJB.delete(interview)) {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Não pode apagar"
						+ " entrevista com resultados."
						+ " Fale com o gestor da base de dados."));
				log.info("Failure in removing interview");
				log.debug("Id "+id);
			} else {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Entrevista removida"));
				log.info("Interview removed");
				log.debug("Id "+id);
			}
		} else {
			log.error("No interview with id "+id);
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Não existe entrevista com id "
					+id));
		}
	}

	public void removePosition() {
		log.info("Removing position by id");
		log.debug("Id "+id);
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			if (!positionEJB.delete(position)) {
				showInfo = true;
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Não pode apagar"
						+ " posição com candidaturas"));
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Se quiser mesmo terá"
						+ " que as apagar manualmente..."));
				if (position.getStatus().equals(Constants.STATUS_OPEN))
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Não quer em "
							+ "alternativa colocar a "
							+ "posição em hold ou fechá-la?"));
				List<SubmissionEntity> slist = 
						submissionEJB.findSubmissionsOfPosition(position);
				tableHeader = "Candidaturas da posição";
				for (SubmissionEntity s : slist)
					info.add("Candidatura do candidado "
							+s.getCandidate().getFirstName()+" "
							+s.getCandidate().getLastName()+" à posição "
							+s.getPosition().getPositionCode());
				// adicionar código
				log.info("Failure in removing position");
				log.debug("Id "+id);
			} else {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Posição removida"));
				log.info("Position removed");
				log.debug("Id "+id);
			}
		} else {
			log.error("No position with id "+id);
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Não existe posição com id "+id));
		}
	}

	public void removeSubmission() {
		log.info("Removing submission by id");
		log.debug("Id "+id);
		SubmissionEntity submission = submissionEJB.find(id);
		if (submission != null) {
			boolean delSub = true;
			showInfo = true;
			List<InterviewEntity> ilist = 
					interviewEJB.findInterviewsOfSubmission(submission);
			if (ilist != null && !ilist.isEmpty()) {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("A candidatura"
						+ " tem entrevistas."
						+ " Quer mesmo assim removê-la?"));
				// pedir resposta
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Está a apagar"
						+ " automaticamente..."));
				// pedir resposta
				delSub = true; // mudar...

				tableHeader = "Entrevistas da candidatura";
				for (InterviewEntity i : ilist)
					info.add("Entrevista do candidato "
							+i.getSubmission().getCandidate().getFirstName()
							+" "+i.getSubmission().getCandidate().getLastName()
							+" para a posição "
							+i.getSubmission().getPosition().getPositionCode()
							+" (data e hora: "+ftDate.format(i.getDate())
							+", "+ftHour.format(i.getDate())+")");
			}

			if (delSub) {
				submissionEJB.delete(submission);
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Candidatura removida"));
				log.info("Submission removed");
				log.debug("Id "+id);
			} else {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Candidatura NÃO removida"));
				log.info("Submission not removed");
				log.debug("Id "+id);
			}
		} else {
			log.error("No submission with id "+id);
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Não existe candidatura"
					+ " com "+id));
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public SubmissionEntity getSubmissionToRemove() {
		return submissionToRemove;
	}

	public void setSubmissionToRemove(SubmissionEntity submissionToRemove) {
		this.submissionToRemove = submissionToRemove;
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

	public InterviewEntity getInterviewToRemove() {
		return interviewToRemove;
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

	public List<SubmissionEntity> getSlist() {
		return slist;
	}

	public void setSlist(List<SubmissionEntity> slist) {
		this.slist = slist;
	}

	public List<ScriptEntity> getSclist() {
		return sclist;
	}

	public void setSclist(List<ScriptEntity> sclist) {
		this.sclist = sclist;
	}

	public List<InterviewEntity> getIlist() {
		return ilist;
	}

	public void setIlist(List<InterviewEntity> ilist) {
		this.ilist = ilist;
	}

	public void setInterviewToRemove(InterviewEntity interviewToRemove) {
		this.interviewToRemove = interviewToRemove;
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
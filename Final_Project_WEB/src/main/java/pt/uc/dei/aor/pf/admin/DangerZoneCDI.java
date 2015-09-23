package pt.uc.dei.aor.pf.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@Named
@RequestScoped
public class DangerZoneCDI {

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

	// fields
	private Long id;

	private List<String> info = new ArrayList<String>();	

	public void listRemovedEmails() {
		List<UserEntity> list = userEJB.findRemovedEmails();
		for (UserEntity u : list) {
			String emailR = u.getEmail();
			int sizeOfRemovedData = Constants.REMOVED_DATA.length();
			info.add("Email "+
				emailR.substring(0, emailR.length()-sizeOfRemovedData));
		}
	}
	
	public void removeUser() {
		log.info("Removing user by id");
		log.debug("Id "+id);
		UserEntity user = userEJB.find(id);
		if (user != null) {
			int deleteCode = userEJB.delete(user);
			if (deleteCode == -1) {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Não é possível apagar"
						+ " os dados do superAdmin!"
						+ " Fale com o gestor da base de dados"));
				log.info("Failure in removing user");
				log.debug("Id "+id);
			} else {
				// avisar que existem posições que precisam de novo gestor
				if (deleteCode == 1 || deleteCode == 3) {
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Há posições abertas"
							+ " geridas pelo user"));
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Quer mudar os gestores"
							+ " agora ou depois manualmente?"));
					// query repetida... tirar fora????
					List<PositionEntity> plist =
							positionEJB.findOpenPositionsManagedByUser(user);
					for (PositionEntity p : plist)
						info.add("Posição "+p.getPositionCode());
					// adicionar código
				}

				// avisar que existem entrevistas agendadas que
				// preciam novo entrevistador
				if (deleteCode == 2 || deleteCode == 3) {
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Há entrevistas"
							+ " agendadas que só têm"
							+ " como entrevistador o user"));
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Quer adicionar"
							+ " entrevistador agora"
							+ " ou depois manualmente?"));
					// query e for repetidos...
					List<InterviewEntity> ilist = 
							interviewEJB.findScheduledInterviewsByUser(user);
					List<InterviewEntity> list1int = 
							new ArrayList<InterviewEntity>();
					for (InterviewEntity i : ilist) {
						List<UserEntity> ulist = i.getInterviewers();
						// só tem um entrevistador...
						if (ulist != null && ulist.size() == 1) list1int.add(i);
					}
					for (InterviewEntity i : list1int)
						info.add("Entrevista do candidato "
								+i.getSubmission().getCandidate().getFirstName()
								+" "+i.getSubmission().getCandidate().getLastName()
								+" para a posição "
								+i.getSubmission().getPosition().getPositionCode()
								+" (data: "+i.getDate()+")");
					log.info("Failure in removing user");
					log.debug("Id "+id);
					// adicionar código
				}
				log.info("User data removed");
				log.debug("Id "+id);}
				FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Dados do "
							+ "utilizador removidos"));
		} else {
			log.error("No user with id "+id);
			FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Não existe user com id "
						+id));
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
				for (PositionEntity p : plist)
					info.add("Posição "+p.getPositionCode());
				//adicionar código
				log.info("Failure in removing script");
				log.debug("Id "+id);
				break;
			case -2:
				// verificar como faremos com os scripts...
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Não pode apagar"
						+ " um guião associado a"
						+ " entrevistas agendadas"));
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Se quiser terá de ir"
						+ " alterar manualmente"
						+ " o guião de cada uma delas"));
				List<InterviewEntity> ilist = 
						interviewEJB.findScheduledInterviewsWithScript(script);
				for (InterviewEntity i : ilist)
					info.add("Entrevista do candidato "
							+i.getSubmission().getCandidate().getFirstName()
							+" "+i.getSubmission().getCandidate().getLastName()
							+" para a posição "
							+i.getSubmission().getPosition().getPositionCode()
							+" (data: "+i.getDate()+")");
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
				System.out.println(slist);
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

				for (InterviewEntity i : ilist)
					info.add("Entrevista do candidato "
							+i.getSubmission().getCandidate().getFirstName()
							+" "+i.getSubmission().getCandidate().getLastName()
							+" para a posição "
							+i.getSubmission().getPosition().getPositionCode()
							+" (data: "+i.getDate()+")");
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

}
package pt.uc.dei.aor.pf.admin;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
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
	private Long id;

	public void removeUser() {
		log.info("Removing user by id");
		log.debug("Id "+id);
		UserEntity user = userEJB.find(id);
		if (user != null) {
			int deleteCode = userEJB.delete(user);
			switch (deleteCode) {
			case -1:
				System.out.println("Não é possível apagar os dados"
					+ " do superAdmin! Fale com o gestor da base de dados");
				break;
			case -2: 
				// avisar que existem posições que precisam de novo gestor
				System.out.println("Há posições abertas geridas pelo user"
						+ " a apagar");
				System.out.println("Quer mudar os gestores agora ou depois"
						+ " manualmente?");
				// query repetida...
				List<PositionEntity> plist = 
						positionEJB.findOpenPositionsManagedByUser(user);
				System.out.println(plist); // adicionar código
				break;
			case -3:
				// avisar que existem entrevistas agendadas que
				// preciam novo entrevistador
				System.out.println("Há entrevistas agendadas que só têm"
						+ " como entrevistador o user a apagar");
				System.out.println("Quer adicionar entrevistador agora"
						+ " ou depois manualmente?"); 
				// query repetida...
				List<InterviewEntity> ilist = 
						interviewEJB.findScheduledInterviewsByUser(user);
				// falta verificar quais só têm um entrevistador...
				System.out.println(ilist); // adicionar código
				break;
			}
		} else log.error("No user with id "+id);
	}

	public void removeScript() {
		log.info("Removing script by id");
		log.debug("Id "+id);
		ScriptEntity script = scriptEJB.find(id);
		if (script != null) {
			int deleteCode = scriptEJB.delete(script);
			switch (deleteCode) {
			case -1:
				System.out.println("Não pode apagar um guião usado por"
						+ " defeito em posições abertas");
				System.out.println("Se quiser terá de ir alterar manualmente"
						+ " o guião por defeito de cada uma delas");
				List<PositionEntity> plist = 
						positionEJB.findOpenPositionsByScript(script);
				System.out.println(plist); //adicionar código
				break;
			case -2:
				// verificar como faremos com os scripts...
				System.out.println("Não pode apagar um guião associado a"
						+ " entrevistas agendadas");
				System.out.println("Se quiser terá de ir alterar manualmente"
						+ " o guião de cada uma delas");
				List<InterviewEntity> ilist = 
						interviewEJB.findScheduledInterviewsWithScript(script);
				System.out.println(ilist);
			}	

		} else log.error("No script with id "+id);
	}

	public void removeInterview() {
		log.info("Removing interview by id");
		log.debug("Id "+id);
		InterviewEntity interview = interviewEJB.find(id);
		if (interview != null) {
			if (!interviewEJB.delete(interview))
				System.out.println("Não pode apagar entrevista com resultados."
						+ " Fale com o gestor da base de dados.");
		} else log.error("No interview with id "+id);
	}

	public void removePosition() {
		log.info("Removing position by id");
		log.debug("Id "+id);
		PositionEntity position = positionEJB.find(id);
		if (position != null) {
			if (!positionEJB.delete(position)) {
				System.out.println("Não pode apagar posição com candidaturas");
				System.out.println("Se quiser mesmo terá que as apagar"
						+ " manualmente...");
				System.out.println("Não quer em alternativa colocar a "
						+ "posição em hold ou fechá-la?");
				List<SubmissionEntity> slist = 
					submissionEJB.findSubmissionsOfPosition(position);
				System.out.println(slist); // adicionar código
			}
		} else log.error("No position with id "+id);
	}

	public void removeSubmission() {
		log.info("Removing submission by id");
		log.debug("Id "+id);
		SubmissionEntity submission = submissionEJB.find(id);
		if (submission != null) {
			List<InterviewEntity> ilist = 
					interviewEJB.findInterviewsOfSubmission(submission);
			if (ilist != null && !ilist.isEmpty()) {
				System.out.println("A candidatura tem entrevistas."
						+ " Quer mesmo assim removê-la?");
				boolean delSub = true; // pedir resposta
				if (delSub) submissionEJB.delete(submission);
			}
		} else log.error("No submission with id "+id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
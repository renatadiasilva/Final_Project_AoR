package pt.uc.dei.aor.pf.webManagement;

import javax.ejb.Stateless;
import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Stateless
public class EntityFieldLengthsImp implements EntityFieldLengthsInterface {
	private static final Logger log = LoggerFactory.getLogger(EntityFieldLengthsImp.class);

	//Esta classe informa a camada WEB do tamanho máximo dos campos a 
	//preecher pelo utilizador para bater certo com a persistência
	
	//Ex JSF(PrimeFaces) maxlength="#{max.length.userInfoAddress()}"

	//Se houver erro devolve um valor por defeito
	private static final int defaultValue=20;



	//Método Genérico
	private <E> int getLength(Class<E> entity,  String declaredField, String method){
		try {
			return entity.getDeclaredField(declaredField).getAnnotation(Column.class).length();
		} catch (NoSuchFieldException e) {
			log.error("Error getting annotation length: "+method);
		} catch (SecurityException e) {
			log.error("Error getting annotation length: "+method);
		}
		return defaultValue;
	}



	//UserEntity
	@Override
	public int userFirstName() {
		return this.getLength(UserEntity.class, "first_name", "userFirstName");
	}

	@Override
	public int userLastName() {
		return this.getLength(UserEntity.class, "last_name", "userLastName");
	}



	//UserInfoEntity
	@Override
	public int userInfoAddress() {
		return this.getLength(UserInfoEntity.class, "address", "userInfoAddress");
	}

	@Override
	public int userInfoCity() {
		return this.getLength(UserInfoEntity.class, "city", "userInfoCity");
	}

	@Override
	public int userInfoHomePhone() {
		return this.getLength(UserInfoEntity.class, "home_phone", "userInfoHomePhone");
	}

	@Override
	public int userInfoMobilePhone() {
		return this.getLength(UserInfoEntity.class, "mobile_phone", "userInfoMobilePhone");
	}

	@Override
	public int userInfoCountry() {
		return this.getLength(UserInfoEntity.class, "country", "userInfoCountry");
	}

	@Override
	public int userInfoCourse() {
		return this.getLength(UserInfoEntity.class, "course", "userInfoCourse");
	}

	@Override
	public int userInfoSchool() {
		return this.getLength(UserInfoEntity.class, "school", "userInfoSchool");
	}



	//PositionEntity
	@Override
	public int positionTitle() {
		return this.getLength(PositionEntity.class, "title", "positionTitle");
	}

	@Override
	public int positionCompany() {
		return this.getLength(PositionEntity.class, "company", "positionCompany");
	}

	@Override
	public int positionDescription() {
		return this.getLength(PositionEntity.class, "description", "positionDescription");
	}



	//SubmissionEntity
	@Override
	public int submissionRejectReason() {
		return this.getLength(SubmissionEntity.class, "rejected_reason", "submissionRejectReason");
	}



	//InterviewEntity
	@Override
	public int interviewFeedback() {
		return this.getLength(InterviewEntity.class, "feedback", "interviewFeedback");
	}
	
	
	
	//ScriptEntity
	@Override
	public int scriptTitle() {
		return this.getLength(ScriptEntity.class, "title", "scriptTitle");
	}
	
	@Override
	public int scriptComments() {
		return this.getLength(ScriptEntity.class, "comments", "scriptComments");
	}
	
	
	
	//QuestionEntity
	@Override
	public int questionQuestion() {
		return this.getLength(QuestionEntity.class, "question", "questionQuestion");
	}
	
	
	
}

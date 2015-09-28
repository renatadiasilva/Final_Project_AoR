package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.dao.QuestionDao;
import pt.uc.dei.aor.pf.entities.QuestionEntity;

@Stateless
public class QuestionEJBImp implements QuestionEJBInterface {

	private static final Logger log = 
			LoggerFactory.getLogger(QuestionEJBImp.class);
	
	@EJB
	private QuestionDao questionDAO;
	
	@Override
	public void save(QuestionEntity question) {
		log.info("Saving question in DB");
		isQuestionComplete(question);
		questionDAO.save(question);
	}
	
	@Override
	public QuestionEntity saveAndReturn(QuestionEntity question){
		log.info("Saving question in DB and returning persisted question");
		isQuestionComplete(question);
		return questionDAO.saveAndReturn(question);
	}

	@Override
	public void update(QuestionEntity question) {
		log.info("Updating question of DB");
		isQuestionComplete(question);
		questionDAO.update(question);
	}

	@Override
	public void delete(QuestionEntity question) {
		log.info("Deleting question from DB");
		// change something (visibility?)
		questionDAO.update(question);
	}

	@Override
	public QuestionEntity find(Long id) {
		log.info("Finding question by ID");
		return questionDAO.find(id);
	}

	@Override
	public List<QuestionEntity> findAll() {
		log.info("Creating Query for all questions");
		return questionDAO.findAll();
	}

	@Override
	public String getTypeText(QuestionEntity question) {
		if (question.getType().equals(QuestionEntity.VALUE)) 
			return Constants.QUESTION_VALUE;
		if (question.getType().equals(QuestionEntity.ISTRUE)) 
			return Constants.QUESTION_ISTRUE;
		if (question.getType().equals(QuestionEntity.ANSWER)) 
			return Constants.QUESTION_ASWER;
		return "";
	}

	private void isQuestionComplete(QuestionEntity question) {
		boolean hasError = false;
		
		if (question == null) hasError = true;
		else if (question.getType() == null) hasError = true;
		else if (question.getQuestion() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The question is missing data. "
					+ "Check the notnull attributes.");
	}

}

package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.AnswerDao;
import pt.uc.dei.aor.pf.entities.AnswerEntity;

@Stateless
public class AnswerEJBImp implements AnswerEJBInterface {

	private static final Logger log =
			LoggerFactory.getLogger(AnswerEJBImp.class);
	
	@EJB
	private AnswerDao answerDAO;
	
	@Override
	public void save(AnswerEntity answer) {
		log.info("Saving answer in DB");
		isAnswerComplete(answer);
		answerDAO.save(answer);
	}

	@Override
	public void update(AnswerEntity answer) {
		log.info("Updating answer of DB");
		isAnswerComplete(answer);
		answerDAO.update(answer);
	}

	@Override
	public void delete(AnswerEntity answer) {
		log.info("Deleting answer from DB");
		// change something (visibility?)
		answerDAO.update(answer);
	}

	@Override
	public AnswerEntity find(Long id) {
		log.info("Finding answer by ID");
		return answerDAO.find(id);
	}

	@Override
	public List<AnswerEntity> findAll() {
		log.info("Creating Query for all answers");
		return answerDAO.findAll();
	}

	private void isAnswerComplete(AnswerEntity answer) {
		boolean hasError = false;
		
		if (answer == null) hasError = true;
		else if (answer.getInterview() == null) hasError = true;
		else if (answer.getQuestion() == null) hasError = true;

		if (hasError)
			throw new IllegalArgumentException("The interview is missing data. "
					+ "Check the notnull attributes.");
	}

}
package pt.uc.dei.aor.pf.beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.dao.AnswerDAO;
import pt.uc.dei.aor.pf.entities.AnswerEntity;

@Stateless
public class AnswerEJBImp implements AnswerEJBInterface {

	private static final Logger log = LoggerFactory.getLogger(AnswerEJBImp.class);
	
	@EJB
	private AnswerDAO answerDAO;
	
	@Override
	public void save(AnswerEntity answer) {
		log.info("Saving answer in DB");
		answerDAO.save(answer);
	}

	@Override
	public void update(AnswerEntity answer) {
		log.info("Updating answer of DB");
		answerDAO.update(answer);
	}

	@Override
	public void delete(AnswerEntity answer) {
		log.info("Deleting answer from DB");
		answerDAO.delete(answer.getId(), AnswerEntity.class);
	}

	@Override
	public AnswerEntity find(Long id) {
		log.info("Finding answer by ID");
		return answerDAO.find(id);
	}

}
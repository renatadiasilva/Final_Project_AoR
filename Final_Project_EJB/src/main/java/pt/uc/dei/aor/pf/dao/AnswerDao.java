package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.AnswerEntity;

@Stateless
public class AnswerDAO extends GenericDAO<AnswerEntity> {

	public AnswerDAO() {
		super(AnswerEntity.class);
	}

}

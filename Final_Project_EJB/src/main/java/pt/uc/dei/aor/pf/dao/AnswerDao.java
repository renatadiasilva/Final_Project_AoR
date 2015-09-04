package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.AnswerEntity;

@Stateless
public class AnswerDao extends GenericDao<AnswerEntity> {

	public AnswerDao() {
		super(AnswerEntity.class);
	}

}

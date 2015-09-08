package pt.uc.dei.aor.pf.dao;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.QuestionEntity;

@Stateless
public class QuestionDao extends GenericDao<QuestionEntity> {

	public QuestionDao() {
		super(QuestionEntity.class);
	}

}

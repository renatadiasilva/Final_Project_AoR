package pt.uc.dei.aor.pf.beans;

import java.util.List;

import pt.uc.dei.aor.pf.entities.QuestionEntity;

public interface QuestionEJBInterface {
	
	public abstract void save(QuestionEntity question);
	public abstract void update(QuestionEntity question);
	public abstract void delete(QuestionEntity question);
	public abstract QuestionEntity find(Long id);
	public abstract List<QuestionEntity> findAll();
	public abstract QuestionEntity saveAndReturn(QuestionEntity question); 
	
}

package pt.uc.dei.aor.pf.beans;

import java.util.List;

import pt.uc.dei.aor.pf.entities.AnswerEntity;

public interface AnswerEJBInterface {
	
	public abstract void save(AnswerEntity answer);
	public abstract void update(AnswerEntity answer);
	public abstract void delete(AnswerEntity answer);
	public abstract AnswerEntity find(Long id);
	public abstract List<AnswerEntity> findAll();
	
}

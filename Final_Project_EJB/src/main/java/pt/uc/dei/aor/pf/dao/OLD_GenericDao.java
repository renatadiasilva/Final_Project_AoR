package pt.uc.dei.aor.pf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

public abstract class OLD_GenericDao<O> {

	@PersistenceContext(unitName = "ProjetoFinal")
	protected EntityManager em;

	private Class<O> entityClass;

	public OLD_GenericDao(Class<O> entityClass) {
		this.entityClass = entityClass;
	}

	public void save(O entity) {
		em.persist(entity);
	}

	public void delete(Object id, Class<O> classe) {
		O entityRemover = em.getReference(classe, id);
		em.remove(entityRemover);
	}

	public O update(O entity) {
		return em.merge(entity);
	}

	public O find(int entityID) {
		return em.find(entityClass, entityID);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<O> findAll() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}
	
	//usar mais NamedQueries


}

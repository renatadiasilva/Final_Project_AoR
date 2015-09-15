package pt.uc.dei.aor.pf.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public abstract class GenericDao<E> {

	@PersistenceContext(unitName = "ProjetoFinal")
	protected EntityManager em;

	private Class<E> entityClass;
	
	// used in accent insensitive searchs
	static final String ACCENT_LETTERS    = "\'ÀÁÂÃÄÅĀĂĄÉÊĒĔĖĘĚÌÍÎÏÌĨĪĬÒÓÔÕÖŌŎŐÙÚÛÜŨŪŬŮÇ\'";
	static final String NO_ACCENT_LETTERS = "\'AAAAAAAAAEEEEEEEIIIIIIIIOOOOOOOOUUUUUUUUC\'";	
	
	public GenericDao(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public void save(E entity) {
		em.persist(entity);
	}

	public void delete(Object id, Class<E> cla) {
		E entityToBeRemoved = em.getReference(cla, id);
		em.remove(entityToBeRemoved);
	}

	public E update(E entity) {
		return em.merge(entity);
	}

	public E find(Long entityID) {
		return em.find(entityClass, entityID);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<E> findAll() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected List<E> findSomeResults(String namedQuery, Map<String, Object> parameters) {
		Query nq = em.createNamedQuery(namedQuery);
		if (parameters != null && !parameters.isEmpty())
			populateQueryParameters(nq, parameters);
		return nq.getResultList();
	}

	protected static String makeQuery(String selectPart, String fromPart,
			String extraPart, String[] attributesPart, String[] valuesPart,
			String AND_OR, String wherePart, String orderbyPart) {
		String query = "SELECT "+selectPart+" FROM "+fromPart+" WHERE "
			+ extraPart+"TRANSLATE(UPPER(REPLACE("+attributesPart[0]
			+",\' \',\'\')), "+ACCENT_LETTERS+","+NO_ACCENT_LETTERS
			+") LIKE \'"+valuesPart[0]+"\'";
		
		for (int i = 1; i < attributesPart.length; i++) 
			query += AND_OR+"TRANSLATE(UPPER(REPLACE("+attributesPart[i]
				+",\' \',\'\')), "+ACCENT_LETTERS+","+NO_ACCENT_LETTERS
				+") LIKE \'"+valuesPart[i]+"\'";
		query += ")";
		if (!wherePart.isEmpty()) query +=" AND "+wherePart;
		if (!orderbyPart.isEmpty()) query +=" ORDER BY "+orderbyPart;
		return query;
	}

	private void populateQueryParameters(Query query, Map<String, Object> parameters) {
		for (Entry<String, Object> entry : parameters.entrySet())
			query.setParameter(entry.getKey(), entry.getValue());
	}

}

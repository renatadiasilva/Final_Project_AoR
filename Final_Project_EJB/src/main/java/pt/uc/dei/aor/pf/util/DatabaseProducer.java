package pt.uc.dei.aor.pf.util;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DatabaseProducer {

	@Produces
	@PersistenceContext(unitName = "ProjetoFinal")
	private EntityManager em;
}

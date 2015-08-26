package pt.uc.dei.aor.pf.dao;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pt.uc.dei.aor.pf.entities.Musica;
import pt.uc.dei.aor.pf.entities.Utilizador;


@RunWith(MockitoJUnitRunner.class)
public class UserDaoTest {

	@Mock
	EntityManager em;

	@Mock
	Query mockedQuery;

	@Mock
	Utilizador utilizador;

	@InjectMocks
	UserDao ejb;

	@Test
	public void fazLoginPorUtilizador() {

		final String QUERY = "select u from Utilizador u where u.mail=:mailparam";
		when(mockedQuery.getResultList()).thenReturn(new ArrayList<Musica>());
		when(em.createQuery(QUERY)).thenReturn(mockedQuery);

		when(utilizador.getMail()).thenReturn("mail");
//		when(utilizador.getPassword()).thenReturn("pass");

		utilizador = ejb.login(utilizador);
		verify(mockedQuery).setParameter("mailparam", "mail");
//		verify(mockedQuery).setParameter("passparam", "pass");
		verify(mockedQuery).getSingleResult();
		verify(em).createQuery(QUERY);

	}

	@Test
	public void VerificaSeExisteUtilizadorComNomeDado() {

		boolean existe;
		final String QUERY = "select u.mail from Utilizador u where u.mail= :mailParam";

		when(mockedQuery.getSingleResult()).thenReturn("emailTemp");
		when(em.createQuery(QUERY)).thenReturn(mockedQuery);

		existe = ejb.checkEmail("emailTemp");
		verify(mockedQuery).setParameter("mailParam", "emailTemp");
		verify(mockedQuery).getSingleResult();
		verify(em).createQuery(QUERY);
		MatcherAssert.assertThat(existe, Matchers.is(Matchers.equalTo(true)));

	}

	@Test
	public void fazLoginPorUtilizadorComExcepcao() {

		final String QUERY = "select u from Utilizador u where u.mail=:mailparam";

		Mockito.doThrow(new NoResultException("Sem resultados"))
				.when(mockedQuery).getSingleResult();

		when(em.createQuery(QUERY)).thenReturn(mockedQuery);

		when(utilizador.getMail()).thenReturn("mail");
//		when(utilizador.getPassword()).thenReturn("pass");

		utilizador = ejb.login(utilizador);
		verify(mockedQuery).setParameter("mailparam", "mail");
//		verify(mockedQuery).setParameter("passparam", "pass");
		verify(mockedQuery).getSingleResult();
		verify(em).createQuery(QUERY);

	}

}

package pt.uc.dei.aor.pf.dao;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
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
import pt.uc.dei.aor.pf.entities.Playlist;
import pt.uc.dei.aor.pf.entities.Utilizador;


@RunWith(MockitoJUnitRunner.class)
public class PlaylistDaoTest {

	@Mock
	EntityManager em;

	@Mock
	Query mockedQuery;
	@Mock
	NamedQuery mockedNamedQuery;

	@Mock
	Playlist playlist;
	@Mock
	Musica musica;
	@Mock
	Utilizador utilizador;


	@InjectMocks
	PlaylistDao ejb;


	@Test
	public void  findAllByUserTemDevolverTodasAsPlaylistsDeUmUtilizador(){
		@SuppressWarnings("unused")
		List<Playlist> listaPlaylists;
		final String QUERY = "from Playlist p where p.dono.id = :userId";

		when(mockedQuery.getResultList()).thenReturn(new ArrayList<Playlist>());
		when(em.createQuery(QUERY)).thenReturn(mockedQuery);

		when(utilizador.getId()).thenReturn(1);

		listaPlaylists=ejb.findAllByUser(utilizador);
		verify(mockedQuery).setParameter("userId", 1);
		verify(mockedQuery).getResultList();
		verify(em).createQuery(QUERY);

	}

	@Test
	public void  playlistsOrdASCNomeTemDevolverUmaPlaylist(){
		@SuppressWarnings("unused")
		List<Playlist> listaPlaylists;
		final String NQUERYAsc = "PlaylistName.ASC";
		

		when(((Query) mockedQuery).getResultList()).thenReturn(new ArrayList<Playlist>());
		when(em.createNamedQuery(NQUERYAsc)).thenReturn((Query)mockedQuery);

		when(utilizador.getId()).thenReturn(1);

		
		listaPlaylists=ejb.playlistsOrdNome(utilizador,"asc");
		verify((Query) mockedQuery).setParameter("userId", 1);
//		verify((Query)mockedNamedQuery).setParameter("sentido", "asc");
		verify(mockedQuery).getResultList();
		verify(em).createNamedQuery(NQUERYAsc);

	}

	@Test
	public void  playlistsOrdDescNomeTemDevolverUmaPlaylist(){
		@SuppressWarnings("unused")
		List<Playlist> listaPlaylists;
		
		final String NQUERYDesc = "PlaylistName.DESC";

		when(((Query) mockedQuery).getResultList()).thenReturn(new ArrayList<Playlist>());
		when(em.createNamedQuery(NQUERYDesc)).thenReturn((Query)mockedQuery);

		when(utilizador.getId()).thenReturn(1);
		
		listaPlaylists=ejb.playlistsOrdNome(utilizador,"desc");
		verify((Query) mockedQuery).setParameter("userId", 1);
		verify(mockedQuery).getResultList();
		verify(em).createNamedQuery(NQUERYDesc);

	}
	
	@Test
	public void  playlistsOrdDescDataTemDevolverUmaPlaylist(){
		@SuppressWarnings("unused")
		List<Playlist> listaPlaylists;
		
		final String NQUERYDesc = "PlaylistData.DESC";

		when(((Query) mockedQuery).getResultList()).thenReturn(new ArrayList<Playlist>());
		when(em.createNamedQuery(NQUERYDesc)).thenReturn((Query)mockedQuery);

		when(utilizador.getId()).thenReturn(1);
		
		listaPlaylists=ejb.playlistsOrdData(utilizador,"desc");
		verify((Query) mockedQuery).setParameter("userId", 1);
		verify(mockedQuery).getResultList();
		verify(em).createNamedQuery(NQUERYDesc);

	}
	
	@Test
	public void  playlistsOrdAscDataTemDevolverUmaPlaylist(){
		@SuppressWarnings("unused")
		List<Playlist> listaPlaylists;
		
		final String NQUERYAsc = "PlaylistData.ASC";

		when(((Query) mockedQuery).getResultList()).thenReturn(new ArrayList<Playlist>());
		when(em.createNamedQuery(NQUERYAsc)).thenReturn((Query)mockedQuery);

		when(utilizador.getId()).thenReturn(1);
		
		listaPlaylists=ejb.playlistsOrdData(utilizador,"asc");
		verify((Query) mockedQuery).setParameter("userId", 1);
		verify(mockedQuery).getResultList();
		verify(em).createNamedQuery(NQUERYAsc);

	}

	@Test
	public void  playlistsOrdAscTamanhoTemDevolverUmaPlaylist(){
		@SuppressWarnings("unused")
		List<Playlist> listaPlaylists;
		
		final String NQUERYAsc = "PlaylistTamanho.ASC";

		when(((Query) mockedQuery).getResultList()).thenReturn(new ArrayList<Playlist>());
		when(em.createNamedQuery(NQUERYAsc)).thenReturn((Query)mockedQuery);

		when(utilizador.getId()).thenReturn(1);
		
		listaPlaylists=ejb.playlistsOrdTamanho(utilizador,"asc");
		verify((Query) mockedQuery).setParameter("userId", 1);
		verify(mockedQuery).getResultList();
		verify(em).createNamedQuery(NQUERYAsc);

	}

	@Test
	public void  playlistsOrdDescTamanhoTemDevolverUmaPlaylist(){
		@SuppressWarnings("unused")
		List<Playlist> listaPlaylists;
		
		final String NQUERYDesc = "PlaylistTamanho.DESC";

		when(((Query) mockedQuery).getResultList()).thenReturn(new ArrayList<Playlist>());
		when(em.createNamedQuery(NQUERYDesc)).thenReturn((Query)mockedQuery);

		when(utilizador.getId()).thenReturn(1);
		
		listaPlaylists=ejb.playlistsOrdTamanho(utilizador,"desc");
		verify((Query) mockedQuery).setParameter("userId", 1);
		verify(mockedQuery).getResultList();
		verify(em).createNamedQuery(NQUERYDesc);

	}
	
	@Test
	public void  VerificaSeExistePlaylistComNomeDado(){
				
		boolean existe;
		final String QUERY = "select p.nome from Playlist p where p.nome= :nomeParam";
		
		when(mockedQuery.getSingleResult()).thenReturn("nomeParam");
      	when(em.createQuery(QUERY)).thenReturn(mockedQuery);
       
        existe=ejb.checkPlaylist("nomeParam");
        verify(mockedQuery).setParameter("nomeParam", "nomeParam");
        verify(mockedQuery).getSingleResult();
        verify(em).createQuery(QUERY);
        MatcherAssert.assertThat(existe, Matchers.is(Matchers.equalTo(true)));

	}
	
	@Test
	public void  SENomePlaylistNaoExiste(){
				
		@SuppressWarnings("unused")
		boolean existe;
		final String QUERY = "select p.nome from Playlist p where p.nome= :nomeParam";
		
		when(mockedQuery.getSingleResult()).thenReturn("NoResultException");
      	when(em.createQuery(QUERY)).thenReturn(mockedQuery);
       
        existe=ejb.checkPlaylist("alice");
        verify(mockedQuery).setParameter("nomeParam", "alice");
        verify(mockedQuery).getSingleResult();
        verify(em).createQuery(QUERY);
//       
	}
	
	@Test
    public void  VerificaSeExistePlaylistComNomeDadoAtravesExcepcao(){
               
        boolean existe;
        final String QUERY = "select p.nome from Playlist p where p.nome= :nomeParam";
       
        Mockito.doThrow(new NoResultException("Sem resultados")).when(mockedQuery).getSingleResult();
         when(em.createQuery(QUERY)).thenReturn(mockedQuery);
      
        existe=ejb.checkPlaylist("nomeParam");
        verify(mockedQuery).setParameter("nomeParam", "nomeParam");
        verify(mockedQuery).getSingleResult();
        verify(em).createQuery(QUERY);
        MatcherAssert.assertThat(existe, Matchers.is(false));


    }
	
}

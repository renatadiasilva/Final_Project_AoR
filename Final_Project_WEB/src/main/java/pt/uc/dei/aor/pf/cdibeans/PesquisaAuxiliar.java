package pt.uc.dei.aor.pf.cdibeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import pt.uc.dei.aor.pf.beans.MusicaInterface;
import pt.uc.dei.aor.pf.beans.PlaylistInterface;
import pt.uc.dei.aor.pf.entities.Musica;
import pt.uc.dei.aor.pf.entities.Playlist;

@Named
@SessionScoped
public class PesquisaAuxiliar implements Serializable {

	private static final long serialVersionUID = 1186970608311468603L;

	@Inject
	MusicaInterface musicaBean;
	@Inject
	PlaylistInterface playlistBean;
	@Inject
	UserSession user;
	@Inject
	MusicaCDIBean musicaCDIBean;
	@Inject
	PlaylistCDIBean playCDIbean;
	private String selectedItem;
	private String selectedItemTitulo;
	// private String[] selectedItemTituloArtista;
	private List<String> listaArtistas;
	private List<String> listaTitulos;
	private List<String> listaTitulosPorArtista;
	private List<String> listanomesPlaylists;
	private List<Playlist> listaPlayLists;

	private Musica musicaSeleccionada;

	public List<Musica> getTodasMusicas() {
		return musicaCDIBean.getTodasMusicas();
	}

	private String artistaSeleccionado;
	private String playListSeleccionada;
	private String tituloSeleccionadoPorArtista;
	private Musica musicaArtistaTitulo;

	private List<Musica> listaPesquisa;

	@PostConstruct
	public void init() {
		listaArtistas = new ArrayList<String>();
		listaTitulos = new ArrayList<String>();
		listaTitulosPorArtista = new ArrayList<String>();
		listaArtistas = findListaArtistas();
		listaTitulos = findListaTitulos();
		listaPlayLists = new ArrayList<Playlist>();
		listanomesPlaylists = new ArrayList<String>();
		musicaSeleccionada = null;

		listaPesquisa = new ArrayList<Musica>();
	}
	
	public void refreshLists(){
		listaTitulos = findListaTitulos();
		listaArtistas = findListaArtistas();
	}

	// ----------------------------------ARTISTAS-----------------------------------------
	// Procura a lista de todos os artistas existentes na aplicação
	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;

	}

	public void findMusicaByArtista() {
		this.listaPesquisa = musicaBean.findMusicaByArtista(this.selectedItem);
		RequestContext.getCurrentInstance().update(Arrays.asList("display"));
	}

	public List<String> findListaArtistas() {
		List<String> artistas = musicaBean.findListaArtistas();
		return artistas;
	}

	// todas as musicas da aplicação por artista
	public List<String> getListaArtistas() {
		return listaArtistas;
	}

	// ----------------------------------TITULO-----------------------------------------
	// Procura a lista de todos os titulos existentes na aplicação
	public String getSelectedItemTitulo() {
		return selectedItemTitulo;
	}

	public void setSelectedItemTitulo(String selectedItemTitulo) {
		this.selectedItemTitulo = selectedItemTitulo;
		// String titulo=this.selectedItemTitulo;
		// this.todasMusicasByTitulo=musicaBean.findMusicaByTitulo(titulo);
	}

	public List<String> findListaTitulos() {
		List<String> titulos = musicaBean.findListaTitulos();
		return titulos;
	}

	// todas as musicas da aplicação por titulos
	public List<String> getListaTitulos() {
		return listaTitulos;
	}

	public void findMusicaByTitulo() {
		this.listaPesquisa = musicaBean
				.findMusicaByTitulo(this.selectedItemTitulo);
	}

	// ----------------------------------TITULO&&Artistas-----------------------------------------
	// Artista seleccionado

	public String getArtistaSeleccionado() {
		return artistaSeleccionado;
	}

	public void setArtistaSeleccionado(String artistaSeleccionado) {
		this.artistaSeleccionado = artistaSeleccionado;
		this.listaTitulosPorArtista = musicaBean
				.findListaTitulosPorArtista(artistaSeleccionado);
	}

	// lista de titulos por artista
	public List<String> getListaTitulosPorArtista() {
		return this.listaTitulosPorArtista;
	}

	// selecção de titulo
	public String getTituloSeleccionadoPorArtista() {
		return tituloSeleccionadoPorArtista;
	}

	public void setTituloSeleccionadoPorArtista(
			String tituloSeleccionadoPorArtista) {
		this.tituloSeleccionadoPorArtista = tituloSeleccionadoPorArtista;
	}

	public void procuraMusica() {
		listaPesquisa.clear();
		listaPesquisa.add(musicaBean.findMusicaByTituloArtista(
				this.artistaSeleccionado, this.tituloSeleccionadoPorArtista));
	}

	public Musica getMusicaArtistaTitulo() {
		return this.musicaArtistaTitulo;
	}

	public void onArtistaChange() {
		this.listaTitulosPorArtista = musicaBean
				.findListaTitulosPorArtista(artistaSeleccionado);
		listaPesquisa = musicaBean
				.findMusicaByArtista(this.artistaSeleccionado);
	}

	// ----------------------------------PlayLists-----------------------------------------
	// Procura a lista de todas as playlists do utilizador

	public void consultarListasByDono() {
		this.listaPlayLists = playlistBean.allPlaylists(user.getCurrent());
	}

	public List<String> getListanomesPlaylists() {
		getlistaPlayLists();
		listanomesPlaylists.clear();
		for (Playlist p : listaPlayLists)
			this.listanomesPlaylists.add(p.getNome());
		return this.listanomesPlaylists;
	}

	// Adicionar musica à PlayList
	public void adicionarMusicaPlayList() {
		System.out.println("****************** playlist: "+playListSeleccionada);
		Playlist playlist = null;

		for (Playlist p : listaPlayLists) {
			if (p.getNome().compareTo(this.playListSeleccionada) == 0) {
				playlist = p;
			}
		}

		if (musicaSeleccionada != null) {
				
			boolean success = playlistBean.adicionaMusica(this.musicaSeleccionada, playlist);
			if (success) {
				playCDIbean.getData();
				musicaSeleccionada = null;
				musicaCDIBean.setMusicaSeleccionada(null);
				selectedItemTitulo = null;
				artistaSeleccionado = null;
				playListSeleccionada=null;
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"Music added successfully.", "");
				FacesContext.getCurrentInstance().addMessage(null, message);
			} else {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"This music is already in the selected playlist", "");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}

		} else {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "No music selected.", "");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		RequestContext.getCurrentInstance().update(Arrays.asList("right-bar"));
	}

	// Nome da PlayList Seleccionada
	public String getPlayListSeleccionada() {
		return playListSeleccionada;
	}

	public void setPlayListSeleccionada(String playListSeleccionada) {
		this.playListSeleccionada = playListSeleccionada;
	}

	// Musica a adicionar
	public Musica getMusicaSeleccionada() {
		return musicaSeleccionada;
	}

	public void setMusicaSeleccionada(Musica musicaSeleccionada) {
		this.musicaSeleccionada = musicaSeleccionada;
	}

	public void getlistaPlayLists() {
		consultarListasByDono();

	}

	public void onRowSelect(SelectEvent event) {
		musicaCDIBean.setMusicaSeleccionada(null);

	}

	public List<Musica> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<Musica> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

}

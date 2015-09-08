package pt.uc.dei.aor.pf.cdibeans;

import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import javax.enterprise.context.SessionScoped;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import org.primefaces.context.RequestContext;
//import org.primefaces.event.SelectEvent;
//
//import pt.uc.dei.aor.pf.beans.PlaylistInterface;
//import pt.uc.dei.aor.pf.entities.Musica;
//import pt.uc.dei.aor.pf.entities.Playlist;
//import pt.uc.dei.aor.pf.util.PageHandler;

//@Named
//@SessionScoped
public class PlaylistCDIBean implements Serializable {

	private static final long serialVersionUID = 1L;
//	@Inject
//	UserSession user;
//	@Inject
//	private PlaylistInterface playBean;
//	private Playlist playlist;
//	private ArrayList<Playlist> playlists;
//	private Playlist selectedPlaylist;
//	private String direccao;
//	private String sortedBy;
//	@Inject
//	PageHandler pageHandler;
//	@Inject
//	PesquisaAuxiliar pesqAux;
//	private Musica musicaRemover;
//	private Musica musicaPlay;
//	private String url;
//	private String tempName;
//	private String nomeInicial;
//
//	public PlaylistCDIBean() {
//		playlist = new Playlist();
//		direccao = null;
//		sortedBy = null;
//	}
//
//	public void init() {
//		if (user.isLogged())
//			getData();
//	}
//
//	public void getData() {
//		if (sortedBy == null || direccao == null) {
//			playlists = (ArrayList<Playlist>) playBean.allPlaylists(user
//					.getCurrent());
//		} else if (sortedBy.equals("name")) {
//			sortName(direccao);
//		} else if (sortedBy.equals("date")) {
//			sortDate(direccao);
//		} else if (sortedBy.equals("size")) {
//			sortDate(direccao);
//		} else {
//			playlists = (ArrayList<Playlist>) playBean.allPlaylists(user
//					.getCurrent());
//		}
//	}
//
//	public void sortName(String dir) {
//		direccao = dir;
//		playlists = (ArrayList<Playlist>) playBean.playlistOrdenadoNome(
//				user.getCurrent(), direccao);
//		sortedBy = "name";
//	}
//
//	public void sortDate(String dir) {
//		direccao = dir;
//		playlists = (ArrayList<Playlist>) playBean.playlistOrdenadoData(
//				user.getCurrent(), direccao);
//		sortedBy = "date";
//	}
//
//	public void sortSize(String dir) {
//		direccao = dir;
//		playlists = (ArrayList<Playlist>) playBean.playlistOrdenadoTamanho(
//				user.getCurrent(), direccao);
//		sortedBy = "size";
//	}
//
//	public void save() {
//		if (!checkPlaylistName(tempName)) {
//			try {
//				playlist.setDono(user.getCurrent());
//				playlist.setNome(tempName);
//				playBean.save(playlist);
//				getData();
//				playlist = new Playlist();
//				tempName="";
//				FacesMessage message = new FacesMessage(
//						FacesMessage.SEVERITY_INFO,
//						"Playlist created successfully.", "");
//				FacesContext.getCurrentInstance().addMessage(null, message);
//				// fazer novo get no pesquisa auxiliar
//				pesqAux.getListanomesPlaylists();
//			} catch (Exception e) {
//				FacesMessage message = new FacesMessage(
//						FacesMessage.SEVERITY_ERROR,
//						"Error creating playlist.", "");
//				FacesContext.getCurrentInstance().addMessage(null, message);
//			}
//		} else {
//			FacesMessage message = new FacesMessage(
//					FacesMessage.SEVERITY_ERROR,
//					"Playlist with same name already exists.", "");
//			tempName="";
//			RequestContext.getCurrentInstance().update(
//					Arrays.asList("form-edit-playlist"));
//			FacesContext.getCurrentInstance().addMessage(null, message);
//		}
//	}
//
//	public void edit() {
//		playBean.update(selectedPlaylist);
//		getData();
//		selectedPlaylist = null;
//		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
//				"Playlist updated successfully.", "");
//		FacesContext.getCurrentInstance().addMessage(null, message);
//		// fazer novo get no pesquisa auxiliar
//		pesqAux.getListanomesPlaylists();
//	}
//
//	public void onRowSelect(SelectEvent event) {
//		nomeInicial = selectedPlaylist.getNome();
//		pageHandler.setPage("musicas_playlist");
//		RequestContext.getCurrentInstance().update(
//				Arrays.asList("right-panel", "form-nav:toolbar-nav"));
//	}
//
//	public void excluir() {
//		playBean.delete(selectedPlaylist);
//		getData();
//		selectedPlaylist = null;
//		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
//				"Playlist deleted successfully.", "");
//		FacesContext.getCurrentInstance().addMessage(null, message);
//		// fazer novo get no pesquisa auxiliar
//		pesqAux.getListanomesPlaylists();
//	}
//
//	public void removeDaPlaylist() {
//		playBean.removeMusica(selectedPlaylist, musicaRemover);
//		getData();
//		musicaRemover = null;
//	}
//
//	public void play() {
//		url = "http://localhost:8080";
//		url += musicaPlay.getFilePath();
//	}
//
//	public void updateName() {
//		boolean existe = checkPlaylistName(nomeInicial);
//		if (!existe) {
//			selectedPlaylist.setNome(nomeInicial);
//			edit();
//		} else {
//			nomeInicial = selectedPlaylist.getNome();
//			RequestContext.getCurrentInstance().update(
//					Arrays.asList("form-edit-playlist"));
//			FacesMessage message = new FacesMessage(
//					FacesMessage.SEVERITY_ERROR,
//					"A Playlist with the same name already exists.", "");
//			FacesContext.getCurrentInstance().addMessage(null, message);
//		}
//		
//	}
//
//	public boolean checkPlaylistName(String nome) {
//		for (Playlist p : playlists) {
//			if (p.getNome().equals(nome)) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	// ************** Getters & Setters *********************//
//
//	public ArrayList<Playlist> getPlaylists() {
//		return playlists;
//	}
//
//	public Playlist getPlaylist() {
//		return playlist;
//	}
//
//	public void setPlaylist(Playlist playlist) {
//		this.playlist = playlist;
//	}
//
//	public Playlist getSelectedPlaylist() {
//		return selectedPlaylist;
//	}
//
//	public void setSelectedPlaylist(Playlist selectedPlaylist) {
//		this.selectedPlaylist = selectedPlaylist;
//	}
//
//	public String getDireccao() {
//		return direccao;
//	}
//
//	public void setDireccao(String direccao) {
//		this.direccao = direccao;
//	}
//
//	public Musica getMusicaRemover() {
//		return musicaRemover;
//	}
//
//	public void setMusicaRemover(Musica musicaRemover) {
//		this.musicaRemover = musicaRemover;
//	}
//
//	public Musica getMusicaPlay() {
//		return musicaPlay;
//	}
//
//	public void setMusicaPlay(Musica musicaPlay) {
//		this.musicaPlay = musicaPlay;
//	}
//
//	public String getUrl() {
//		return url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//
//	public String getTempName() {
//		return tempName;
//	}
//
//	public void setTempName(String tempName) {
//		this.tempName = tempName;
//	}
//
//	public String getNomeInicial() {
//		return nomeInicial;
//	}
//
//	public void setNomeInicial(String nomeInicial) {
//		this.nomeInicial = nomeInicial;
//	}
//
}

package pt.uc.dei.aor.pf.cdibeans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.aor.pf.beans.MusicaInterface;
import pt.uc.dei.aor.pf.entities.Musica;

@Named
@SessionScoped
public class MusicaCDIBean implements Serializable {

	private static final long serialVersionUID = 7548986860031156326L;

	@Inject
	MusicaInterface musicaBean;

	@Inject
	UserSession user;
	@Inject
	PlaylistCDIBean playCdi;
	@Inject
	PesquisaAuxiliar pesqAux;

	private List<Musica> todasMusicas;
	private List<Musica> todasMusicasByDono;
	private Musica musicaCriacao;
	private Musica musicaSeleccionada;
	private UploadedFile file;

	public MusicaCDIBean() {

	}

	public void criarNovaMusica() {
		musicaCriacao = new Musica();

	}
	public void fileUploadListener(FileUploadEvent e){
		this.file = e.getFile();
	}
	public void salvar() {
		 if (file != null) {
		 try {
		 Properties props = System.getProperties();
		 file.write(props.getProperty("user.dir") +
		 "\\music\\" + user.getCurrent().getId()+"_"
		 + file.getFileName());
		 String path = "/music/" +user.getCurrent().getId()+"_"+ file.getFileName();
		 this.musicaCriacao.setDono(user.getCurrent());
		 this.musicaCriacao.setFilePath(path);
		 musicaBean.save(this.musicaCriacao);
		 // refresh nos paineis
		 RequestContext.getCurrentInstance().update(
		 Arrays.asList("frm:msgs", "frm:musica-table"));
		 consultar();
		 pesqAux.setMusicaSeleccionada(musicaSeleccionada);
		 pesqAux.refreshLists();
		 musicaSeleccionada = null;
		 // mensagem de sucesso
		 FacesMessage message = new FacesMessage(
		 FacesMessage.SEVERITY_INFO, "Music added successfully",
		 "");
		 FacesContext.getCurrentInstance().addMessage(null, message);
		
		 } catch (Exception e) {
		 FacesMessage msg = new FacesMessage(
		 FacesMessage.SEVERITY_ERROR,
		 "Error occured while uploading file.", null);
		 FacesContext.getCurrentInstance().addMessage(null, msg);
		 e.printStackTrace();
		 }
		 } else {
		 FacesMessage msg = new FacesMessage(
		 FacesMessage.SEVERITY_ERROR,
		 "Please select an mp3 file to upload.", null);
		 FacesContext.getCurrentInstance().addMessage(null, msg);
		 }

	}

	public void editar() {
		FacesMessage msg = new FacesMessage(
				 FacesMessage.SEVERITY_INFO,
				 "Music edited successfully!", null);
				 FacesContext.getCurrentInstance().addMessage(null, msg);
		musicaBean.update(this.musicaSeleccionada);
		pesqAux.setMusicaSeleccionada(musicaSeleccionada);
		musicaSeleccionada = null;
		RequestContext.getCurrentInstance().update(
				Arrays.asList("frm:musica-table"));
		consultar();
	}

	public void excluir() {
		musicaBean.delete(this.musicaSeleccionada);
		consultar();
		musicaSeleccionada = null;
		pesqAux.setMusicaSeleccionada(musicaSeleccionada);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Music deleted successfully", "");
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void consultar() {
		this.todasMusicas = musicaBean.findAll();
	}

	public void onRowSelect(SelectEvent event) {
		pesqAux.setMusicaSeleccionada(musicaSeleccionada);
	}


	// ************** Getters & Setters *********************//
	public List<Musica> getTodasMusicas() {
		consultar();
		return todasMusicas;
	}

	public List<Musica> getTodasMusicasByDono() {
		consultarByDono();
		return this.todasMusicasByDono;
	}

	public void consultarByDono() {
		this.todasMusicasByDono = musicaBean.findAllByUser(user.getCurrent());
	}

	public Musica getMusicaSeleccionada() {
		return musicaSeleccionada;
	}

	public void setMusicaSeleccionada(Musica musicaSeleccionada) {
		this.musicaSeleccionada = musicaSeleccionada;
	}

	public Musica getMusicaCriacao() {
		return this.musicaCriacao;
	}

	public void setMusicaCriacao(Musica musicaCriacao) {
		this.musicaCriacao = musicaCriacao;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

}

package pt.uc.dei.aor.pf.entities;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement(name="utilizador")
public class Utilizador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_utilizador")

	private int id;

	@Column(name="nome")
	private String nome;
	@Column(name="mail")
	private String mail;
	@Column(name="password")
	private String password;

	@XmlTransient 
	@OneToMany(mappedBy="dono", cascade=CascadeType.ALL)
	private List<Playlist> playlists;
	@XmlTransient 
	@OneToMany
	private List<Musica> musicas;

	// usar para os roles!!!!!
	// @OneToMany(mappedBy = "dono")
	// private List<Letra> userLetras;

	@XmlTransient 
	@Column(name="role")
	private String role ;

	@XmlTransient 
	public List<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}
	@XmlTransient 
	public List<Musica> getMusicas() {
		return musicas;
	}

	public void setMusicas(List<Musica> musicas) {
		this.musicas = musicas;
	}

	@Temporal(javax.persistence.TemporalType.DATE)
	private Calendar data_nascimento;

	public Utilizador() {
		this.role="CLIENT";
	}

	public Utilizador(String mail, String password) {
		this.mail = mail;
		this.password = password;
		this.role="CLIENT";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Calendar getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(Calendar data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utilizador other = (Utilizador) obj;
		if (id != other.id)
			return false;
		return true;
	}

}

package pt.uc.dei.aor.pf.beans;

import java.util.List;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.Utilizador;

@Local
public interface UserInterface {

	public abstract boolean save(Utilizador user);

	public abstract void update(Utilizador user);
	public abstract void logout(Utilizador user);
	public abstract void updatePassword(Utilizador user);

	public abstract void delete(Utilizador user);
	public abstract Utilizador find(int id);
	public abstract Utilizador findSimpleUser(int id);
	public abstract Utilizador findUserByEmail(String email);
	public abstract void deleteUserById(int id);
	public abstract Utilizador login(Utilizador user);
	public abstract List<Utilizador> findAll();
	public abstract List<Utilizador> findAllWS();

}

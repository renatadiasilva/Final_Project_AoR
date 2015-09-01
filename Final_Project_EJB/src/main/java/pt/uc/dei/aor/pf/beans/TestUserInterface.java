package pt.uc.dei.aor.pf.beans;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.Utilizador;



@Local
public interface TestUserInterface{
	
	public abstract void save(UserEntity user);
	public abstract void update(UserEntity user);
	public abstract void delete(UserEntity user);

	public abstract UserEntity findUserByEmail(String email);
	
}
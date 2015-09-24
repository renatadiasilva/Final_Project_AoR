package pt.uc.dei.aor.pf.mailManagement;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.UserEntity;

@Local
public interface SecureMailManagementInterface {

	public abstract void passwordRecovery(UserEntity user, String temporaryPassword);

	public abstract void testEmail(String receiver, String subject, String text);

	public abstract void candidateToAuthenticate(UserEntity newUser);

	public abstract void authenticatedEmail(UserEntity user);

	public abstract void sendPassToNewUser(UserEntity newUser, 
			String temporaryPassword);

	public abstract void newEmail(UserEntity user);

}

package pt.uc.dei.aor.pf.mailManagement;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.UserEntity;

@Local
public interface MailManagementInterface {

	public abstract void passwordRecovery(UserEntity user, String temporaryPassword);

	public abstract void testEmail(String receiver, String subject, String text);

}

package pt.uc.dei.aor.pf.mailManagement;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class MailManagementImp implements MailManagementInterface {
	
	private static final Logger log = LoggerFactory.getLogger(MailManagementImp.class);
	
	@Override
	public void passwordRecovery (UserEntity user, String temporaryPassword){
		// Envia um email ao user a notificar da nova password temporária
		log.info("Email enviado a "+user.getEmail()+" com a password temporária "+temporaryPassword);
	}

}

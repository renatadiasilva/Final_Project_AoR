package pt.uc.dei.aor.pf.mailManagement;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.StyleEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
@LocalBean
public class SecureMailManagementImp implements SecureMailManagementInterface{
	private static final Logger log = LoggerFactory.getLogger(SecureMailManagementImp.class);

	@Resource(mappedName="java:jboss/mail/Gmail")
	Session gmailSession;
	
	@EJB
	private StyleEJBInterface styleEJB;

	private static final String FROM="itjobs.aor@gmail.com";

	//	private static final String RECEIVER = "duarte.m.a.goncalves@gmail.com, renatadiasilva@gmail.com";
	 private static final String RECEIVER = "duarte.m.a.goncalves@gmail.com";
	private static final boolean OVERRIDE = true;

	private static final String SERVICE_CONTEXT="https://localhost/Final_Project_WEB/services/";

	public SecureMailManagementImp() {
	}

	@Asynchronous
	private void sendEmail(String receiver, String subject, String content) {

		log.info("Sending Email from " + FROM + " to " + receiver + " : " + subject);

		if(OVERRIDE)receiver=RECEIVER;

		try {

			Message message = new MimeMessage(gmailSession);
			message.setFrom(new InternetAddress(FROM));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(receiver));
			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);

			log.info("Email was sent");

		} catch (MessagingException e) {
			log.error("Error while sending email : " + e.getMessage());
		}
	}

	@Override
	public void passwordRecovery(UserEntity user, String temporaryPassword){
		// Envia um email ao user a notificar da nova password temporária
		String receiver=user.getEmail();
		String subject="Recuperação de password";
		String companyName = styleEJB.findDefaulStyle().getCompanyName();
		String text="Olá "+user.getFirstName()+" "+user.getLastName()+","+
				"\n\nA sua nova password para aceder à plataforma "
				+companyName+": "
				+temporaryPassword+"\n\nCumprimentos,\nA equipa "+companyName;

		log.info("Envio de email para "+user.getEmail()+" para "
				+ "notificar da password temporária "+temporaryPassword);
		this.sendEmail(receiver, subject, text);
	}

	@Override
	public void candidateToAuthenticate(UserEntity newUser) {
		// Envia um email a um novo candidato para autenticar o registo
		// Link com a query para a autenticação do utilizador
		String link=SecureMailManagementImp.SERVICE_CONTEXT
				+Constants.SERVLET_AUTH_CANDIDATE
				+"?"+Constants.SERVLET_EMAIL+"="+newUser.getEmail();

		String companyName = styleEJB.findDefaulStyle().getCompanyName();
		String text="Olá "+newUser.getFirstName()+" "+newUser.getLastName()+","
				+"\n\nBem vindo à plataforma "+companyName+". Para terminar o "
				+ "seu registo, por favor siga o link: "+link
				+"\n\nCumprimentos,\nA equipa "
				+companyName;

		this.sendEmail(newUser.getEmail(), "Registo na plataforma "
				+ companyName+" - Autenticação do email", text);
	}

	@Override
	public void authenticatedEmail(UserEntity user) {
		String companyName = styleEJB.findDefaulStyle().getCompanyName();
		String text="Olá "+user.getFirstName()+" "+user.getLastName()+","+
				"\n\nO seu email encontra-se autenticado e o seu registo "
				+ "concluído. Boa sorte na procura do seu próximo emprego!"
				+"\n\nCumprimentos,\nA equipa "+companyName;
		
		this.sendEmail(user.getEmail(), "Registo na plataforma "
				+ companyName+" - Registo concluído", text);
	}

	@Override
	public void sendPassToNewUser(UserEntity newUser, 
			String temporaryPassword) {
		// Send email to a new user with temporary password
		String companyName = styleEJB.findDefaulStyle().getCompanyName();
		String text="Olá "+newUser.getFirstName()+" "+newUser.getLastName()+","
				+"\n\nBem vindo à plataforma "+companyName+"."
				+"\n\nA sua password (temporária) para aceder à plataforma "
				+companyName+" é "
				+temporaryPassword+"\n\nCumprimentos,\nA equipa "+companyName;

		log.info("An email was sent to a new user: "
				+ newUser.getEmail()+" with "
				+ "temporary password "+temporaryPassword);

		this.sendEmail(newUser.getEmail(), "Registo na plataforma "
				+ companyName+" - Envio de password temporária", text);
	}
}
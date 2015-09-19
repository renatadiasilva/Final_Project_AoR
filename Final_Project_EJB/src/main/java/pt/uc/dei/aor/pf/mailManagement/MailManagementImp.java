package pt.uc.dei.aor.pf.mailManagement;

import java.util.Properties;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class MailManagementImp implements MailManagementInterface {

	private static final Logger log = LoggerFactory.getLogger(MailManagementImp.class);

	private static final String USERNAME = "itjobs.aor@gmail.com";
	private static final String PASSWORD = "RenataDuarte";


	//	private static final String RECEIVER = "duarte.m.a.goncalves@gmail.com, renatadiasilva@gmail.com";
	private static final String RECEIVER = "duarte.m.a.goncalves@gmail.com";
	private static final boolean OVERRIDE = true;

	private Session session;

	private Properties props;

	// REVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private String serviceContext="https://localhost/Final_Project_WEB/";

	public MailManagementImp() {

		this.props = new Properties();
		this.props.put("mail.smtp.auth", "true");
		this.props.put("mail.smtp.starttls.enable", "true");
		this.props.put("mail.smtp.host", "smtp.gmail.com");
		this.props.put("mail.smtp.port", "587");

		this.session = Session.getInstance(this.props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});

	}

	@Override
	public void setContext(String context) {
		this.serviceContext=context;
	}

	@Asynchronous
	private void sendEmail (String receiver, String subject, String text){
		log.error("This class is disabled, please use SecureMailManagement(Imp/Interface)");

//		if(OVERRIDE)receiver=RECEIVER;
//
//		try {
//			Message message = new MimeMessage(this.session);
//			message.setFrom(new InternetAddress(USERNAME));
//			message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(receiver));
//			message.setSubject(subject);
//			message.setText(text);
//
//			Transport.send(message);
//
//			log.info("Email sent to "+receiver);
//
//		} catch (MessagingException e) {
//			log.error("Error sending email to "+receiver);
//		}

	}

	@Override
	public void passwordRecovery (UserEntity user, String temporaryPassword){
		// Envia um email ao user a notificar da nova password temporária
		String receiver=user.getEmail();
		String subject="Recuperação de password";
		String text="Olá "+user.getFirstName()+" "+user.getLastName()+","+
				"\n\nA sua nova password para aceder à plataforma ITJobs: "+temporaryPassword
				+"\n\nCumprimentos,\nA equipa ITJobs";

		log.info("Envio de email para "+user.getEmail()+" para notificar da password temporária "+temporaryPassword);
		this.sendEmail(receiver, subject, text);
	}

	@Override
	public void testEmail(String receiver, String subject, String text){
		this.sendEmail(receiver, subject, text);
	}

	@Override
	public void candidateToAuthenticate(UserEntity newUser) {
		// Envia um email a um novo candidato para autenticar o registo
		// Link com a query para a autenticação do utilizador
		String link=this.serviceContext
				+"?"+Constants.QUERY_SUBJECT+"="+Constants.QUERY_SUBJECT_AUTH_CANDIDATE
				+"&"+Constants.QUERY_EMAIL+"="+newUser.getEmail();

		String text="Olá "+newUser.getFirstName()+" "+newUser.getLastName()+","+
				"\n\nBem vindo à plataforma ITJobs. Para terminar o seu registo, por favor siga o link: "+link
				+"\n\nCumprimentos,\nA equipa ITJobs";

		this.sendEmail(newUser.getEmail(), "Registo na plataforma ITJobs - Autenticação do email", text);
	}

}

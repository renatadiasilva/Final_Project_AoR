package pt.uc.dei.aor.pf;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import pt.uc.dei.aor.pf.mailManagement.MailManagementInterface;

@Named
@RequestScoped
public class TestEmailCDI {
	
	@EJB
	MailManagementInterface mail;
	
	public void testMail(){
		mail.testEmail("ninja@mail.com", "Teste", "Olá, este é o conteúdo do email de teste");
	}

}

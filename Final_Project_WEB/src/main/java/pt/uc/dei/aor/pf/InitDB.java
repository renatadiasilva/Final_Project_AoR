package pt.uc.dei.aor.pf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
//import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.entities.UserInfoEntity;

@Named
@RequestScoped
public class InitDB {

	private static final Logger log = LoggerFactory.getLogger(InitDB.class);
	
	@EJB
	UserEJBInterface userEJB;

	@EJB
	UserInfoEJBInterface userInfoEJB;

	public void populate() throws ParseException {

		log.info("Populate...");

		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 

		UserEntity [] users = {
			new UserEntity("admin@mail.com", "12345", "Maria", "Poderosa", null),
			new UserEntity("jlopes@gmail.com", "12345", "José Pedro", "Lopes", null),
			new UserEntity("ssst@hotmail.com", "12345", "Susana", "Teodóro", null),
			new UserEntity("mafaldinhas@sapo.pt", "12345", "Mafalda", "Santos", null),
			new UserEntity("sera1960@sapo.pt", "12345", "Serafim", "Simões", null),
			new UserEntity("duarte.m.a.goncalves@gmail.com ", "12345", "Duarte", "Gonçalves",
					null),
			new UserEntity("renatadiasilva@gmail.com", "12345", "Renata", "Silva", null),
			new UserEntity("leitaosilva@gmail.com", "12345", "Fátima", "Leitão", null),
			new UserEntity("dvdleitaods@gmail.com", "12345", "David", "Leitão", null),
			new UserEntity("jackerozeno@hotmail.com", "12345", "Jackeline", "Rozeno", null),
		};
		
		UserInfoEntity [] usersI = {
			new UserInfoEntity(ft.parse("1960-05-11"), "Avenida da Liberdade",
						"Lisboa", null, "969362531", "Portugal",
						"Engenharia Informática","Universidade do Porto, Portugal", null, users[4]),
			new UserInfoEntity(ft.parse("1985-01-15"), "Rua de Cima", "Proença-a-Nova",
						null, "968302615", "Portugal", "Arquitetura", "Universidade de Mundo",
						null, users[5]),
			new UserInfoEntity(ft.parse("1977-10-24"), "Ladeira Seminário", "Coimbra",
						"239716625", "918927181", "Portugal", "Matemática", "Universidade de Coimbra, Portugal",
						null, users[6]),
			new UserInfoEntity(ft.parse("1953-02-27"), "Ladeira Seminário", "Coimbra",
						"239716625", "917801254", "Portugal", "Medicina", "Universidade de Coimbra, Portugal",
						null, users[7]),
			new UserInfoEntity(ft.parse("1989-05-17"), "Ladeira Seminário", "Coimbra",
						"239716625", "912847967", "Portugal", "Engenharia Informática",
						"Instituto Superior de Engenharias de Coimbra, Portugal",
						null, users[8]),
			new UserInfoEntity(ft.parse("1985-09-02"), "Avenida Sá da Bandeira", "Coimbra",
						null, "912993207", "Brasil", "Direito", "Centro Universitário Ritter dos Reis, "
						+ "Canoas, Rio Grande do Sul, Brasil", null, users[9]),
		};
		  
		List<String> roles = Arrays.asList(UserEntity.ROLE_ADMIN, UserEntity.ROLE_MANAGER,
				UserEntity.ROLE_INTERVIEWER, UserEntity.ROLE_CANDIDATE);
		users[0].setDefaultRole(UserEntity.ROLE_ADMIN);
		users[0].setRoles(roles);

		roles = Arrays.asList(UserEntity.ROLE_ADMIN);
		users[1].setDefaultRole(UserEntity.ROLE_ADMIN);
		users[1].setRoles(roles);

		roles = Arrays.asList(UserEntity.ROLE_MANAGER, UserEntity.ROLE_CANDIDATE);
		users[2].setDefaultRole(UserEntity.ROLE_MANAGER);
		users[2].setRoles(roles);

		roles = Arrays.asList(UserEntity.ROLE_INTERVIEWER);
		users[3].setDefaultRole(UserEntity.ROLE_INTERVIEWER);
		users[3].setRoles(roles);

		roles = Arrays.asList(UserEntity.ROLE_CANDIDATE);

		for(int i = 0; i < 6; i++) {
			users[i+4].setDefaultRole(UserEntity.ROLE_CANDIDATE);
			users[i+4].setRoles(roles);
			users[i+4].setUserInfo(usersI[i]);
		}
		
		for (UserEntity u : users) {
			this.userEJB.save(u);
		}

//		sources.add(SubmissionEntity.SOURCE_EXPRESSO);
//		sources.add(SubmissionEntity.SOURCE_FACEBOOK);
//
//		newSub = new SubmissionEntity(newUser, "\\path\\ml.pdf", sources, false);
////		newSub.setPosition(position);
////		newSub.setAssociatedBy(admin/gestor);
//		cal = Calendar.getInstance();
//		cal.set(2015, 0, 15);
//		newSub.setDate(cal.getTime());
//		newSub.setStatus(SubmissionEntity.STATUS_ACCEPTED);
//		
//		// faltam cenas pesquisas...
//		// guardar tudo em listas 
//		
//		SubmissionEntity newSub = new SubmissionEntity();
//		List<String> sources = new ArrayList<String>();
	}

}

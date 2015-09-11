package pt.uc.dei.aor.pf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
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

	public void populate() {

		log.info("Populate...");

		UserEntity newUser = new UserEntity();
		List<String> roles = new ArrayList<String>();

		roles.add(UserEntity.ROLE_ADMIN);
		roles.add(UserEntity.ROLE_MANAGER);
		roles.add(UserEntity.ROLE_INTERVIEWER);
		roles.add(UserEntity.ROLE_CANDIDATE);

		newUser = new UserEntity("admin@mail.com", "12345", "Maria", "Poderosa", roles);
		newUser.setDefaultRole(UserEntity.ROLE_ADMIN);

		this.userEJB.save(newUser);

		roles.clear();
		roles.add(UserEntity.ROLE_ADMIN);

		newUser = new UserEntity("jlopes@gmail.com", "12345", "José Pedro", "Lopes", roles);
		newUser.setDefaultRole(UserEntity.ROLE_ADMIN);

		this.userEJB.save(newUser);


		roles.clear();
		roles.add(UserEntity.ROLE_MANAGER);
		roles.add(UserEntity.ROLE_CANDIDATE);

		newUser = new UserEntity("ssst@hotmail.com", "12345", "Susana", "Teodóro", roles);
		newUser.setDefaultRole(UserEntity.ROLE_MANAGER);

		this.userEJB.save(newUser);

		
		roles.clear();
		roles.add(UserEntity.ROLE_INTERVIEWER);

		newUser = new UserEntity("mafaldinhas@sapo.pt", "12345", "Mafalda", "Santos", roles);
		newUser.setDefaultRole(UserEntity.ROLE_INTERVIEWER);

		this.userEJB.save(newUser);

		
		roles.clear();
		roles.add(UserEntity.ROLE_CANDIDATE);

		newUser = new UserEntity("sera1960@sapo.pt", "12345", "Serafim", "Simões", roles);
		newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 11);
		cal.set(Calendar.MONTH, 4);
		cal.set(Calendar.YEAR, 1960);
		
		UserInfoEntity newUserInfo = new UserInfoEntity(cal.getTime(), "Avenida da Liberdade",
				"Lisboa", null, "969362531", "Portugal",
				"Engenharia Informática","Universidade do Porto, Portugal", null, newUser);
		
		newUser.setUserInfo(newUserInfo);

		this.userEJB.save(newUser);

		
		newUser = new UserEntity("duarte.m.a.goncalves@gmail.com ", "12345", "Duarte", "Gonçalves",
				roles);
		newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);

		cal.set(Calendar.DAY_OF_MONTH, 15);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 1985);
		
		newUserInfo = new UserInfoEntity(cal.getTime(), "Rua de Cima", "Proença-a-Nova",
				null, "968302615", "Portugal", "Arquitetura", "Universidade de Mundo",
				null, newUser);
		
		newUser.setUserInfo(newUserInfo);

		this.userEJB.save(newUser);

		newUser = new UserEntity("renatadiasilva@gmail.com", "12345", "Renata", "Silva", roles);
		newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);

		cal.set(Calendar.DAY_OF_MONTH, 24);
		cal.set(Calendar.MONTH, 9);
		cal.set(Calendar.YEAR, 1977);
		
		newUserInfo = new UserInfoEntity(cal.getTime(), "Ladeira Seminário", "Coimbra",
				"239716625", "918927181", "Portugal", "Matemática", "Universidade de Coimbra, Portugal",
				null, newUser);
		
		newUser.setUserInfo(newUserInfo);

		this.userEJB.save(newUser);


		newUser = new UserEntity("leitaosilva@gmail.com", "12345", "Fátima", "Leitão", roles);
		newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);

		cal.set(Calendar.DAY_OF_MONTH, 27);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.YEAR, 1953);
		
		newUserInfo = new UserInfoEntity(cal.getTime(), "Ladeira Seminário", "Coimbra",
				"239716625", "917801254", "Portugal", "Medicina", "Universidade de Coimbra, Portugal",
				null, newUser);
		
		newUser.setUserInfo(newUserInfo);

		this.userEJB.save(newUser);


		newUser=new UserEntity("dvdleitaods@gmail.com", "12345", "David", "Leitão", roles);
		newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);

		cal.set(Calendar.DAY_OF_MONTH, 7);
		cal.set(Calendar.MONTH, 4);
		cal.set(Calendar.YEAR, 1989);
		
		newUserInfo = new UserInfoEntity(cal.getTime(), "Ladeira Seminário", "Coimbra",
				"239716625", "912847967", "Portugal", "Engenharia Informática",
				"Instituto Superior de Engenharias de Coimbra, Portugal",
				null, newUser);
		
		newUser.setUserInfo(newUserInfo);

		this.userEJB.save(newUser);

		
		newUser=new UserEntity("jackerozeno@hotmail.com", "12345", "Jackeline", "Rozeno", roles);
		newUser.setDefaultRole(UserEntity.ROLE_CANDIDATE);

		cal.set(Calendar.DAY_OF_MONTH, 2);
		cal.set(Calendar.MONTH, 8);
		cal.set(Calendar.YEAR, 1985);
		
		newUserInfo = new UserInfoEntity(cal.getTime(), "Avenida Sá da Bandeira", "Coimbra",
				null, "912993207", "Brasil", "Direito", "Universidade de Porto Alegre, Brasil",
				null, newUser);
		
		newUser.setUserInfo(newUserInfo);

		this.userEJB.save(newUser);
	}

}

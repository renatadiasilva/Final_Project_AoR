package pt.uc.dei.aor.pf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
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

	@EJB
	ScriptEJBInterface scriptEJB;

	@EJB
	PositionEJBInterface positionEJB;
	
	@EJB
	SubmissionEJBInterface submissionEJB;

	@EJB
	InterviewEJBInterface interviewEJB;
	
	public void populate() throws ParseException {

		log.info("Populate...");

		SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 
		SimpleDateFormat ftDateHour = new SimpleDateFormat ("yyyy-MM-dd HH:mm"); 

		// ENTITY LISTS
		
		UserEntity [] ulist = {
			new UserEntity("admin@mail.com", "12345", "Maria", "Poderosa", null),  // ulist[0]
			new UserEntity("jlopes@gmail.com", "12345", "José Pedro", "Lopes", null), // ulist[1]
			new UserEntity("ssst@hotmail.com", "12345", "Susana", "Teodóro", null), // ulist[2]
			new UserEntity("mafaldinhas@sapo.pt", "12345", "Mafalda", "Santos", null), // ulist[3]
			new UserEntity("sera1960@sapo.pt", "12345", "Serafim", "Simões", null), // ulist[4]
			new UserEntity("duarte.m.a.goncalves@gmail.com ", "12345", "Duarte", "Gonçalves",
					null), // ulist[5]
			new UserEntity("renatadiasilva@gmail.com", "12345", "Renata", "Silva", null), // ulist[6]
			new UserEntity("leitaosilva@gmail.com", "12345", "Fátima", "Leitão", null), // ulist[7]
			new UserEntity("dvdleitaods@gmail.com", "12345", "David", "Leitão", null), // ulist[8]
			new UserEntity("jackerozeno@hotmail.com", "12345", "Jackeline", "Rozeno", null), // ulist[9]
		};
		
		UserInfoEntity [] uilist = {
			new UserInfoEntity(ftDate.parse("1960-05-11"), "Avenida da Liberdade",
						"Lisboa", null, "969362531", "Portugal",
						"Engenharia Informática","Universidade do Porto, Portugal", null, ulist[4]),  // uilist[0]
			new UserInfoEntity(ftDate.parse("1985-01-15"), "Rua de Cima", "Proença-a-Nova",
						null, "968302615", "Portugal", "Arquitetura", "Universidade de Mundo",
						null, ulist[5]),  // uilist[2]
			new UserInfoEntity(ftDate.parse("1977-10-24"), "Ladeira Seminário", "Coimbra",
						"239716625", "918927181", "Portugal", "Matemática", "Universidade de Coimbra, Portugal",
						null, ulist[6]), // uilist[3]
			new UserInfoEntity(ftDate.parse("1953-02-27"), "Ladeira Seminário", "Coimbra",
						"239716625", "917801254", "Portugal", "Medicina", "Universidade de Coimbra, Portugal",
						null, ulist[7]), // uilist[4]
			new UserInfoEntity(ftDate.parse("1989-05-17"), "Ladeira Seminário", "Coimbra",
						"239716625", "912847967", "Portugal", "Engenharia Informática",
						"Instituto Superior de Engenharias de Coimbra, Portugal",
						null, ulist[8]), // uilist[5]
			new UserInfoEntity(ftDate.parse("1985-09-02"), "Avenida Sá da Bandeira", "Coimbra",
						null, "912993207", "Brasil", "Direito", "Centro Universitário Ritter dos Reis, "
						+ "Canoas, Rio Grande do Sul, Brasil", null, ulist[9]), // uilist[6]
		};

		ScriptEntity [] sclist = {
				new ScriptEntity(null, "Guião Geral v1", null, "Primeira versão", false,
						ulist[1]), // sclist[0]
				new ScriptEntity(null, "Guião Geral v2", null, "Segunda versão", true,
								ulist[1]), // sclist[1]
			};

		PositionEntity [] plist = {
				// sla = 60 dias
				new PositionEntity("Programadores Java", null, 4, null, ftDate.parse("2015-10-02"), 
						ulist[2], ulist[1], "Critical Software", PositionEntity.TECH_JAVA, "Procuram-se programadores "
								+ "bla bla bla", null, sclist[0]), // plist[0]
				new PositionEntity("Técnico de Segurança", null, 1, null, ftDate.parse("2015-11-04"), 
						ulist[2], ulist[1], "Critical Software", PositionEntity.TECH_SAFETY, "Procura-se técnico de segurança "
								+ "bla bla bla", null, sclist[0]), // plist[1]
			};
			
		SubmissionEntity [] slist = {
			new SubmissionEntity(ulist[4], null, null, false), // slist[0]
			new SubmissionEntity(ulist[4], null, null, false), // slist[1]
			new SubmissionEntity(ulist[5], null, null, true), // slist[2]  // espontânea
			new SubmissionEntity(ulist[6], null, null, true), // slist[3]  // espontânea mas associada a pos
			new SubmissionEntity(ulist[6], null, null, false), // slist[4] // clone da outra
			new SubmissionEntity(ulist[7], null, null, false), // slist[5]			
			new SubmissionEntity(ulist[8], null, null, false), // slist[6]			
			new SubmissionEntity(ulist[8], null, null, false), // slist[7]			
			new SubmissionEntity(ulist[9], null, null, false), // slist[8]			
		};
		
		InterviewEntity [] ilist = {
				new InterviewEntity(slist[5], ftDateHour.parse("2015-09-20 10:00"), sclist[0], ulist[1]), // ilist[0]				
				new InterviewEntity(slist[8], ftDateHour.parse("2015-09-01 17:30"), sclist[0], ulist[2]), // ilist[1]				
		};
		
		
		// USER/USERSINFO ATTRIBUTES
		
		List<String> roles = Arrays.asList(UserEntity.ROLE_ADMIN, UserEntity.ROLE_MANAGER,
				UserEntity.ROLE_INTERVIEWER, UserEntity.ROLE_CANDIDATE);
		ulist[0].setDefaultRole(UserEntity.ROLE_ADMIN);
		ulist[0].setRoles(roles);

		roles = Arrays.asList(UserEntity.ROLE_ADMIN);
		ulist[1].setDefaultRole(UserEntity.ROLE_ADMIN);
		ulist[1].setRoles(roles);

		roles = Arrays.asList(UserEntity.ROLE_MANAGER, UserEntity.ROLE_CANDIDATE);
		ulist[2].setDefaultRole(UserEntity.ROLE_MANAGER);
		ulist[2].setRoles(roles);

		roles = Arrays.asList(UserEntity.ROLE_INTERVIEWER);
		ulist[3].setDefaultRole(UserEntity.ROLE_INTERVIEWER);
		ulist[3].setRoles(roles);

		roles = Arrays.asList(UserEntity.ROLE_CANDIDATE);

		for(int i = 0; i < 6; i++) {
			ulist[i+4].setDefaultRole(UserEntity.ROLE_CANDIDATE);
			ulist[i+4].setRoles(roles);
			ulist[i+4].setUserInfo(uilist[i]);  // tirar para ver se dá
		}
		
		// SCRIPT ATTRIBUTES
		
		sclist[1].setDerivedFrom(sclist[0]);
				

		// POSITION ATTRIBUTES
		
		List<String> locations = Arrays.asList(PositionEntity.LOCATION_COIMBRA,
				PositionEntity.LOCATION_PORTO);
		List<String> channels = Arrays.asList(PositionEntity.SOCIAL_CRITICAL,
				PositionEntity.SOCIAL_FACEBOOK);
		plist[0].setLocations(locations);
		plist[0].setAdvertisingChannels(channels);
		plist[0].setOpeningDate(ftDate.parse("2015-08-02"));

		locations = Arrays.asList(PositionEntity.LOCATION_PORTO);
		channels = Arrays.asList(PositionEntity.SOCIAL_CRITICAL,
				PositionEntity.SOCIAL_FACEBOOK, PositionEntity.SOCIAL_GLASSDOOR);
		plist[1].setLocations(locations);
		plist[1].setAdvertisingChannels(channels);
		plist[1].setOpeningDate(ftDate.parse("2015-09-02"));
		

		// SUBMISSION ATTRIBUTES
		
		List<String> sources = Arrays.asList(SubmissionEntity.SOURCE_EXPRESSO,
				SubmissionEntity.SOURCE_FACEBOOK);
		slist[0].setPosition(plist[0]);
		slist[0].setSources(sources);
		slist[0].setDate(ftDate.parse("2015-08-20"));

		slist[1].setPosition(plist[1]);
		slist[1].setSources(sources);

		// slist[2] e slist[3] são candidaturas espontâneas
		slist[2].setDate(ftDate.parse("2015-08-15"));
		slist[3].setDate(ftDate.parse("2015-08-28"));
		
		slist[4].setPosition(plist[0]);
		slist[4].setAssociatedBy(ulist[2]);
		slist[4].setDate(ftDate.parse("2015-08-28"));

		Arrays.asList(SubmissionEntity.SOURCE_EXPRESSO);
		slist[5].setPosition(plist[0]);
		slist[5].setSources(sources);
		slist[5].setStatus(SubmissionEntity.STATUS_ACCEPTED);
		slist[5].setDate(ftDate.parse("2015-08-05"));
		
		slist[6].setPosition(plist[0]);
		slist[6].setSources(sources);

		sources = Arrays.asList(SubmissionEntity.SOURCE_FACEBOOK);
		slist[7].setPosition(plist[1]);
		slist[7].setSources(sources);
		slist[7].setDate(ftDate.parse("2015-09-09"));
		
		slist[8].setPosition(plist[0]);
		slist[8].setSources(sources);
		slist[8].setStatus(SubmissionEntity.STATUS_SPROPOSAL);
		slist[8].setDate(ftDate.parse("2015-08-10"));
		
//		// faltam cenas pesquisas... acentos
		
		ilist[0].addInterviewer(ulist[3]);

		ilist[1].addInterviewer(ulist[2]);  // pode ser gestor ou um admin a entrevistar
		ilist[1].setCarriedOut(true);
		ilist[1].setApproved(true);
		ilist[1].setFeedback("O candidato mostrou-se muito dinâmico blablabla");

		for (UserEntity u : ulist) {
			this.userEJB.save(u);
		}

		for (ScriptEntity sc : sclist) {
			this.scriptEJB.save(sc);
		}

		for (PositionEntity p : plist) {
			this.positionEJB.save(p);
		}

		for (SubmissionEntity s : slist) {
			this.submissionEJB.save(s);
		}

		for (InterviewEntity i : ilist) {
			this.interviewEJB.save(i);
		}

	}

}
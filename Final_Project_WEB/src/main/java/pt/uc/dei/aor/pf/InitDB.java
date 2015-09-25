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
import pt.uc.dei.aor.pf.beans.QuestionEJBInterface;
import pt.uc.dei.aor.pf.beans.ScriptEJBInterface;
import pt.uc.dei.aor.pf.beans.StyleEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.beans.UserInfoEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.QuestionEntity;
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
	QuestionEJBInterface questionEJB;

	@EJB
	PositionEJBInterface positionEJB;
	
	@EJB
	SubmissionEJBInterface submissionEJB;

	@EJB
	InterviewEJBInterface interviewEJB;
	
	@EJB
	StyleEJBInterface styleEJB;
	
	public void populate() throws ParseException {

		log.info("Populate...");

		SimpleDateFormat ftDate = new SimpleDateFormat ("yyyy-MM-dd"); 
		SimpleDateFormat ftDateHour = new SimpleDateFormat ("yyyy-MM-dd HH:mm"); 

		// ENTITY LISTS
		
		UserEntity [] ulist = {
			new UserEntity("admin@mail.com", "12345", "Maria", 
					"Poderosa", null),  // ulist[0]
			new UserEntity("jlopes@gmail.com", "12345", "José Pedro", 
					"Lopes", null), // ulist[1]
			new UserEntity("ssst@hotmail.com", "12345", "Susana", 
					"Teodóro", null), // ulist[2]
			new UserEntity("mafaldinhas@sapo.pt", "12345", "Mafalda", 
					"Santos", null), // ulist[3]
			new UserEntity("sera1960@sapo.pt", "12345", "Serafim", 
					"Simões", null), // ulist[4]
			new UserEntity("duarte.m.a.goncalves@gmail.com ", "12345", 
					"Duarte", "Gonçalves", null), // ulist[5]
			new UserEntity("renatadiasilva@gmail.com", "12345", "Renata", 
					"Silva", null), // ulist[6]
			new UserEntity("leitaosilva@gmail.com", "12345", "Fátima", 
					"Leitão", null), // ulist[7]
			new UserEntity("dvdleitaods@gmail.com", "12345", "David",
					"Leitão", null), // ulist[8]
			new UserEntity("jackerozeno@hotmail.com", "12345", "Jackeline",
					"Rozeno", null), // ulist[9]
			new UserEntity("gestor1000@itgrow.pt", "12345", "Stephen",
					"Hawkings", null), // ulist[10]
			new UserEntity("entre2000@itgrow.pt", "12345", "Isaac",
					"Newton", null), // ulist[11]
		};
		
		UserInfoEntity [] uilist = {
			new UserInfoEntity(ftDate.parse("1960-05-11"), 
					"Avenida da Liberdade",
					"Lisboa", null, "969 362 531", "Portugal",
					"Engenharia Informática","Universidade do Porto, Portugal",
					null, ulist[4]),  // uilist[0]
			new UserInfoEntity(ftDate.parse("1985-01-15"), "Rua de Cima",
					"Proença-a-Nova", null, "968 302 615", "Portugal",
					"Arquitetura", "Universidade de Mundo",
					null, ulist[5]),  // uilist[2]
			new UserInfoEntity(ftDate.parse("1977-10-24"), "Ladeira Seminário",
					"Coimbra","239 716 625", "918 927 181", "Portugal",
					"Matemática", "Universidade de Coimbra, Portugal",
					null, ulist[6]), // uilist[3]
			new UserInfoEntity(ftDate.parse("1953-02-27"), "Ladeira Seminário",
					"Coimbra", "239 716 625", "917 801 254", "Portugal",
					"Medicina", "Universidade de Coimbra, Portugal",
					null, ulist[7]), // uilist[4]
			new UserInfoEntity(ftDate.parse("1989-05-17"), "Ladeira Seminário",
					"Coimbra", "239 716 625", "912 847 967", "Portugal",
					"Engenharia Informática",
					"Instituto Superior de Engenharias de Coimbra, Portugal",
					null, ulist[8]), // uilist[5]
			new UserInfoEntity(ftDate.parse("1985-09-02"), 
					"Avenida Sá da Bandeira", "Coimbra",
					null, "912 993 207", "Brasil", "Direito",
					"Centro Universitário Ritter dos Reis, Canoas, Rio Grande"
					+ " do Sul, Brasil", null, ulist[9]), // uilist[6]
		};

		ScriptEntity [] sclist = {
			new ScriptEntity(null, "Guião Geral v1", null, 
					"Primeira versão", false, ulist[1]), // sclist[0]
			new ScriptEntity(null, "Guião Geral v2", null, 
					"Segunda versão", true, ulist[1]), // sclist[1]
			new ScriptEntity(null, "Guião Programadores", null, 
					"Primeira versão, baseado no padrão internacional"
					+ " blablabla", true, ulist[1]), // sclist[2]
			new ScriptEntity(null, "Guião (entrevista agendada)", null, 
					"Só para testar", true, ulist[1]), // sclist[3]
			new ScriptEntity(null, "Guião sem utilidade", null, 
					"Só para testar", true, ulist[1]), // sclist[4]
			new ScriptEntity(null, "Guião (entrevista passada)", null, 
					"Só para testar", true, ulist[1]), // sclist[5]
			};
		
		QuestionEntity [] qList = {
				new QuestionEntity(QuestionEntity.ANSWER, "Qual é a sua idade?"),
				new QuestionEntity(QuestionEntity.ANSWER, "Qual é o seu clube?"),
				new QuestionEntity(QuestionEntity.ANSWER, "Você é ninja?"),
				new QuestionEntity(QuestionEntity.ANSWER, "Qual é o significado da vida?"),
				new QuestionEntity(QuestionEntity.ANSWER, "O Benfica é o maior?"),
		};

		PositionEntity [] plist = {
			new PositionEntity("Programadores Java", null, 4, 
				Constants.STATUS_OPEN, null, 60, 
				ulist[2], ulist[1], "Critical Software", 
				Constants.TECH_JAVA, "Procuram-se programadores java "
				+ "bla bla bla", null, sclist[2]), // plist[0]
			new PositionEntity("Tecnico de Segurança", null, 1, 
				Constants.STATUS_OPEN, null, 60, 
				ulist[2], ulist[1], "Critical Software",
				Constants.TECH_SAFETY, "Procura-se técnico de"
				+ " segurança bla bla bla", null, sclist[2]), // plist[1]
			new PositionEntity("Programadores .NET", null, 3, 
				Constants.STATUS_OPEN, null, 150, 
				ulist[10], ulist[1], "IT Grow", Constants.TECH_DOTNET,
				"Procuram-se programadores .NET bla bla bla", 
				null, sclist[2]), // plist[2]
			new PositionEntity("Analistas de Integração", null, 2, 
				Constants.STATUS_OPEN, ftDate.parse("2015-09-12"),
				30, ulist[10], ulist[1], "BPI", 
				Constants.TECH_INTEGRATION,
				"O BPI procura analistas de integração para integrar"
				+ " a sua equipa blablabla", 
				null, sclist[1]), // plist[3]
			new PositionEntity("Junior Programador de Java", null, 1, 
				Constants.STATUS_OPEN, ftDate.parse("2015-07-08"),
				40, ulist[2], ulist[1], "BPI", 
				Constants.TECH_JAVA,
				"O BPI procura programadores de Java para integrar"
				+ " a sua equipa de desenvolvimento da aplicação blablabla", 
				null, sclist[0]), // plist[4]
			new PositionEntity("Gestor de projeto", null, 1, 
				Constants.STATUS_OPEN, null,
				200, ulist[2], ulist[1], "Critical Software", 
				Constants.TECH_MANAGEMENT,
				"A Critical Software procura um gestor de projeto"
				+ " blablabla", 
				null, sclist[1]), // plist[5]
			};
			
		SubmissionEntity [] slist = {
			new SubmissionEntity(ulist[4], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[0]
			new SubmissionEntity(ulist[4], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[1]
			// espontânea
			new SubmissionEntity(ulist[5], Constants.STATUS_SUBMITED, 
					null, null, true), // slist[2] 
			// espontânea mas posteriormente associada a posição
			new SubmissionEntity(ulist[6], Constants.STATUS_SUBMITED, 
					null, null, true), // slist[3]  
			// clone da anterior (com posição)
			new SubmissionEntity(ulist[6], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[4] 
			new SubmissionEntity(ulist[7], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[5]			
			new SubmissionEntity(ulist[8], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[6]			
			new SubmissionEntity(ulist[8], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[7]			
			new SubmissionEntity(ulist[9], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[8]			
			new SubmissionEntity(ulist[4], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[9]			
			new SubmissionEntity(ulist[7], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[10]			
			new SubmissionEntity(ulist[8], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[11]			
			new SubmissionEntity(ulist[9], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[12]			
			new SubmissionEntity(ulist[4], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[13]			
			new SubmissionEntity(ulist[7], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[14]			
			new SubmissionEntity(ulist[8], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[15]			
			new SubmissionEntity(ulist[9], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[16]			
			new SubmissionEntity(ulist[7], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[17]			
			new SubmissionEntity(ulist[8], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[18]			
			new SubmissionEntity(ulist[9], Constants.STATUS_SUBMITED, 
					null, null, false), // slist[19]			
		};
		
		InterviewEntity [] ilist = {
			new InterviewEntity(slist[5], ftDateHour.parse("2015-09-20 10:00"),
					sclist[2], ulist[1]), // ilist[0]				
			new InterviewEntity(slist[8], ftDateHour.parse("2015-09-01 17:30"),
					sclist[2], ulist[2]), // ilist[1]				
			new InterviewEntity(slist[10], ftDateHour.parse("2015-08-21 12:00"),
					sclist[2], ulist[10]), // ilist[2]				
			new InterviewEntity(slist[11], ftDateHour.parse("2015-08-06 14:00"),
					sclist[2], ulist[10]), // ilist[3]	 false			
			new InterviewEntity(slist[12], ftDateHour.parse("2015-08-06 15:00"),
					sclist[2], ulist[10]), // ilist[4]				
			new InterviewEntity(slist[12], ftDateHour.parse("2015-08-10 09:00"),
					sclist[2], ulist[10]), // ilist[5]			
			new InterviewEntity(slist[13], ftDateHour.parse("2015-09-05 09:00"),
					sclist[1], ulist[10]), // ilist[6]				
			new InterviewEntity(slist[15], ftDateHour.parse("2015-09-05 10:00"),
					sclist[1], ulist[10]), // ilist[7]				
			new InterviewEntity(slist[16], ftDateHour.parse("2015-09-05 11:00"),
					sclist[1], ulist[10]), // ilist[8]				
			new InterviewEntity(slist[17], ftDateHour.parse("2015-07-30 14:00"),
					sclist[0], ulist[2]), // ilist[9]				
			new InterviewEntity(slist[18], ftDateHour.parse("2015-07-30 15:00"),
					sclist[3], ulist[2]), // ilist[10]				
			new InterviewEntity(slist[19], ftDateHour.parse("2015-07-30 16:00"),
					sclist[5], ulist[2]), // ilist[11]				
		};
		
		
		// USER/USERSINFO ATTRIBUTES
		
		List<String> roles = Arrays.asList(Constants.ROLE_ADMIN,
				Constants.ROLE_MANAGER, Constants.ROLE_INTERVIEWER,
				Constants.ROLE_CANDIDATE);
		ulist[0].setDefaultRole(Constants.ROLE_ADMIN);
		ulist[0].setRoles(roles);

		roles = Arrays.asList(Constants.ROLE_ADMIN);
		ulist[1].setDefaultRole(Constants.ROLE_ADMIN);
		ulist[1].setRoles(roles);
		ulist[1].setCreatedBy(ulist[0]);

		roles = Arrays.asList(Constants.ROLE_MANAGER,
				Constants.ROLE_CANDIDATE);
		ulist[2].setDefaultRole(Constants.ROLE_MANAGER);
		ulist[2].setRoles(roles);
		ulist[2].setCreatedBy(ulist[0]);

		roles = Arrays.asList(Constants.ROLE_INTERVIEWER);
		ulist[3].setDefaultRole(Constants.ROLE_INTERVIEWER);
		ulist[3].setRoles(roles);
		ulist[3].setCreatedBy(ulist[0]);

		roles = Arrays.asList(Constants.ROLE_CANDIDATE);

		for(int i = 0; i < 6; i++) {
			ulist[i+4].setDefaultRole(Constants.ROLE_CANDIDATE);
			ulist[i+4].setRoles(roles);
			ulist[i+4].setUserInfo(uilist[i]);
		}
		
		// manual submission... saved by admin
		ulist[4].setCreatedBy(ulist[0]);		
		
		roles = Arrays.asList(Constants.ROLE_MANAGER);
		ulist[10].setDefaultRole(Constants.ROLE_MANAGER);
		ulist[10].setRoles(roles);
		ulist[10].setCreatedBy(ulist[0]);

		roles = Arrays.asList(Constants.ROLE_INTERVIEWER);
		ulist[11].setDefaultRole(Constants.ROLE_INTERVIEWER);
		ulist[11].setRoles(roles);
		ulist[11].setCreatedBy(ulist[0]);
		
		for(UserEntity u: ulist) u.setAuthenticated(true);

		// SCRIPT ATTRIBUTES
		
		//tirar ou manter?
		sclist[1].setDerivedFrom(sclist[0]);
				

		// POSITION ATTRIBUTES
		
		List<String> locations = Arrays.asList(Constants.LOCATION_COIMBRA,
				Constants.LOCATION_PORTO);
		List<String> channels = Arrays.asList(Constants.SOCIAL_CRITICAL,
				Constants.SOCIAL_FACEBOOK);
		plist[0].setLocations(locations);
		plist[0].setAdvertisingChannels(channels);
		plist[0].setOpeningDate(ftDate.parse("2015-08-02"));
		plist[0].setSlaDate(ftDate.parse("2015-09-20"));

		locations = Arrays.asList(Constants.LOCATION_PORTO);
		channels = Arrays.asList(Constants.SOCIAL_CRITICAL,
				Constants.SOCIAL_FACEBOOK, 
				Constants.SOCIAL_GLASSDOOR);
		plist[1].setLocations(locations);
		plist[1].setAdvertisingChannels(channels);
		plist[1].setOpeningDate(ftDate.parse("2015-09-02"));
		
		locations = Arrays.asList(Constants.LOCATION_LISBOA);
		channels = Arrays.asList(Constants.SOCIAL_CRITICAL,
				Constants.SOCIAL_FACEBOOK);
		plist[2].setLocations(locations);
		plist[2].setAdvertisingChannels(channels);
		plist[2].setOpeningDate(ftDate.parse("2015-07-31"));

		locations = Arrays.asList(Constants.LOCATION_LISBOA,
				Constants.LOCATION_COIMBRA, Constants.LOCATION_PORTO);
		channels = Arrays.asList(Constants.SOCIAL_CRITICAL,
				Constants.SOCIAL_LINKEDIN);
		plist[3].setLocations(locations);
		plist[3].setAdvertisingChannels(channels);
		plist[3].setOpeningDate(ftDate.parse("2015-08-02"));
		plist[3].setStatus(Constants.STATUS_CLOSED);
		
		locations = Arrays.asList(Constants.LOCATION_COIMBRA);
		channels = Arrays.asList(Constants.SOCIAL_CRITICAL,
				Constants.SOCIAL_FACEBOOK, Constants.SOCIAL_LINKEDIN);
		plist[4].setLocations(locations);
		plist[4].setAdvertisingChannels(channels);
		plist[4].setOpeningDate(ftDate.parse("2015-06-29"));
		plist[4].setStatus(Constants.STATUS_CLOSED);
		
		locations = Arrays.asList(Constants.LOCATION_COIMBRA);
		channels = Arrays.asList(Constants.SOCIAL_CRITICAL);
		plist[4].setLocations(locations);
		plist[4].setAdvertisingChannels(channels);

		locations = Arrays.asList(Constants.LOCATION_COIMBRA);
		channels = Arrays.asList(Constants.SOCIAL_CRITICAL);
		plist[5].setLocations(locations);
		plist[5].setAdvertisingChannels(channels);
		
		// SUBMISSION ATTRIBUTES
		
		List<String> sources = Arrays.asList(Constants.SOURCE_EXPRESSO,
				Constants.SOURCE_FACEBOOK);
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

		Arrays.asList(Constants.SOURCE_EXPRESSO);
		slist[5].setPosition(plist[0]);
		slist[5].setSources(sources);
		slist[5].setStatus(Constants.STATUS_ACCEPTED);
		slist[5].setDate(ftDate.parse("2015-08-05"));
		
		slist[6].setPosition(plist[0]);
		slist[6].setSources(sources);

		sources = Arrays.asList(Constants.SOURCE_FACEBOOK,
				Constants.SOURCE_NETEMPREGO);
		slist[7].setPosition(plist[1]);
		slist[7].setSources(sources);
		slist[7].setDate(ftDate.parse("2015-09-09"));
		
		slist[8].setPosition(plist[0]);
		slist[8].setSources(sources);
		slist[8].setStatus(Constants.STATUS_PROPOSAL);
		slist[8].setDate(ftDate.parse("2015-08-10"));
		slist[8].setProposalDate(ftDate.parse("2015-08-20"));
		
		sources = Arrays.asList(Constants.SOURCE_FACEBOOK,
				Constants.SOURCE_EXPRESSO, 
				Constants.SOURCE_LINKEDIN);
		slist[9].setPosition(plist[2]);
		slist[9].setSources(sources);
		slist[9].setStatus(Constants.STATUS_REJECTED);
		slist[9].setDate(ftDate.parse("2015-08-10"));
		slist[9].setRejectReason("O candidato não tem currículo "
				+ "para a posição");
		
		sources = Arrays.asList(Constants.SOURCE_EXPRESSO);
		slist[10].setPosition(plist[2]);
		slist[10].setSources(sources);
		slist[10].setStatus(Constants.STATUS_NEGOTIATION);
		slist[10].setDate(ftDate.parse("2015-08-20"));
		slist[10].setProposalDate(ftDate.parse("2015-08-30"));

		slist[11].setPosition(plist[2]);
		slist[11].setStatus(Constants.STATUS_HIRED);
		slist[11].setDate(ftDate.parse("2015-08-01"));
		slist[11].setProposalDate(ftDate.parse("2015-08-21"));
		slist[11].setHiredDate(ftDate.parse("2015-08-31"));

		sources = Arrays.asList(Constants.SOURCE_FACEBOOK,
				Constants.SOURCE_EXPRESSO);
		slist[12].setPosition(plist[2]);
		slist[12].setSources(sources);
		slist[12].setStatus(Constants.STATUS_PROPOSAL);
		slist[12].setDate(ftDate.parse("2015-08-01"));
		slist[12].setProposalDate(ftDate.parse("2015-08-11"));

		sources = Arrays.asList(Constants.SOURCE_EXPRESSO);
		slist[13].setPosition(plist[3]);
		slist[13].setSources(sources);
		slist[13].setStatus(Constants.STATUS_HIRED);
		slist[13].setDate(ftDate.parse("2015-08-30"));
		slist[13].setProposalDate(ftDate.parse("2015-09-02"));
		slist[13].setHiredDate(ftDate.parse("2015-09-05"));
		
		sources = Arrays.asList(Constants.SOURCE_FACEBOOK);
		slist[14].setPosition(plist[3]);
		slist[14].setSources(sources);
		slist[14].setStatus(Constants.STATUS_REJECTED);
		slist[14].setDate(ftDate.parse("2015-08-17"));
		slist[14].setRejectReason("O candidato não tem currículo "
				+ "para a posição");

		sources = Arrays.asList(Constants.SOURCE_FACEBOOK,
				Constants.SOURCE_EXPRESSO);
		slist[15].setPosition(plist[3]);
		slist[15].setSources(sources);
		slist[15].setStatus(Constants.STATUS_HIRED);
		slist[15].setDate(ftDate.parse("2015-09-01"));
		slist[15].setProposalDate(ftDate.parse("2015-09-09"));
		slist[15].setHiredDate(ftDate.parse("2015-09-12"));

		sources = Arrays.asList(Constants.SOURCE_FACEBOOK,
				Constants.SOURCE_EXPRESSO);
		slist[16].setPosition(plist[3]);
		slist[16].setSources(sources);
		slist[16].setStatus(Constants.STATUS_ACCEPTED);
		slist[16].setDate(ftDate.parse("2015-08-30"));

		sources = Arrays.asList(Constants.SOURCE_FACEBOOK,
				Constants.SOURCE_NETEMPREGO);
		slist[17].setPosition(plist[4]);
		slist[17].setSources(sources);
		slist[17].setStatus(Constants.STATUS_ACCEPTED);
		slist[17].setDate(ftDate.parse("2015-07-12"));

		sources = Arrays.asList(Constants.SOURCE_FACEBOOK,
				Constants.SOURCE_LINKEDIN);
		slist[18].setPosition(plist[4]);
		slist[18].setSources(sources);
		slist[18].setStatus(Constants.STATUS_ACCEPTED);
		slist[18].setDate(ftDate.parse("2015-07-01"));

		sources = Arrays.asList(Constants.SOURCE_FACEBOOK,
				Constants.SOURCE_EXPRESSO,
				Constants.SOURCE_LINKEDIN);
		slist[19].setPosition(plist[4]);
		slist[19].setSources(sources);
		slist[19].setStatus(Constants.STATUS_HIRED);
		slist[19].setDate(ftDate.parse("2015-07-20"));
		slist[19].setProposalDate(ftDate.parse("2015-07-30"));
		slist[19].setHiredDate(ftDate.parse("2015-08-07"));
		

		// INTERVIEW ATTRIBUTES
		
		ilist[0].addInterviewer(ulist[3]);

		ilist[1].addInterviewer(ulist[2]);
		ilist[1].addInterviewer(ulist[11]);
		ilist[1].setCarriedOut(true);
		ilist[1].setFeedback("O candidato mostrou-se muito dinâmico blablabla");

		ilist[2].addInterviewer(ulist[10]);
		ilist[2].setCarriedOut(true);
		ilist[2].setFeedback("O candidato correspondeu ao currículo "
				+ "apresentado blablabla");

		ilist[3].addInterviewer(ulist[3]);
		ilist[3].setCarriedOut(true);
		ilist[3].setFeedback("O candidato tem uma criatividade "
				+ "impressionante blablabla");

		ilist[4].addInterviewer(ulist[3]);
		ilist[4].setCarriedOut(true);
		ilist[4].setFeedback("O candidato foi aprovado mas precisa de "
				+ "realizar nova entrevista");

		ilist[5].addInterviewer(ulist[3]);
		ilist[5].addInterviewer(ulist[10]);
		ilist[5].addInterviewer(ulist[11]);
		ilist[5].setCarriedOut(true);
		ilist[5].setFeedback("O candidato satisfez plenamente blablabla");
		ilist[5].setFirst(false);

		ilist[6].addInterviewer(ulist[3]);
		ilist[6].addInterviewer(ulist[11]);
		ilist[6].setCarriedOut(true);
		ilist[6].setFeedback("Recomenda-se a contratação");

		ilist[7].addInterviewer(ulist[3]);
		ilist[7].setCarriedOut(true);
		ilist[7].setFeedback("Excelente! Recomenda-se a contratação");
		
		ilist[8].addInterviewer(ulist[3]);
		ilist[8].addInterviewer(ulist[11]);
		ilist[8].setCarriedOut(false);
		
		ilist[9].addInterviewer(ulist[3]);
		ilist[9].setCarriedOut(true);
		ilist[9].setFeedback("Boa prestação. A considerar.");
		
		ilist[10].addInterviewer(ulist[3]);
		ilist[10].setCarriedOut(false);
		
		ilist[11].addInterviewer(ulist[3]);
		ilist[11].setCarriedOut(true);
		ilist[11].setFeedback("Mais valia para a equipa. Boas indicações");
		

		// ENTITIES PRESISTENCE
		
		for (UserEntity u : ulist) {
			this.userEJB.save(u);
		}

		for (ScriptEntity sc : sclist) {
			this.scriptEJB.save(sc);
		}
		
		for (QuestionEntity q : qList){
			this.questionEJB.save(q);
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
		
//		System.out.println("New style ID: "+this.styleEJB.saveAndReturn(new StyleEntity("Critical", "Critical Software", "Critical Software Recruitment", "#A50F13", "#660", true)).getId());
		
	}

}
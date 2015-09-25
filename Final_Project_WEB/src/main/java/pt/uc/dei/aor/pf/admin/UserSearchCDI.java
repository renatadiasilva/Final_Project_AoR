package pt.uc.dei.aor.pf.admin;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.SearchPattern;
import pt.uc.dei.aor.pf.beans.InterviewEJBInterface;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

import java.io.Serializable;


@Named
@SessionScoped
public class UserSearchCDI implements Serializable {

	private static final long serialVersionUID = 1886694735685187920L;

	private static final Logger log = LoggerFactory.getLogger(UserSearchCDI.class);

	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private InterviewEJBInterface interviewEJB;
	
	// search fields
	private String email, fname, lname, role, keyword;
	private Long id;

	private List<UserEntity> ulist;

	public UserSearchCDI() {
	}
	
	public void clean() {
		email = fname = lname = role = keyword = "";
		id = 0L;
		searchAll();
	}

	public void cleanAll() {
		email = fname = lname = role = keyword = "";
		id = 0L;
		ulist = new ArrayList<UserEntity>();
	}

	public void remove() {
		log.info("Removing user by id");
		log.debug("Id "+id);
		UserEntity user = userEJB.find(id);
		if (user != null) {
			int deleteCode = userEJB.delete(user);
			switch (deleteCode) {
			case -1:
				System.out.println("Não é possível apagar os dados"
					+ " do superAdmin! Fale com o gestor da base de dados");
				break;
			case -2: 
				// avisar que existem posições que precisam de novo gestor
				System.out.println("Há posições abertas geridas pelo user"
						+ " a apagar");
				System.out.println("Quer mudar os gestores agora ou depois"
						+ " manualmente?");
				// query repetida...
				List<PositionEntity> plist = 
						positionEJB.findOpenPositionsManagedByUser(user);
				System.out.println(plist); // adicionar código
				break;
			case -3:
				// avisar que existem entrevistas agendadas que
				// preciam novo entrevistador
				System.out.println("Há entrevistas agendadas que só têm"
						+ " como entrevistador o user a apagar");
				System.out.println("Quer adicionar entrevistador agora"
						+ " ou depois manualmente?"); 
				// query repetida...
				List<InterviewEntity> ilist = 
						interviewEJB.findScheduledInterviewsByUser(user);
				// falta verificar quais só têm um entrevistador...
				System.out.println(ilist); // adicionar código
				break;
			}
		} else log.error("No user with id "+id);
	}

	// ALL
	
	public List<UserEntity> getAllUlist() {
		this.searchAll();
		return ulist;
	}

	public void searchAll() {
		log.info("Searching for all users (except with no data)");
		this.ulist = userEJB.findAllNotRemoved();
	}

	public void searchRemoved() {
		log.info("Searching for all removed users");
		this.ulist = userEJB.findRemovedEmails();
	}

	public void searchAllAdmins() {
		log.info("Searching for all administrators");
		this.ulist = userEJB.findAllAdmins();
	}

	public void searchAllManagers() {
		log.info("Searching for all managers");
		this.ulist = userEJB.findAllManagers();
	}

	public void searchAllInterviewers() {
		log.info("Searching for all interviwers");
		this.ulist = userEJB.findAllInterviewers();
	}

	public void searchAllCandidates() {
		log.info("Searching for all candidates");
		this.ulist = userEJB.findAllCandidates();
	}

	public void searchByEmail() {
		log.info("Searching for users by email");
		String pattern = SearchPattern.preparePattern(email);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findUsersByEmail(pattern);
	}

	public void searchByName() {
		log.info("Searching for users by name (first/second)");
		String pattern = SearchPattern.preparePattern(fname);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findUsersByName(pattern);
	}

	public void searchByKeyword() {
		log.info("Searching for internal users by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		this.ulist = userEJB.findUsersByKeyword(pattern);
	}

	public void searchByKeywordAndRole() {
		log.info("Searching for internal users by keyword");
		String pattern = SearchPattern.preparePattern(keyword);
		log.debug("Internal search string: "+pattern);
		if (role == null) role = " ";
		log.debug("Search role: "+role);
		this.ulist = userEJB.findUsersByKeywordAndRole(pattern, role);
	}

	// getters e setters

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<UserEntity> getUlist() {
		return ulist;
	}

	public void setUlist(List<UserEntity> ulist) {
		this.ulist = ulist;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	// tirar
	public void meh() {
		// Deal with it
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String admin() {
		return Constants.ROLE_ADMIN;
	}
	
	public String manager() {
		return Constants.ROLE_MANAGER;
	}
	
	public String interviewer() {
		return Constants.ROLE_INTERVIEWER;
	}

	public String candidate() {
		return Constants.ROLE_CANDIDATE;
	}
}
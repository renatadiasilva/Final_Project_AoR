package pt.uc.dei.aor.pf;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.candidate.NewSubmissionCDI;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class CheckPositionCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5955528757542061330L;

	@Inject
	private UserSessionManagement userManagement;

	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private SubmissionEJBInterface submissionEJB;

	@EJB
	private UserEJBInterface userEJB;

	private String title;

	private String description;

	private List<PositionEntity> positions;

	private boolean forbidden, candidate;

	// 1ª Verificação
	public boolean forbidden(PositionEntity position){
		this.forbidden=this.candidate=false;

		this.forbidden=position.getPositionManager().getEmail().equals(userManagement.getUserMail());

		return this.forbidden;
	}

	// 2ª Verificação
	public boolean candidate(PositionEntity position){
		
		if(!this.forbidden){
			UserEntity candidate=this.userEJB.findUserByEmail(userManagement.getUserMail());
			
			this.candidate = positionEJB.alreadyCandidateOfPosition(candidate, position);
			return this.candidate;
		}
		return false;
	}

	// 3ª Verificação
	public boolean submitable(){
		return !this.forbidden&&!this.candidate;
	}

	public void checkPosition(PositionEntity position){
		this.title=position.getTitle();
		this.description=position.getDescription();
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public List<PositionEntity> getPositions() {
		this.positions=this.positionEJB.findOpenPositions();
		return positions;
	}

	public void setPositions(List<PositionEntity> positions) {
		this.positions = positions;
	}

}

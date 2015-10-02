package pt.uc.dei.aor.pf;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;

import java.io.Serializable;

@Named
@SessionScoped
public class AssociateSpontaneousCDI implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4006581824096212501L;
	
	@Inject
	private UserSessionManagement userManagement;
	
	@EJB
	private UserEJBInterface userEJB;
	
	@EJB
	private PositionEJBInterface positionEJB;
	
	@EJB
	private SubmissionEJBInterface submissionEJB;
	
	private SubmissionEntity spontaneousSubmission;
	
	private List<PositionEntity> positions;
	
	private PositionEntity position;
	
	private List<SubmissionEntity> spontaneousSubmissions;
	
	public void clean(){
		this.spontaneousSubmission=null;
		this.position=null;
	}
	
	public void loadSpontaneousSubmission(SubmissionEntity submission){
		this.spontaneousSubmission=submission;
	}
	
	public void unloadSpontaneousSubmission(){
		this.spontaneousSubmission=null;
	}
	
	public boolean hasSpontaneousSubmission(){
		return this.spontaneousSubmission!=null;
	}
	
	public boolean currentSpontaneousSubmission(SubmissionEntity submission){
		if(this.spontaneousSubmission==null)return false;
		return this.spontaneousSubmission.getId()==submission.getId();
	}

	public List<PositionEntity> getPositions() {
		this.positions=this.positionEJB.findOpenPositions();
		return positions;
	}

	public void setPositions(List<PositionEntity> positions) {
		this.positions = positions;
	}
	
	public void associatePosition(PositionEntity position) {
		this.position=position;
		
		System.out.println("Associate Position");
		
		SubmissionEntity clonedSubmission= new SubmissionEntity(
				spontaneousSubmission.getCandidate(), Constants.STATUS_SUBMITED,
				null, spontaneousSubmission.getSources(), false);
		
		UserEntity currentUser=this.userEJB.findUserByEmail(this.userManagement.getUserMail());
		
		clonedSubmission.setPosition(this.position);		
		clonedSubmission.setAssociatedBy(currentUser);
		clonedSubmission.setDerivedFrom(this.spontaneousSubmission);

		this.submissionEJB.save(clonedSubmission);
		clonedSubmission.setDate(this.spontaneousSubmission.getDate());
		this.submissionEJB.update(clonedSubmission);

		this.spontaneousSubmission.setAlreadyAssociated(true);
		this.submissionEJB.update(this.spontaneousSubmission);
		
		// Envia os mails aqui
		
		this.clean();
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nova candidatura criada a partir de candidatura espontânea."));
		
	}

	public List<SubmissionEntity> getSpontaneousSubmissions() {
		this.spontaneousSubmissions=this.submissionEJB.findSpontaneousSubmissions();
		return spontaneousSubmissions;
	}

	public void setSpontaneousSubmissions(List<SubmissionEntity> spontaneousSubmissions) {
		this.spontaneousSubmissions = spontaneousSubmissions;
	}

	public void loadPosition(PositionEntity position){
		this.position=position;
	}
	
	public boolean isCandidate(PositionEntity position) {
		return this.positionEJB.alreadyCandidateOfPosition(this.spontaneousSubmission.getCandidate(), position);
	}
}

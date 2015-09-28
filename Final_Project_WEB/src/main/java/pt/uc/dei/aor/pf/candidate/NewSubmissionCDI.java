package pt.uc.dei.aor.pf.candidate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.aor.pf.ExtraAd;
import pt.uc.dei.aor.pf.beans.PositionEJBInterface;
import pt.uc.dei.aor.pf.beans.SubmissionEJBInterface;
import pt.uc.dei.aor.pf.beans.UserEJBInterface;
import pt.uc.dei.aor.pf.constants.Constants;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;
import pt.uc.dei.aor.pf.session.UserSessionManagement;
import pt.uc.dei.aor.pf.upload.UploadFile;

@Named
@SessionScoped
public class NewSubmissionCDI implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7876316223190235623L;

	@Inject
	private UserSessionManagement userManagement;

	@Inject
	private UploadFile uploadFile;

	@EJB
	private UserEJBInterface userEJB;

	@EJB
	private SubmissionEJBInterface submissionEJB;

	private PositionEntity position;

	private UploadedFile file;

	private boolean critical, linkedin, glassdoor, facebook;

	private List<String> advertisingChannels;

	private List<ExtraAd> altAdvertisingChannels;

	private String extraAd;

	private void compileAds(){

		// advertisingChannels
		if(this.critical)this.advertisingChannels.add(Constants.SOCIAL_CRITICAL);
		if(this.linkedin)this.advertisingChannels.add(Constants.SOCIAL_LINKEDIN);
		if(this.glassdoor)this.advertisingChannels.add(Constants.SOCIAL_GLASSDOOR);
		if(this.facebook)this.advertisingChannels.add(Constants.SOCIAL_FACEBOOK);
		if(!this.altAdvertisingChannels.isEmpty())
			for(ExtraAd ad:this.altAdvertisingChannels)
				this.advertisingChannels.add(ad.getAd());

	}

	public void cleanAds(){
		critical=linkedin=glassdoor=facebook=false;

		this.advertisingChannels=new ArrayList<String>();
		this.altAdvertisingChannels=new ArrayList<ExtraAd>();

		this.advertisingChannels.clear();
		this.altAdvertisingChannels.clear();
	}

	public void addAltAdvertisingChannels() {
		System.out.println("Adicionar: "+this.extraAd);
		if(this.extraAd!=null&&!this.extraAd.isEmpty())
			this.altAdvertisingChannels.add(new ExtraAd(this.extraAd));
		this.extraAd="";
	}

	public void deleteAltAdvertisingChannels(ExtraAd ad) {
		this.altAdvertisingChannels.remove(ad);
	}

	public void newSubmission(FileUploadEvent event){
		this.file=event.getFile();

		this.compileAds();

		UserEntity candidate=this.userEJB.findUserByEmail(this.userManagement.getUserMail());

		SubmissionEntity submission=new SubmissionEntity(candidate, Constants.STATUS_SUBMITED, null, this.advertisingChannels, false);
		submission.setPosition(this.position);

		// Persiste e devolve a entidade já com o id
		submission=this.submissionEJB.saveAndReturn(submission);

		// Grava a motivationLetter
		this.uploadFile.uploadFile(this.file, UploadFile.FOLDER_SUBMISSION_MOTIVATION_LETTER, submission.getId(), UploadFile.DOCUMENT_EXTENSION_PDF);

		this.cleanAds();
		this.position=null;

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nova candidatura submetida. Durante a próxima hora pode mudar o CV e a Carta de Motivação desta candidatura na área das suas candidaturas."));
	}

	public void associatePosition(PositionEntity position) {
		this.position = position;
		this.cleanAds();
	}

	public void dissociatePosition(){
		this.position=null;
		this.cleanAds();
	}

	public boolean hasPosition(){
		return this.position!=null;
	}

	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	public boolean isLinkedin() {
		return linkedin;
	}

	public void setLinkedin(boolean linkedin) {
		this.linkedin = linkedin;
	}

	public boolean isGlassdoor() {
		return glassdoor;
	}

	public void setGlassdoor(boolean glassdoor) {
		this.glassdoor = glassdoor;
	}

	public boolean isFacebook() {
		return facebook;
	}

	public void setFacebook(boolean facebook) {
		this.facebook = facebook;
	}

	public String getExtraAd() {
		return extraAd;
	}

	public void setExtraAd(String extraAd) {
		this.extraAd = extraAd;
	}

	public List<ExtraAd> getAltAdvertisingChannels() {
		return altAdvertisingChannels;
	}

	public void setAltAdvertisingChannels(List<ExtraAd> altAdvertisingChannels) {
		this.altAdvertisingChannels = altAdvertisingChannels;
	}

}

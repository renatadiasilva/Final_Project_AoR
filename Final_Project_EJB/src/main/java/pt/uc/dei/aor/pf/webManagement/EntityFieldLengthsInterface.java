package pt.uc.dei.aor.pf.webManagement;

import javax.ejb.Local;

@Local
public interface EntityFieldLengthsInterface {

	//User
	public abstract int userFirstName();

	public abstract int userLastName();

	//UserInfo
	public abstract int userInfoAddress();

	public abstract int userInfoCity();

	public abstract int userInfoHomePhone();

	public abstract int userInfoMobilePhone();

	public abstract int userInfoCountry();

	public abstract int userInfoCourse();

	public abstract int userInfoSchool();

	//Position
	public abstract int positionTitle();

	public abstract int positionCompany();

	public abstract int positionDescription();

	// Submission
	public abstract int submissionRejectReason();

	//InterviewEntity
	public abstract int interviewFeedback();

	//ScriptEntity
	public abstract int scriptTitle();

	public abstract int scriptComments();

	//QuestionEntity
	public abstract int questionQuestion();

}

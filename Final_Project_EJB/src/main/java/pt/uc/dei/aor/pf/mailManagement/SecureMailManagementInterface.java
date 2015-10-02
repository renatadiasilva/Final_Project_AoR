package pt.uc.dei.aor.pf.mailManagement;

import java.util.Date;

import javax.ejb.Local;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Local
public interface SecureMailManagementInterface {

	public abstract void passwordRecovery(UserEntity user, String temporaryPassword);

	public abstract void candidateToAuthenticate(UserEntity newUser);

	public abstract void authenticatedEmail(UserEntity user);

	public abstract void sendPassToNewUser(UserEntity newUser, 
			String temporaryPassword);

	public abstract void newEmail(UserEntity user);

	public abstract void slaWarning(PositionEntity position);

	public abstract void newPositionWarning(PositionEntity position);

	public abstract void newSubmissionWarning(SubmissionEntity submission);

	public abstract void newCandidateWarning(SubmissionEntity submission);

	public abstract void notifyHired(SubmissionEntity submission);

	public abstract void notifyRejected(SubmissionEntity submission);

	public abstract void notifyScheduledInterview(InterviewEntity interview);

	public abstract void notifyChangeDateInterviewCand(InterviewEntity interview,
			Date newDate);

	public abstract void notifyNewInterviewInt(InterviewEntity interview,
			UserEntity newInterviewer, Date date);

	public abstract void notifyChangeInterviewInt(InterviewEntity interview, Date newDate,
			UserEntity oldInterviewer, boolean dateIsNew, boolean scriptIsNew);

}

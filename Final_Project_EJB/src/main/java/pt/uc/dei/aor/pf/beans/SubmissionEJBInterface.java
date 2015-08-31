package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface SubmissionEJBInterface {
	
	public abstract void save(SubmissionEntity submission);
	public abstract void update(SubmissionEntity submission);
	public abstract void delete(SubmissionEntity submission);
	public abstract SubmissionEntity find(Long id);
	public abstract List<SubmissionEntity> findAll();
	public abstract List<SubmissionEntity> findSubmissionsByPosition(PositionEntity position);
	public abstract List<SubmissionEntity> findSpontaneousSubmissions();
	
	// listagens para relatório?? (marketing)
	public abstract List<SubmissionEntity> findSubmissionsBySource(String source); // lista!!!
	
	public abstract List<SubmissionEntity> findSubmissionsByCandidate(UserEntity candidate); //??position
	
	public abstract List<SubmissionEntity> findSubmissionsByDate(Date date1, Date date2, String period);  // daily/monthly/yearly??
	public abstract List<SubmissionEntity> findSpontaneousSubmissionsByDate(Date date1, Date date2, String period);
	public abstract List<SubmissionEntity> findRejectedSubmissions(Date date1, Date date2, String period);  // more status??? ver!!

}

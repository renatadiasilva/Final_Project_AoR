package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.SubmissionEntity;

public interface SubmissionEJBInterface {
	
	public abstract void save(SubmissionEntity submission);
	public abstract void update(SubmissionEntity submission);
	public abstract void delete(SubmissionEntity submission);
	public abstract SubmissionEntity find(Long id);
	public abstract List<SubmissionEntity> findAll();
	public abstract List<SubmissionEntity> findSpontaneousSubmissions();
	public abstract List<SubmissionEntity> findSubmissionsByDate(Date date1,
			Date date2);
	public abstract List<SubmissionEntity> findSpontaneousSubmissionsByDate(
			Date date1, Date date2);
	public abstract List<SubmissionEntity> findRejectedSubmissions(Date date1,
			Date date2);
	public abstract List<SubmissionEntity> findPresentedProposals(Date date1,
			Date date2);
	public abstract List<SubmissionEntity> findSubmissionsBySource(
			String source, Date date1, Date date2);
		
}

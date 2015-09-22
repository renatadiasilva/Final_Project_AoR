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
	public abstract void addPositionToSpontaneous(SubmissionEntity submission,
			PositionEntity position, UserEntity user);
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
	public abstract List<SubmissionEntity> findSubmissionsOfPosition(
			PositionEntity position);
	public abstract List<SubmissionEntity> findSubmissionsOfCandidate(
			UserEntity candidate);
	public abstract List<Object[]> averageTimeToHired(Date date1, Date date2,
			char p);
	public abstract List<Object[]> countSubmissionsByDate(Date date1,
			Date date2, char p, String result, String restriction);
	public abstract Long countTotalSubmissions(Date date1,
			Date date2);
	public abstract Long countTotalSubmissionsPos(Date date1,
			Date date2);
	public abstract Long countTotalSpontaneous(Date date1,
			Date date2);
	public abstract Long countTotalRejected(Date date1, Date date2);
	public abstract Long countTotalProposals(Date date1, Date date2);
	public abstract Long countTotalHired(Date date1, Date date2);
	public abstract Long countTotalRejectedPos(Date date1, Date date2);
	public abstract Long countTotalProposalsPos(Date date1, Date date2);
	public abstract Double overallAverageTimeToHired(Date date1, Date date2);
	
}

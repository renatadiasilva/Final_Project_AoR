package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.SubmissionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public class SubmissionEJBImp implements SubmissionEJBInterface {

	@Override
	public void save(SubmissionEntity submission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(SubmissionEntity submission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(SubmissionEntity submission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SubmissionEntity find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubmissionEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubmissionEntity> findSubmissionsByPosition(
			PositionEntity position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubmissionEntity> findSpontaneousSubmissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubmissionEntity> findSubmissionsByCandidate(
			UserEntity candidate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubmissionEntity> findSubmissionsByDate(Date date1, Date date2,
			String period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubmissionEntity> findSpontaneousSubmissionsByDate(Date date1,
			Date date2, String period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubmissionEntity> findRejectedSubmissions(Date date1,
			Date date2, String period) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubmissionEntity> findSubmissionsBySource(String source) {
		// TODO Auto-generated method stub
		return null;
	}

}

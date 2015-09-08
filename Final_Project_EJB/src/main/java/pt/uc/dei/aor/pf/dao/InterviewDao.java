package pt.uc.dei.aor.pf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import pt.uc.dei.aor.pf.entities.InterviewEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

@Stateless
public class InterviewDao extends GenericDao<InterviewEntity> {

	public InterviewDao() {
		super(InterviewEntity.class);
	}

	public List<InterviewEntity> findCarriedOutInterviews(Date date1,
			Date date2) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("date1", date1);
		parameters.put("date2", date2);  //formato!!
		return super.findSomeResults("Interview.findCarriedOutInterviews", parameters);
	}
	
	public List<InterviewEntity> findCarriedOutInterviewsByUser(
			UserEntity interviewer) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", interviewer);
		return super.findSomeResults("Interview.findCarriedOutInterviewsByUser", parameters);
	}
	
	public List<InterviewEntity> findScheduledInterviewsByUser(
			UserEntity interviewer) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", interviewer);
		return super.findSomeResults("Interview.findScheduledInterviewsByUser", parameters);
	}

	public List<InterviewEntity> findScheduledInterviewsByCandidate(
			UserEntity candidate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", candidate);
		return super.findSomeResults("Interview.findScheduledInterviewsByCandidate", parameters);
	}
	
}
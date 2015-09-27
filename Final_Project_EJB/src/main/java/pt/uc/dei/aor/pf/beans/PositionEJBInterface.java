package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.ScriptEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface PositionEJBInterface {
	
	public abstract void save(PositionEntity position);
	public abstract void update(PositionEntity position);
	public abstract void delete(PositionEntity position);
	public abstract PositionEntity find(Long id);
	public abstract List<PositionEntity> findAll();
	public abstract List<PositionEntity> findAllOrderByCode();
	public abstract List<PositionEntity> findOpenPositions();
	public abstract List<PositionEntity> findCloseToSLAPositions(
			int daysBefore);
	public abstract List<PositionEntity> findPositionsByDate(Date openingDate1, 
			Date openingDate2);
	public abstract List<PositionEntity> findPositionsByCode(
			String codePattern);
	public abstract List<PositionEntity> findPositionsByTitle(String title);
	public abstract List<PositionEntity> findPositionsByLocationsOne(
			List<String> location);
	public abstract List<PositionEntity> findPositionsByLocationsAll(
			List<String> location);
	public abstract List<PositionEntity> findPositionsByStatus(
			String currentState);
	public abstract List<PositionEntity> findPositionsByCompany(String company);
	public abstract List<PositionEntity> findPositionsByTechArea(
			String tecnhicalArea);
	public abstract List<PositionEntity> findPositions(Date openingDate1, 
			Date openingDate2, String positionCode,
			String title, String location, String currentStatus, String company, 
			String technicalArea);
	public abstract List<PositionEntity> findPositionsByManager(
			Date openingDate1, Date openingDate2, String positionCode, 
			String title, String location, String currentStatus, String company, 
			String technicalArea, UserEntity positionManager);
	public abstract List<PositionEntity> findPositionsByKeyword(String keyword);
	public abstract List<PositionEntity> findPositionsByKeywordShort(
			String keyword, String status);
	public abstract List<PositionEntity> findPositionsByKeywordAndManager(
			String keyword, UserEntity positionManager);
	public abstract List<PositionEntity> findPositionsByCandidate(
			UserEntity candidate);
	public abstract boolean alreadyCandidateOfPosition(UserEntity candidate,
			PositionEntity position);
	public abstract List<PositionEntity> findPositionsByScript(
			ScriptEntity script);	
	public abstract List<PositionEntity> findPositionsManagedByUser(
			UserEntity manager);
	public List<PositionEntity> findOpenPositionsManagedByUser(
			UserEntity manager);
	public abstract List<Object[]> countSubmissionsByPosition(Date date1,
			Date date2);
	public abstract List<Object[]> countRejectedByPosition(Date date1,
			Date date2);
	public abstract List<Object[]> countProposalsByPosition(Date date1,
			Date date2);
	public abstract List<Object[]> averageTimeToClose(Date date1, Date date2,
			char period);
	public abstract Double overallAverageTimeToClose(Date date1, Date date2);
	public abstract List<PositionEntity> findAfterSLAPositions();
	public abstract List<PositionEntity> findClosedPositions();
}
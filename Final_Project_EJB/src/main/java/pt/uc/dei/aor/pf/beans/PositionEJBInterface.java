package pt.uc.dei.aor.pf.beans;

import java.util.Date;
import java.util.List;

import pt.uc.dei.aor.pf.entities.PositionEntity;
import pt.uc.dei.aor.pf.entities.UserEntity;

public interface PositionEJBInterface {
	
	//validações?? passar atributos e não position inteira??)
	//public abstract PositionEntity addPosition(........);
	public abstract void save(PositionEntity position);
	public abstract void update(PositionEntity position);
	//public abstract void updatePositionTitle(PositionEntity position, ........);
	public abstract void delete(PositionEntity position);
	public abstract PositionEntity find(Long id);
	public abstract List<PositionEntity> findAll();
	public abstract List<PositionEntity> findOpenPositions();  //Closed?? onHold??
	public abstract List<PositionEntity> findCloseToSLAPositions(int daysBefore);
	public abstract List<PositionEntity> findPositionsByDate(Date openingDate1, Date openingDate2);
	public abstract List<PositionEntity> findPositionsByCode(String positionCode);
	public abstract List<PositionEntity> findPositionsByTitle(String title);
	public abstract List<PositionEntity> findPositionsByLocation(List<String> location);
	public abstract List<PositionEntity> findPositionsByStatus(String currentState);
	public abstract List<PositionEntity> findPositionsByCompany(String company);
	public abstract List<PositionEntity> findPositionsByTechArea(String tecnhicalArea);
	public abstract List<PositionEntity> findPositions(Date openingDate1, Date openingDate2, String positionCode,
			String title, String location, String currentStatus, String company, String technicalArea);
	public abstract List<PositionEntity> findPositionsByManager(Date openingDate1, Date openingDate2,
			String positionCode, String title, String location, String currentStatus, String company, 
			String technicalArea, UserEntity positionManager);
	public abstract List<PositionEntity> findPositionsByKeyword(String keyword);
	public abstract List<PositionEntity> findPositionsByKeywordByManager(String keyword, UserEntity positionManager);
	public abstract List<PositionEntity> findPositionsByCandidate(UserEntity candidate); //??submission SIIIIM
	
}
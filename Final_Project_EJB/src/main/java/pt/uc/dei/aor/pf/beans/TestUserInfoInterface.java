package pt.uc.dei.aor.pf.beans;

import pt.uc.dei.aor.pf.entities.UserInfoEntity;

public interface TestUserInfoInterface {

	public void save(UserInfoEntity userInfo);

	public void update(UserInfoEntity userInfo);

	public void delete(UserInfoEntity userInfo);

}

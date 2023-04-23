package rangiffler.data.dao;

import rangiffler.data.entity.UserAuthEntity;

public interface UserAuthDao extends DAO {

    UserAuthEntity getUser(String username);

}

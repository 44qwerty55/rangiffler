package rangiffler.data.dao;

import rangiffler.data.entity.UserDataEntity;

public interface UserDataDao extends DAO {

    UserDataEntity getUser(String username);

    void deleteUser(String username);
}

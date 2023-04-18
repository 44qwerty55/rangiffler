package rangiffler.data.dao;

import rangiffler.data.entity.UserDataEntity;
import rangiffler.model.FriendStatus;
import rangiffler.model.UserJson;

public interface UserDataDao extends DAO {

    UserDataEntity getUser(String username);

    FriendStatus getFriendStatus(UserJson user, UserJson friend);

    void deleteUser(String username);

    void deleteUserFriend(String username);
}

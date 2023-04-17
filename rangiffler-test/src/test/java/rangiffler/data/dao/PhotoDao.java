package rangiffler.data.dao;

import rangiffler.data.entity.PhotoEntity;
import rangiffler.data.entity.UserDataEntity;

import java.util.List;
import java.util.UUID;

public interface PhotoDao extends DAO {

    List<PhotoEntity> getUsersPhoto(String username);

    void deletePhoto(UUID photoUuid);

    void deleteUserPhoto(String username);
}

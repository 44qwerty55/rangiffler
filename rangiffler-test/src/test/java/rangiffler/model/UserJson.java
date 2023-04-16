package rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import rangiffler.data.entity.UserDataEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class UserJson {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatar;
    private String friendStatus;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private boolean isAvailable;

    public UserJson() {
    }

    public UserJson(String username, String password) {
        this.username = username;
        this.lastName = password;
        this.isAvailable = true;
    }

    public static UserJson fromEntity(UserDataEntity userDataEntity) {
        UserJson userJson = new UserJson();
        byte[] photo = userDataEntity.getAvatar();
        userJson.setId(userDataEntity.getId())
                .setUsername(userDataEntity.getUsername())
                .setFirstName(userDataEntity.getFirstName())
                .setLastName(userDataEntity.getLastName())
        .setAvatar(photo != null && photo.length > 0 ? new String(userDataEntity.getAvatar(), StandardCharsets.UTF_8) : null);
        return userJson;
    }

}

package rangiffler.data.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class UserDataEntity {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private byte[] avatar;


}

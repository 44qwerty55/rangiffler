package rangiffler.data.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

package rangiffler.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class FriendsNameForFoto {

    private List<String> FriendsName;

    public List<String> getFriendsName() {
        return FriendsName;
    }

    public void setFriendsName(List<String> friendsName) {
        FriendsName = friendsName;
    }
}

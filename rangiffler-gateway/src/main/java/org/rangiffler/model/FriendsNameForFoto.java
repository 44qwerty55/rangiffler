package org.rangiffler.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class FriendsNameForFoto {

    private List<String> FriendsName;

    public static FriendsNameForFoto fromUserJson(List<UserJson> users) {
        FriendsNameForFoto friendsNameForFoto = new FriendsNameForFoto();
        List<String> names = new ArrayList<>();
        users.forEach(us ->  names.add(us.getUsername()) );
        friendsNameForFoto.setFriendsName(names);
        return friendsNameForFoto;

    }
}

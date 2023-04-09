package org.rangiffler.service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.rangiffler.model.PhotoJson;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

 // private final UserService userService = new UserService();
  //private final CountryService countryService = new CountryService();

  List<PhotoJson> mainUserPhotoList = new ArrayList<>();

  List<PhotoJson> allUsersPhotoList = List.of(
      new PhotoJson());
/*
  public PhotoJson addPhoto(PhotoJson photoJson) {
    photoJson.setId(UUID.randomUUID());
    photoJson.setUsername(userService.getCurrentUser().getUsername());
    mainUserPhotoList.add(photoJson);
    return photoJson;
  }

  public List<PhotoJson> getAllUserPhotos() {
    return mainUserPhotoList;
  }


 */
  public PhotoJson editPhoto(PhotoJson photoJson) {
    PhotoJson photo = mainUserPhotoList.stream().filter(ph -> ph.getId().equals(photoJson.getId()))
        .findFirst().orElseThrow();
    photo.setDescription(photoJson.getDescription());
    photo.setCountryJson(photoJson.getCountryJson());
    return photo;
  }

  public List<PhotoJson> getAllFriendsPhotos() {
    return allUsersPhotoList;
  }

  /*
  public void deletePhoto(UUID photoId) {
    PhotoJson photoJson = mainUserPhotoList.stream().filter(ph -> ph.getId().equals(photoId))
        .findFirst().orElseThrow();
    mainUserPhotoList.remove(photoJson);
  }

   */
}

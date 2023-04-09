package org.rangiffler.controller;

import java.util.List;
import java.util.UUID;
import org.rangiffler.model.PhotoJson;
import org.rangiffler.service.PhotoClient;
import org.rangiffler.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {

  private static final Logger LOG = LoggerFactory.getLogger(PhotoController.class);
  private final PhotoService photoService;
  private final PhotoClient photoClient;

  @Autowired
  public PhotoController(PhotoService photoService,PhotoClient photoClient) {
    this.photoService = photoService;
    this.photoClient = photoClient;
  }


  @GetMapping("/photos")
  public List<PhotoJson> getPhotosForUser(@AuthenticationPrincipal Jwt principal) {
    String username = principal.getClaim("sub");
    return photoClient.getAllPhotosByUsername(username);
  }

  @PostMapping("/photos")
  public PhotoJson addPhoto(@AuthenticationPrincipal Jwt principal,
                            @RequestBody PhotoJson photoJson) {
    String username = principal.getClaim("sub");
    photoJson.setUsername(username);
   // LOG.atInfo().log(photoJson.toString());
    return photoClient.addPhoto(photoJson);
  }

  @DeleteMapping("/photos")
  public void deletePhoto(@RequestParam UUID photoId) {
    photoClient.deletePhoto(photoId);
  }

  @GetMapping("/friends/photos")
  public List<PhotoJson> getAllFriendsPhotos() {
    return photoService.getAllFriendsPhotos();
  }



  @PatchMapping("/photos/{id}")
  public PhotoJson editPhoto(@RequestBody PhotoJson photoJson) {
    return photoService.editPhoto(photoJson);
  }



}

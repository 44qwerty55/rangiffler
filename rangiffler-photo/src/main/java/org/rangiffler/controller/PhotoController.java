package org.rangiffler.controller;

import org.rangiffler.model.FriendsNameForFoto;
import org.rangiffler.model.PhotoJson;
import org.rangiffler.service.RangifflerPhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PhotoController {

    private static final Logger LOG = LoggerFactory.getLogger(PhotoController.class);

    private final RangifflerPhotoService rangifflerPhotoService;

    @Autowired
    public PhotoController(RangifflerPhotoService rangifflerPhotoService) {
        this.rangifflerPhotoService = rangifflerPhotoService;
    }

    @GetMapping("/photos")
    public List<PhotoJson> photos(String username) {
        return rangifflerPhotoService.getAllPhotoByUserName(username);
    }

    @PostMapping("/addPhoto")
    public PhotoJson addPhoto(@RequestBody PhotoJson photoJson) {
        return rangifflerPhotoService.addPhoto(photoJson);
    }

    @DeleteMapping("/dellPhoto")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void dellPhoto(@RequestParam String photoUuid) {
       rangifflerPhotoService.deletePhoto(UUID.fromString(photoUuid));
    }
    @PostMapping("/friends/photos")
    public List<PhotoJson> getAllFriendsPhotos(@RequestBody FriendsNameForFoto friendsNameForFoto){
        return rangifflerPhotoService.getAllFriendsPhotos(friendsNameForFoto);
    }

    @PostMapping("/editPhoto")
    public PhotoJson editPhoto(@RequestBody PhotoJson photoJson) {
        return rangifflerPhotoService.editPhoto(photoJson);
    }
}

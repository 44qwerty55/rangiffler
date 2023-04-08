package org.rangiffler.controller;

import org.rangiffler.model.PhotoJson;
import org.rangiffler.service.RangifflerPhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public PhotoJson addPhoto(@RequestParam PhotoJson photoJson) {
        return rangifflerPhotoService.addPhoto(photoJson);
    }

    @PostMapping("/dellPhoto")
    public void dellPhoto(@RequestParam UUID photoUuid) {
       rangifflerPhotoService.deletePhoto(photoUuid);
    }
}

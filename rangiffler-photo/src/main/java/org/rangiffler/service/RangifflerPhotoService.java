package org.rangiffler.service;

import jakarta.annotation.Nonnull;
import org.rangiffler.data.PhotoEntity;
import org.rangiffler.data.reposotory.PhotoRepository;
import org.rangiffler.model.PhotoJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RangifflerPhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public RangifflerPhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public @Nonnull
    List<PhotoJson> getAllPhotoByUserName(@Nonnull String username) {
        return photoRepository.findAllByUsername(username)
                .stream()
                .map(PhotoJson::fromEntity)
                .collect(Collectors.toList());
    }

    public @Nonnull
    PhotoJson addPhoto(@Nonnull PhotoJson photo) {
        String code = photo.getCountryJson().getCode();
        PhotoEntity photoEntity = photoRepository.findByUsernameAndCode(photo.getUsername(), code);
        if (photoEntity == null) {
            photoEntity = PhotoEntity.fromJson(photo);
            return PhotoJson.fromEntity(photoRepository.save(photoEntity));
        }
        return PhotoJson.fromEntity(photoEntity);
    }

    public @Nonnull
    void deletePhoto(@Nonnull UUID id) {
        Optional<PhotoEntity> photoEntityById = photoRepository.findById(id);
        if (photoEntityById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can`t find spend by given id: " + id);
        } else {
            PhotoEntity photoEntity = photoEntityById.get();
            photoRepository.delete(photoEntity);
        }
    }



}

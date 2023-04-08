package org.rangiffler.data.reposotory;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.rangiffler.data.PhotoEntity;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository  extends JpaRepository<PhotoEntity, UUID> {

    @Nullable
    List<PhotoEntity> findAllByUsername(@Nonnull String username);

    @Nullable
    PhotoEntity findByUsernameAndCode(@Nonnull String username, @Nonnull String code);

}

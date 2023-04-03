package org.rangiffler.data.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.rangiffler.data.CountriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountriesRepository extends JpaRepository<CountriesEntity, UUID>{


        @Nullable
        CountriesEntity findByCode(@Nonnull String code);

}

package com.giftkeeper.persistence.api;

import com.giftkeeper.domain.Occasion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OccasionRepository {
    Occasion save(Occasion occasion);
    Optional<Occasion> findById(UUID id);
    List<Occasion> findByPersonId(UUID personId);
    List<Occasion> findAll();
    void deleteById(UUID id);
}

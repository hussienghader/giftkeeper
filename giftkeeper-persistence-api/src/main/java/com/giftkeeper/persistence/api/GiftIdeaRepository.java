package com.giftkeeper.persistence.api;

import com.giftkeeper.domain.GiftIdea;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GiftIdeaRepository {
    GiftIdea save(GiftIdea giftIdea);
    Optional<GiftIdea> findById(UUID id);
    List<GiftIdea> findByPersonId(UUID personId);
    List<GiftIdea> findAll();
    void deleteById(UUID id);
}

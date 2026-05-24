package com.giftkeeper.persistence.jpa;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.Occasion;
import com.giftkeeper.domain.Person;

final class DomainMapper {
    private DomainMapper() {
    }

    static Person toDomain(final JpaPersonEntity entity) {
        return new Person(entity.getId(), entity.getName(), entity.getBirthDate());
    }

    static Occasion toDomain(final JpaOccasionEntity entity) {
        return new Occasion(entity.getId(), entity.getPerson().getId(), entity.getType(), entity.getDate(), entity.getDescription());
    }

    static GiftIdea toDomain(final JpaGiftIdeaEntity entity) {
        return new GiftIdea(entity.getId(), entity.getPerson().getId(),
            entity.getOccasion() == null ? null : entity.getOccasion().getId(),
            entity.getTitle(), entity.getEstimatedPrice(), entity.getStatus(), entity.getNotes());
    }
}

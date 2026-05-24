package com.giftkeeper.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Occasion {
    private final UUID id;
    private final UUID personId;
    private OccasionType type;
    private LocalDate date;
    private String description;

    public Occasion(final UUID id, final UUID personId, final OccasionType type, final LocalDate date, final String description) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.personId = Objects.requireNonNull(personId, "personId must not be null");
        setType(type);
        setDate(date);
        setDescription(description);
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public OccasionType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setType(final OccasionType type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    public void setDate(final LocalDate date) {
        this.date = Objects.requireNonNull(date, "date must not be null");
    }

    public void setDescription(final String description) {
        final String normalized = description == null ? "" : description.trim();
        if (normalized.length() > 255) {
            throw new DomainException("description must be at most 255 characters");
        }
        this.description = normalized;
    }
}

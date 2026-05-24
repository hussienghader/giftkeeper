package com.giftkeeper.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class GiftIdea {
    private final UUID id;
    private final UUID personId;
    private final UUID occasionId;
    private String title;
    private BigDecimal estimatedPrice;
    private GiftStatus status;
    private String notes;

    public GiftIdea(final UUID id, final UUID personId, final UUID occasionId, final String title,
                    final BigDecimal estimatedPrice, final GiftStatus status, final String notes) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.personId = Objects.requireNonNull(personId, "personId must not be null");
        this.occasionId = occasionId;
        setTitle(title);
        setEstimatedPrice(estimatedPrice);
        setStatus(status == null ? GiftStatus.PLANNED : status);
        setNotes(notes);
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public UUID getOccasionId() {
        return occasionId;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public GiftStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setTitle(final String title) {
        final String normalized = title == null ? "" : title.trim();
        if (normalized.isBlank()) {
            throw new DomainException("title must not be blank");
        }
        if (normalized.length() > 120) {
            throw new DomainException("title must be at most 120 characters");
        }
        this.title = normalized;
    }

    public void setEstimatedPrice(final BigDecimal estimatedPrice) {
        final BigDecimal value = Objects.requireNonNull(estimatedPrice, "estimatedPrice must not be null");
        if (value.signum() < 0) {
            throw new DomainException("estimatedPrice must not be negative");
        }
        if (value.scale() > 2) {
            throw new DomainException("estimatedPrice must have at most two decimal digits");
        }
        this.estimatedPrice = value;
    }

    public void setStatus(final GiftStatus status) {
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public void setNotes(final String notes) {
        final String normalized = notes == null ? "" : notes.trim();
        if (normalized.length() > 500) {
            throw new DomainException("notes must be at most 500 characters");
        }
        this.notes = normalized;
    }

    public void transitionTo(final GiftStatus nextStatus) {
        Objects.requireNonNull(nextStatus, "nextStatus must not be null");
        if (status == nextStatus) {
            return;
        }
        if (!status.canTransitionTo(nextStatus)) {
            throw new DomainException("invalid gift status transition from " + status + " to " + nextStatus);
        }
        this.status = nextStatus;
    }
}

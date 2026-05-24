package com.giftkeeper.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable audit entry used to document important gift lifecycle transitions.
 */
public record GiftHistoryEntry(UUID giftId, GiftStatus fromStatus, GiftStatus toStatus, Instant occurredAt) {
    public GiftHistoryEntry {
        Objects.requireNonNull(giftId, "giftId must not be null");
        Objects.requireNonNull(toStatus, "toStatus must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}

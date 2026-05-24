package com.giftkeeper.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a spending limit for one person. This class is intentionally part
 * of the domain layer so budget rules can be tested without UI, database or
 * framework dependencies.
 */
public class Budget {
    private final UUID personId;
    private final BigDecimal limit;

    public Budget(final UUID personId, final BigDecimal limit) {
        this.personId = Objects.requireNonNull(personId, "personId must not be null");
        this.limit = normalize(limit, "limit");
        if (this.limit.signum() < 0) {
            throw new DomainException("budget limit must not be negative");
        }
    }

    public UUID getPersonId() {
        return personId;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public boolean isExceededBy(final BigDecimal total) {
        return normalize(total, "total").compareTo(limit) > 0;
    }

    public BigDecimal remainingAfter(final BigDecimal total) {
        return limit.subtract(normalize(total, "total")).setScale(2, RoundingMode.UNNECESSARY);
    }

    private static BigDecimal normalize(final BigDecimal value, final String fieldName) {
        final BigDecimal checked = Objects.requireNonNull(value, fieldName + " must not be null");
        if (checked.scale() > 2) {
            throw new DomainException(fieldName + " must have at most two decimal digits");
        }
        return checked.setScale(2, RoundingMode.UNNECESSARY);
    }
}

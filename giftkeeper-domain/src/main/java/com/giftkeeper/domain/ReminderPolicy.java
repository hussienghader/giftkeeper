package com.giftkeeper.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Defines when the application should warn the user before an occasion.
 */
public class ReminderPolicy {
    private final int daysBeforeOccasion;

    public ReminderPolicy(final int daysBeforeOccasion) {
        if (daysBeforeOccasion < 0) {
            throw new DomainException("daysBeforeOccasion must not be negative");
        }
        if (daysBeforeOccasion > 365) {
            throw new DomainException("daysBeforeOccasion must be at most 365");
        }
        this.daysBeforeOccasion = daysBeforeOccasion;
    }

    public int getDaysBeforeOccasion() {
        return daysBeforeOccasion;
    }

    public LocalDate reminderDateFor(final Occasion occasion) {
        Objects.requireNonNull(occasion, "occasion must not be null");
        return occasion.getDate().minusDays(daysBeforeOccasion);
    }

    public boolean shouldRemindOn(final Occasion occasion, final LocalDate today) {
        Objects.requireNonNull(today, "today must not be null");
        return !today.isBefore(reminderDateFor(occasion)) && !today.isAfter(occasion.getDate());
    }
}

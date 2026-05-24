package com.giftkeeper.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Pure domain service: filters upcoming occasions according to a reminder policy.
 */
public class ReminderEngine {
    private final ReminderPolicy policy;

    public ReminderEngine(final ReminderPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy must not be null");
    }

    public List<Occasion> remindersDue(final List<Occasion> occasions, final LocalDate today) {
        Objects.requireNonNull(occasions, "occasions must not be null");
        return occasions.stream()
            .filter(occasion -> policy.shouldRemindOn(occasion, today))
            .toList();
    }
}

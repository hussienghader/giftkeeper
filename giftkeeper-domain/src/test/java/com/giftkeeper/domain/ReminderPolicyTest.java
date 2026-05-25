package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S5778")
class ReminderPolicyTest {
	@Test
	void shouldCreateValidReminderPolicy() {
		final ReminderPolicy policy = new ReminderPolicy(7);
		assertThat(policy.getDaysBeforeOccasion()).isEqualTo(7);
	}

	@Test
	void shouldAcceptZeroDays() {
		assertThatCode(() -> new ReminderPolicy(0)).doesNotThrowAnyException();
	}

	@Test
	void shouldAcceptExactMaxDays() {
		assertThatCode(() -> new ReminderPolicy(365)).doesNotThrowAnyException();
	}

	@Test
	void shouldRejectNegativeDays() {
		assertThatThrownBy(() -> new ReminderPolicy(-1))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("must not be negative");
	}

	@Test
	void shouldRejectMoreThan365Days() {
		assertThatThrownBy(() -> new ReminderPolicy(366))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("at most 365");
	}

	@Test
	void shouldCalculateReminderDate() {
		final ReminderPolicy policy = new ReminderPolicy(7);
		final Occasion occasion = new Occasion(UUID.randomUUID(), UUID.randomUUID(),
			OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		assertThat(policy.reminderDateFor(occasion)).isEqualTo(LocalDate.of(2026, 6, 3));
	}

	@Test
	void shouldRemindOnDayOfOccasion() {
		final ReminderPolicy policy = new ReminderPolicy(0);
		final Occasion occasion = new Occasion(UUID.randomUUID(), UUID.randomUUID(),
			OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		assertThat(policy.shouldRemindOn(occasion, LocalDate.of(2026, 6, 10))).isTrue();
	}

	@Test
	void shouldRemindOnFirstDayOfWindow() {
		final ReminderPolicy policy = new ReminderPolicy(7);
		final Occasion occasion = new Occasion(UUID.randomUUID(), UUID.randomUUID(),
			OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		assertThat(policy.shouldRemindOn(occasion, LocalDate.of(2026, 6, 3))).isTrue();
	}

	@Test
	void shouldNotRemindBeforeWindow() {
		final ReminderPolicy policy = new ReminderPolicy(7);
		final Occasion occasion = new Occasion(UUID.randomUUID(), UUID.randomUUID(),
			OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		assertThat(policy.shouldRemindOn(occasion, LocalDate.of(2026, 6, 2))).isFalse();
	}

	@Test
	void shouldNotRemindAfterOccasion() {
		final ReminderPolicy policy = new ReminderPolicy(7);
		final Occasion occasion = new Occasion(UUID.randomUUID(), UUID.randomUUID(),
			OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		assertThat(policy.shouldRemindOn(occasion, LocalDate.of(2026, 6, 11))).isFalse();
	}

	@Test
	void shouldRejectNullOccasion() {
		final ReminderPolicy policy = new ReminderPolicy(7);
		assertThatThrownBy(() -> policy.reminderDateFor(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("occasion must not be null");
	}

	@Test
	void shouldRejectNullToday() {
		final ReminderPolicy policy = new ReminderPolicy(7);
		final Occasion occasion = new Occasion(UUID.randomUUID(), UUID.randomUUID(),
			OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		assertThatThrownBy(() -> policy.shouldRemindOn(occasion, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("today must not be null");
	}
}

package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReminderEngineTest {
	@Test
	void shouldReturnOnlyOccasionsInsideReminderWindow() {
		final UUID personId = UUID.randomUUID();
		final Occasion soon = new Occasion(UUID.randomUUID(), personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		final Occasion later = new Occasion(UUID.randomUUID(), personId, OccasionType.GRADUATION, LocalDate.of(2026, 8, 1), "Graduation");
		final ReminderEngine engine = new ReminderEngine(new ReminderPolicy(7));
		assertThat(engine.remindersDue(List.of(soon, later), LocalDate.of(2026, 6, 5))).containsExactly(soon);
	}

	@Test
	void shouldReturnEmptyWhenNoOccasionsDue() {
		final ReminderEngine engine = new ReminderEngine(new ReminderPolicy(7));
		assertThat(engine.remindersDue(List.of(), LocalDate.of(2026, 6, 5))).isEmpty();
	}

	@Test
	void shouldIncludeOccasionOnExactReminderStartDay() {
		final UUID personId = UUID.randomUUID();
		final Occasion occasion = new Occasion(UUID.randomUUID(), personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		final ReminderEngine engine = new ReminderEngine(new ReminderPolicy(7));
		assertThat(engine.remindersDue(List.of(occasion), LocalDate.of(2026, 6, 3))).containsExactly(occasion);
	}

	@Test
	void shouldExcludeOccasionOneDayBeforeWindow() {
		final UUID personId = UUID.randomUUID();
		final Occasion occasion = new Occasion(UUID.randomUUID(), personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		final ReminderEngine engine = new ReminderEngine(new ReminderPolicy(7));
		assertThat(engine.remindersDue(List.of(occasion), LocalDate.of(2026, 6, 2))).isEmpty();
	}

	@Test
	void shouldIncludeOccasionOnItsExactDate() {
		final UUID personId = UUID.randomUUID();
		final Occasion occasion = new Occasion(UUID.randomUUID(), personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		final ReminderEngine engine = new ReminderEngine(new ReminderPolicy(7));
		assertThat(engine.remindersDue(List.of(occasion), LocalDate.of(2026, 6, 10))).containsExactly(occasion);
	}

	@Test
	void shouldExcludeOccasionOneDayAfterDate() {
		final UUID personId = UUID.randomUUID();
		final Occasion occasion = new Occasion(UUID.randomUUID(), personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 10), "Birthday");
		final ReminderEngine engine = new ReminderEngine(new ReminderPolicy(7));
		assertThat(engine.remindersDue(List.of(occasion), LocalDate.of(2026, 6, 11))).isEmpty();
	}

	@Test
	void shouldRejectNegativeReminderWindow() {
		assertThatThrownBy(() -> new ReminderPolicy(-1)).isInstanceOf(DomainException.class);
	}

	@Test
	void shouldRejectNullOccasionsList() {
		final ReminderEngine engine = new ReminderEngine(new ReminderPolicy(7));
		assertThatThrownBy(() -> engine.remindersDue(null, LocalDate.of(2026, 6, 5)))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("occasions must not be null");
	}
}

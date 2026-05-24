package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OccasionTest {
	private static final UUID OCCASION_ID = UUID.fromString("55555555-5555-5555-5555-555555555555");
	private static final UUID PERSON_ID = UUID.fromString("66666666-6666-6666-6666-666666666666");

	@Test
	void shouldCreateValidOccasionAndNormalizeDescription() {
		final Occasion occasion = new Occasion(OCCASION_ID, PERSON_ID, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "  Birthday party  ");
		assertThat(occasion.getId()).isEqualTo(OCCASION_ID);
		assertThat(occasion.getPersonId()).isEqualTo(PERSON_ID);
		assertThat(occasion.getType()).isEqualTo(OccasionType.BIRTHDAY);
		assertThat(occasion.getDate()).isEqualTo(LocalDate.of(2026, 6, 8));
		assertThat(occasion.getDescription()).isEqualTo("Birthday party");
	}

	@Test
	void shouldRejectNullRequiredFields() {
		assertThatThrownBy(() -> new Occasion(null, PERSON_ID, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "desc"))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("id must not be null");
		assertThatThrownBy(() -> new Occasion(OCCASION_ID, null, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "desc"))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("personId must not be null");
		assertThatThrownBy(() -> new Occasion(OCCASION_ID, PERSON_ID, null, LocalDate.of(2026, 6, 8), "desc"))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("type must not be null");
		assertThatThrownBy(() -> new Occasion(OCCASION_ID, PERSON_ID, OccasionType.BIRTHDAY, null, "desc"))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("date must not be null");
	}

	@Test
	void shouldAllowChangingTypeDateAndDescription() {
		final Occasion occasion = new Occasion(OCCASION_ID, PERSON_ID, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");
		occasion.setType(OccasionType.ANNIVERSARY);
		occasion.setDate(LocalDate.of(2026, 7, 1));
		occasion.setDescription(null);
		assertThat(occasion.getType()).isEqualTo(OccasionType.ANNIVERSARY);
		assertThat(occasion.getDate()).isEqualTo(LocalDate.of(2026, 7, 1));
		assertThat(occasion.getDescription()).isEmpty();
	}

	@Test
	void shouldRejectTooLongDescription() {
		final Occasion occasion = new Occasion(OCCASION_ID, PERSON_ID, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");
		assertThatThrownBy(() -> occasion.setDescription("a".repeat(256)))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("description must be at most 255 characters");
	}

	@Test
	void shouldAcceptDescriptionAtExactMaxLength() {
		final Occasion occasion = new Occasion(OCCASION_ID, PERSON_ID, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");
		final String exactly255 = "a".repeat(255);
		assertThatCode(() -> occasion.setDescription(exactly255))
			.doesNotThrowAnyException();
		assertThat(occasion.getDescription()).isEqualTo(exactly255);
	}
}

package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PersonTest {
	private static final UUID PERSON_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
	private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-04-07T00:00:00Z"), ZoneOffset.UTC);

	@Test
	void shouldCreateValidPerson() {
		final Person person = new Person(PERSON_ID, " Alice ", LocalDate.of(2000, 1, 1));
		assertThat(person.getId()).isEqualTo(PERSON_ID);
		assertThat(person.getName()).isEqualTo("Alice");
		assertThat(person.getBirthDate()).isEqualTo(LocalDate.of(2000, 1, 1));
	}

	@Test
	void shouldRejectNullId() {
		assertThatThrownBy(() -> new Person(null, "Alice", LocalDate.of(2000, 1, 1)))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("id must not be null");
	}

	@Test
	void shouldRejectBlankNameOnCreation() {
		assertThatThrownBy(() -> new Person(PERSON_ID, "   ", LocalDate.of(2000, 1, 1)))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("name must not be blank");
	}

	@Test
	void shouldRejectTooLongName() {
		final String tooLong = "a".repeat(101);
		assertThatThrownBy(() -> new Person(PERSON_ID, tooLong, LocalDate.of(2000, 1, 1)))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("at most 100 characters");
	}

	@Test
	void shouldAcceptNameAtExactMaxLength() {
		final String exactly100 = "a".repeat(100);
		assertThatCode(() -> new Person(PERSON_ID, exactly100, LocalDate.of(2000, 1, 1)))
			.doesNotThrowAnyException();
	}

	@Test
	void shouldRejectNullNameWhenUpdating() {
		final Person person = new Person(PERSON_ID, "Alice", LocalDate.of(2000, 1, 1));
		assertThatThrownBy(() -> person.setName(null))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("name must not be blank");
	}

	@Test
	void shouldRejectBlankNameWhenUpdating() {
		final Person person = new Person(PERSON_ID, "Alice", LocalDate.of(2000, 1, 1));
		assertThatThrownBy(() -> person.setName("   "))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("name must not be blank");
	}

	@Test
	void shouldAllowChangingNameAndBirthDate() {
		final Person person = new Person(PERSON_ID, "Alice", LocalDate.of(2000, 1, 1));
		person.setName(" Bob ");
		person.setBirthDate(LocalDate.of(1999, 12, 31));
		assertThat(person.getName()).isEqualTo("Bob");
		assertThat(person.getBirthDate()).isEqualTo(LocalDate.of(1999, 12, 31));
	}

	@Test
	void shouldRejectNullBirthDate() {
		final Person person = new Person(PERSON_ID, "Alice", LocalDate.of(2000, 1, 1));
		assertThatThrownBy(() -> person.setBirthDate(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("birthDate must not be null");
	}

	@Test
	void shouldRejectFutureBirthDateUsingClockAwareConstructor() {
		assertThatThrownBy(() -> new Person(PERSON_ID, "Alice", LocalDate.of(2026, 4, 8), FIXED_CLOCK))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("must not be in the future");
	}

	@Test
	void shouldAcceptBirthDateEqualToToday() {
		final LocalDate today = LocalDate.now(FIXED_CLOCK);
		assertThatCode(() -> new Person(PERSON_ID, "Alice", today, FIXED_CLOCK))
			.doesNotThrowAnyException();
	}

	@Test
	void shouldRejectFutureBirthDateUsingClockAwareSetter() {
		final Person person = new Person(PERSON_ID, "Alice", LocalDate.of(2000, 1, 1), FIXED_CLOCK);
		assertThatThrownBy(() -> person.setBirthDate(LocalDate.of(2026, 4, 8), FIXED_CLOCK))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("must not be in the future");
	}
}

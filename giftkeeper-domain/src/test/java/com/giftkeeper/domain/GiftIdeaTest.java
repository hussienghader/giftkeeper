package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S5778")
class GiftIdeaTest {
	private static final UUID GIFT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
	private static final UUID PERSON_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
	private static final UUID OCCASION_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

	@Test
	void shouldCreateValidGiftIdeaAndNormalizeFields() {
		final GiftIdea giftIdea = new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, " Book ", new BigDecimal("19.99"), null, "  Nice  ");
		assertThat(giftIdea.getId()).isEqualTo(GIFT_ID);
		assertThat(giftIdea.getPersonId()).isEqualTo(PERSON_ID);
		assertThat(giftIdea.getOccasionId()).isEqualTo(OCCASION_ID);
		assertThat(giftIdea.getTitle()).isEqualTo("Book");
		assertThat(giftIdea.getEstimatedPrice()).isEqualByComparingTo("19.99");
		assertThat(giftIdea.getStatus()).isEqualTo(GiftStatus.PLANNED);
		assertThat(giftIdea.getNotes()).isEqualTo("Nice");
	}

	@Test
	void shouldRejectNullIdentifiers() {
		assertThatThrownBy(() -> new GiftIdea(null, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("1.00"), GiftStatus.PLANNED, ""))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("id must not be null");
		assertThatThrownBy(() -> new GiftIdea(GIFT_ID, null, OCCASION_ID, "Book", new BigDecimal("1.00"), GiftStatus.PLANNED, ""))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("personId must not be null");
	}

	@Test
	void shouldRejectBlankOrTooLongTitle() {
		assertThatThrownBy(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "   ", new BigDecimal("1.00"), GiftStatus.PLANNED, ""))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("title must not be blank");
		assertThatThrownBy(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "a".repeat(121), new BigDecimal("1.00"), GiftStatus.PLANNED, ""))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("at most 120 characters");
	}

	@Test
	void shouldAcceptTitleAtExactMaxLength() {
		final String exactly120 = "a".repeat(120);
		assertThatCode(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, exactly120, new BigDecimal("1.00"), GiftStatus.PLANNED, ""))
			.doesNotThrowAnyException();
	}

	@Test
	void shouldRejectInvalidPrices() {
		assertThatThrownBy(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", null, GiftStatus.PLANNED, ""))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("estimatedPrice must not be null");
		assertThatThrownBy(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("-0.01"), GiftStatus.PLANNED, ""))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("must not be negative");
		assertThatThrownBy(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("1.999"), GiftStatus.PLANNED, ""))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("at most two decimal digits");
	}

	@Test
	void shouldAcceptPriceAtExactBoundaries() {
		assertThatCode(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("0.00"), GiftStatus.PLANNED, ""))
			.doesNotThrowAnyException();
		assertThatCode(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("9.99"), GiftStatus.PLANNED, ""))
			.doesNotThrowAnyException();
	}

	@Test
	void shouldRejectInvalidNotesLength() {
		assertThatThrownBy(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("1.00"), GiftStatus.PLANNED, "a".repeat(501)))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("notes must be at most 500 characters");
	}

	@Test
	void shouldAcceptNotesAtExactMaxLength() {
		final String exactly500 = "a".repeat(500);
		assertThatCode(() -> new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("1.00"), GiftStatus.PLANNED, exactly500))
			.doesNotThrowAnyException();
	}

	@Test
	void shouldAllowUpdatingMutableFields() {
		final GiftIdea giftIdea = new GiftIdea(GIFT_ID, PERSON_ID, null, "Book", new BigDecimal("10.00"), GiftStatus.PLANNED, "");
		giftIdea.setTitle(" Pen ");
		giftIdea.setEstimatedPrice(new BigDecimal("9.50"));
		giftIdea.setStatus(GiftStatus.BOUGHT);
		giftIdea.setNotes("  updated notes  ");
		assertThat(giftIdea.getTitle()).isEqualTo("Pen");
		assertThat(giftIdea.getEstimatedPrice()).isEqualByComparingTo("9.50");
		assertThat(giftIdea.getStatus()).isEqualTo(GiftStatus.BOUGHT);
		assertThat(giftIdea.getNotes()).isEqualTo("updated notes");
	}

	@Test
	void shouldRejectNullStatus() {
		final GiftIdea giftIdea = new GiftIdea(GIFT_ID, PERSON_ID, null, "Book", new BigDecimal("10.00"), GiftStatus.PLANNED, "");
		assertThatThrownBy(() -> giftIdea.setStatus(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("status must not be null");
	}

	@Test
	void shouldTransitionThroughValidStatesAndIgnoreSameState() {
		final GiftIdea giftIdea = new GiftIdea(GIFT_ID, PERSON_ID, null, "Book", new BigDecimal("10.00"), GiftStatus.PLANNED, "");
		giftIdea.transitionTo(GiftStatus.PLANNED);
		assertThat(giftIdea.getStatus()).isEqualTo(GiftStatus.PLANNED);
		giftIdea.transitionTo(GiftStatus.BOUGHT);
		assertThat(giftIdea.getStatus()).isEqualTo(GiftStatus.BOUGHT);
		giftIdea.transitionTo(GiftStatus.GIFTED);
		assertThat(giftIdea.getStatus()).isEqualTo(GiftStatus.GIFTED);
	}

	@Test
	void shouldRejectInvalidTransitionAndNullTargetStatus() {
		final GiftIdea giftIdea = new GiftIdea(GIFT_ID, PERSON_ID, null, "Book", new BigDecimal("10.00"), GiftStatus.BOUGHT, "");
		assertThatThrownBy(() -> giftIdea.transitionTo(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("nextStatus must not be null");

		final GiftIdea gifted = new GiftIdea(GIFT_ID, PERSON_ID, null, "Book", new BigDecimal("10.00"), GiftStatus.GIFTED, "");
		assertThatThrownBy(() -> gifted.transitionTo(GiftStatus.BOUGHT))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("invalid gift status transition");
	}

	@Test
	void shouldRejectNullAndBlankTitleWhenUpdated() {
		final GiftIdea giftIdea = new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("10.00"), GiftStatus.PLANNED, "note");

		assertThatThrownBy(() -> giftIdea.setTitle(null))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("title must not be blank");
		assertThatThrownBy(() -> giftIdea.setTitle("   "))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("title must not be blank");
	}

	@Test
	void shouldNormalizeNotesWhenNullOrBlank() {
		final GiftIdea giftIdea = new GiftIdea(GIFT_ID, PERSON_ID, OCCASION_ID, "Book", new BigDecimal("10.00"), GiftStatus.PLANNED, "note");

		giftIdea.setNotes(null);
		assertThat(giftIdea.getNotes()).isEmpty();

		giftIdea.setNotes("   ");
		assertThat(giftIdea.getNotes()).isEmpty();
	}
}

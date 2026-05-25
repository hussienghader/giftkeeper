package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S5778")
class GiftHistoryEntryTest {
	@Test
	void shouldCreateValidGiftHistoryEntry() {
		final UUID giftId = UUID.randomUUID();
		final Instant now = Instant.now();
		final GiftHistoryEntry entry = new GiftHistoryEntry(giftId, GiftStatus.PLANNED, GiftStatus.BOUGHT, now);
		assertThat(entry.giftId()).isEqualTo(giftId);
		assertThat(entry.fromStatus()).isEqualTo(GiftStatus.PLANNED);
		assertThat(entry.toStatus()).isEqualTo(GiftStatus.BOUGHT);
		assertThat(entry.occurredAt()).isEqualTo(now);
	}

	@Test
	void shouldAllowNullFromStatus() {
		final GiftHistoryEntry entry = new GiftHistoryEntry(UUID.randomUUID(), null, GiftStatus.PLANNED, Instant.now());
		assertThat(entry.fromStatus()).isNull();
	}

	@Test
	void shouldRejectNullGiftId() {
		assertThatThrownBy(() -> new GiftHistoryEntry(null, GiftStatus.PLANNED, GiftStatus.BOUGHT, Instant.now()))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("giftId must not be null");
	}

	@Test
	void shouldRejectNullToStatus() {
		assertThatThrownBy(() -> new GiftHistoryEntry(UUID.randomUUID(), GiftStatus.PLANNED, null, Instant.now()))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("toStatus must not be null");
	}

	@Test
	void shouldRejectNullOccurredAt() {
		assertThatThrownBy(() -> new GiftHistoryEntry(UUID.randomUUID(), null, GiftStatus.PLANNED, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("occurredAt must not be null");
	}

	@Test
	void shouldImplementEqualsHashCodeAndToString() {
		final UUID giftId = UUID.randomUUID();
		final Instant now = Instant.now();
		final GiftHistoryEntry a = new GiftHistoryEntry(giftId, GiftStatus.PLANNED, GiftStatus.BOUGHT, now);
		final GiftHistoryEntry b = new GiftHistoryEntry(giftId, GiftStatus.PLANNED, GiftStatus.BOUGHT, now);
		assertThat(a).isEqualTo(b);
		assertThat(a).hasSameHashCodeAs(b);
		assertThat(a.toString()).contains("BOUGHT");
	}
}

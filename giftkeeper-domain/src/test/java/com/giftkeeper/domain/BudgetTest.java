package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S5778")
class BudgetTest {
	@Test
	void shouldCreateValidBudget() {
		final UUID personId = UUID.randomUUID();
		final Budget budget = new Budget(personId, new BigDecimal("100.00"));
		assertThat(budget.getPersonId()).isEqualTo(personId);
		assertThat(budget.getLimit()).isEqualByComparingTo("100.00");
	}

	@Test
	void shouldAcceptZeroLimit() {
		assertThatCode(() -> new Budget(UUID.randomUUID(), new BigDecimal("0.00")))
			.doesNotThrowAnyException();
	}

	@Test
	void shouldRejectNegativeLimit() {
		assertThatThrownBy(() -> new Budget(UUID.randomUUID(), new BigDecimal("-0.01")))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("must not be negative");
	}

	@Test
	void shouldRejectNullPersonId() {
		assertThatThrownBy(() -> new Budget(null, new BigDecimal("10.00")))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("personId must not be null");
	}

	@Test
	void shouldRejectNullLimit() {
		assertThatThrownBy(() -> new Budget(UUID.randomUUID(), null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void shouldRejectLimitWithInvalidScale() {
		assertThatThrownBy(() -> new Budget(UUID.randomUUID(), new BigDecimal("10.999")))
			.isInstanceOf(DomainException.class)
			.hasMessageContaining("two decimal");
	}

	@Test
	void shouldDetectExceededBudget() {
		final Budget budget = new Budget(UUID.randomUUID(), new BigDecimal("20.00"));
		assertThat(budget.isExceededBy(new BigDecimal("20.01"))).isTrue();
	}

	@Test
	void shouldNotExceedWhenTotalExactlyEqualsLimit() {
		final Budget budget = new Budget(UUID.randomUUID(), new BigDecimal("20.00"));
		assertThat(budget.isExceededBy(new BigDecimal("20.00"))).isFalse();
	}

	@Test
	void shouldNotExceedWhenTotalIsBelowLimit() {
		final Budget budget = new Budget(UUID.randomUUID(), new BigDecimal("20.00"));
		assertThat(budget.isExceededBy(new BigDecimal("19.99"))).isFalse();
	}

	@Test
	void shouldCalculateRemainingAmount() {
		final Budget budget = new Budget(UUID.randomUUID(), new BigDecimal("100.00"));
		assertThat(budget.remainingAfter(new BigDecimal("35.50"))).isEqualByComparingTo("64.50");
	}

	@Test
	void shouldReturnZeroRemainingWhenTotalEqualsLimit() {
		final Budget budget = new Budget(UUID.randomUUID(), new BigDecimal("100.00"));
		assertThat(budget.remainingAfter(new BigDecimal("100.00"))).isEqualByComparingTo("0.00");
	}
}

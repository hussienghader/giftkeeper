package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GiftStatusTest {
    @Test
    void shouldAllowExpectedTransitions() {
        assertThat(GiftStatus.PLANNED.canTransitionTo(GiftStatus.BOUGHT)).isTrue();
        assertThat(GiftStatus.PLANNED.canTransitionTo(GiftStatus.GIFTED)).isTrue();
        assertThat(GiftStatus.BOUGHT.canTransitionTo(GiftStatus.GIFTED)).isTrue();
    }

    @Test
    void shouldRejectUnexpectedTransitions() {
        assertThat(GiftStatus.PLANNED.canTransitionTo(GiftStatus.PLANNED)).isFalse();
        assertThat(GiftStatus.BOUGHT.canTransitionTo(GiftStatus.PLANNED)).isFalse();
        assertThat(GiftStatus.BOUGHT.canTransitionTo(GiftStatus.BOUGHT)).isFalse();
        assertThat(GiftStatus.GIFTED.canTransitionTo(GiftStatus.PLANNED)).isFalse();
        assertThat(GiftStatus.GIFTED.canTransitionTo(GiftStatus.BOUGHT)).isFalse();
        assertThat(GiftStatus.GIFTED.canTransitionTo(GiftStatus.GIFTED)).isFalse();
    }
}

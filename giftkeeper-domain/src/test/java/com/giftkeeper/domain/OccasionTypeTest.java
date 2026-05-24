package com.giftkeeper.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class OccasionTypeTest {
    @Test
    void shouldExposeAllExpectedEnumValues() {
        assertThat(EnumSet.allOf(OccasionType.class))
            .containsExactly(OccasionType.BIRTHDAY, OccasionType.ANNIVERSARY, OccasionType.GRADUATION, OccasionType.HOLIDAY, OccasionType.OTHER);
        assertThat(OccasionType.valueOf("HOLIDAY")).isEqualTo(OccasionType.HOLIDAY);
    }
}

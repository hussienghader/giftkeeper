package com.giftkeeper.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.giftkeeper.domain.OccasionType;
import java.util.List;
import org.junit.jupiter.api.Test;

class RuleBasedGiftSuggestionCatalogTest {
    private final RuleBasedGiftSuggestionCatalog catalog = new RuleBasedGiftSuggestionCatalog();

    @Test
    void returnsBirthdaySuggestions() {
        assertThat(catalog.suggestionsFor(OccasionType.BIRTHDAY))
            .containsExactly("Personalized notebook", "Smart mug", "Experience voucher");
    }

    @Test
    void returnsAnniversarySuggestions() {
        assertThat(catalog.suggestionsFor(OccasionType.ANNIVERSARY))
            .containsExactly("Photo album", "Dinner voucher", "Custom frame");
    }

    @Test
    void returnsGraduationSuggestions() {
        assertThat(catalog.suggestionsFor(OccasionType.GRADUATION))
            .containsExactly("Professional backpack", "Book voucher", "Desk organizer");
    }

    @Test
    void returnsHolidaySuggestions() {
        assertThat(catalog.suggestionsFor(OccasionType.HOLIDAY))
            .containsExactly("Warm scarf", "Tea set", "Travel accessory");
    }

    @Test
    void returnsOtherSuggestions() {
        assertThat(catalog.suggestionsFor(OccasionType.OTHER))
            .containsExactly("Gift card", "Flowers", "Handwritten card");
    }

    @Test
    void fallsBackToOtherSuggestionsWhenOccasionTypeIsNull() {
        final List<String> suggestions = catalog.suggestionsFor(null);

        assertThat(suggestions).containsExactly("Gift card", "Flowers", "Handwritten card");
    }

    @Test
    void returnsImmutableSuggestionLists() {
        final List<String> suggestions = catalog.suggestionsFor(OccasionType.BIRTHDAY);

        assertThatThrownBy(() -> suggestions.add("Another gift"))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}

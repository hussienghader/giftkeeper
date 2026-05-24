package com.giftkeeper.app;

import com.giftkeeper.domain.OccasionType;
import java.util.List;
import java.util.Map;

public class RuleBasedGiftSuggestionCatalog implements GiftSuggestionCatalog {
    private static final Map<OccasionType, List<String>> SUGGESTIONS = Map.of(
        OccasionType.BIRTHDAY, List.of("Personalized notebook", "Smart mug", "Experience voucher"),
        OccasionType.ANNIVERSARY, List.of("Photo album", "Dinner voucher", "Custom frame"),
        OccasionType.GRADUATION, List.of("Professional backpack", "Book voucher", "Desk organizer"),
        OccasionType.HOLIDAY, List.of("Warm scarf", "Tea set", "Travel accessory"),
        OccasionType.OTHER, List.of("Gift card", "Flowers", "Handwritten card")
    );

    @Override
    public List<String> suggestionsFor(final OccasionType occasionType) {
        return SUGGESTIONS.getOrDefault(occasionType, SUGGESTIONS.get(OccasionType.OTHER));
    }
}

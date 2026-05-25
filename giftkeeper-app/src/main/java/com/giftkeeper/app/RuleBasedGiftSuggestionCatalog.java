package com.giftkeeper.app;

import com.giftkeeper.domain.OccasionType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RuleBasedGiftSuggestionCatalog implements GiftSuggestionCatalog {
    private static final List<String> OTHER_SUGGESTIONS = List.of("Gift card", "Flowers", "Handwritten card");
    private static final Map<OccasionType, List<String>> SUGGESTIONS = createSuggestions();

    private static Map<OccasionType, List<String>> createSuggestions() {
        final Map<OccasionType, List<String>> suggestions = new EnumMap<>(OccasionType.class);
        suggestions.put(OccasionType.BIRTHDAY, List.of("Personalized notebook", "Smart mug", "Experience voucher"));
        suggestions.put(OccasionType.ANNIVERSARY, List.of("Photo album", "Dinner voucher", "Custom frame"));
        suggestions.put(OccasionType.GRADUATION, List.of("Professional backpack", "Book voucher", "Desk organizer"));
        suggestions.put(OccasionType.HOLIDAY, List.of("Warm scarf", "Tea set", "Travel accessory"));
        suggestions.put(OccasionType.OTHER, OTHER_SUGGESTIONS);
        return Map.copyOf(suggestions);
    }

    @Override
    public List<String> suggestionsFor(final OccasionType occasionType) {
        if (occasionType == null) {
            return OTHER_SUGGESTIONS;
        }
        return SUGGESTIONS.getOrDefault(occasionType, OTHER_SUGGESTIONS);
    }
}

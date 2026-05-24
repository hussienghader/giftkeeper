package com.giftkeeper.app;

import com.giftkeeper.domain.OccasionType;
import java.util.List;

/**
 * Port used by GiftIdeasGenerator. It is deliberately small so it can be mocked
 * in unit tests and replaced by another data source later.
 */
public interface GiftSuggestionCatalog {
    List<String> suggestionsFor(OccasionType occasionType);
}

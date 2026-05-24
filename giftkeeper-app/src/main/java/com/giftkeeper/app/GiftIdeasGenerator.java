package com.giftkeeper.app;

import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import com.google.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * Application service that generates simple personalized gift ideas.
 */
public class GiftIdeasGenerator {
    private final GiftSuggestionCatalog catalog;

    @Inject
    public GiftIdeasGenerator(final GiftSuggestionCatalog catalog) {
        this.catalog = Objects.requireNonNull(catalog, "catalog must not be null");
    }

    public List<String> generateFor(final Person person, final OccasionType occasionType) {
        Objects.requireNonNull(person, "person must not be null");
        Objects.requireNonNull(occasionType, "occasionType must not be null");
        return catalog.suggestionsFor(occasionType).stream()
            .map(suggestion -> suggestion + " for " + person.getName())
            .toList();
    }
}

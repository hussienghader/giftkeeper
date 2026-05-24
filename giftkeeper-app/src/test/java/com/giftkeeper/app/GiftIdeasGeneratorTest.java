package com.giftkeeper.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GiftIdeasGeneratorTest {
    @Mock
    private GiftSuggestionCatalog catalog;

    @InjectMocks
    private GiftIdeasGenerator generator;

    @Test
    void generatesPersonalizedSuggestionsUsingMockedCatalog() {
        Person person = new Person(UUID.randomUUID(), "Maya", LocalDate.of(1999, 1, 1));
        when(catalog.suggestionsFor(OccasionType.BIRTHDAY)).thenReturn(List.of("Book", "Headphones"));

        assertThat(generator.generateFor(person, OccasionType.BIRTHDAY))
            .containsExactly("Book for Maya", "Headphones for Maya");
    }
}

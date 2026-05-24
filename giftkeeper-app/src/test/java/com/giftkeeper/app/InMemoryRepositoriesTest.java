package com.giftkeeper.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.Occasion;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InMemoryRepositoriesTest {
    @Test
    void shouldSaveFindListAndDeletePeople() {
        final InMemoryPersonRepository repository = new InMemoryPersonRepository();
        final Person alice = new Person(UUID.randomUUID(), "Alice", LocalDate.of(2000, 1, 1));
        final Person bob = new Person(UUID.randomUUID(), "Bob", LocalDate.of(1999, 1, 1));

        repository.save(alice);
        repository.save(bob);

        assertThat(repository.findById(alice.getId())).contains(alice);
        assertThat(repository.findAll()).containsExactlyInAnyOrder(alice, bob);

        repository.deleteById(alice.getId());
        assertThat(repository.findById(alice.getId())).isEmpty();
        repository.deleteById(UUID.randomUUID());
        assertThat(repository.findAll()).containsExactly(bob);
    }

    @Test
    void shouldSaveFindFilterSortAndDeleteOccasions() {
        final InMemoryOccasionRepository repository = new InMemoryOccasionRepository();
        final UUID personId = UUID.randomUUID();
        final Occasion later = new Occasion(UUID.randomUUID(), personId, OccasionType.HOLIDAY, LocalDate.of(2026, 12, 25), "Holiday");
        final Occasion earlier = new Occasion(UUID.randomUUID(), personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");
        final Occasion other = new Occasion(UUID.randomUUID(), UUID.randomUUID(), OccasionType.OTHER, LocalDate.of(2026, 1, 1), "Other");

        repository.save(later);
        repository.save(earlier);
        repository.save(other);

        assertThat(repository.findById(earlier.getId())).contains(earlier);
        assertThat(repository.findByPersonId(personId)).containsExactly(earlier, later);
        assertThat(repository.findAll()).containsExactlyInAnyOrder(later, earlier, other);

        repository.deleteById(later.getId());
        assertThat(repository.findById(later.getId())).isEmpty();
        repository.deleteById(UUID.randomUUID());
        assertThat(repository.findAll()).containsExactlyInAnyOrder(earlier, other);
    }

    @Test
    void shouldSaveFindFilterSortAndDeleteGiftIdeas() {
        final InMemoryGiftIdeaRepository repository = new InMemoryGiftIdeaRepository();
        final UUID personId = UUID.randomUUID();
        final GiftIdea zeta = new GiftIdea(UUID.randomUUID(), personId, null, "Zeta", new BigDecimal("10.00"), GiftStatus.PLANNED, "");
        final GiftIdea alpha = new GiftIdea(UUID.randomUUID(), personId, null, "Alpha", new BigDecimal("15.00"), GiftStatus.BOUGHT, "");
        final GiftIdea other = new GiftIdea(UUID.randomUUID(), UUID.randomUUID(), null, "Other", new BigDecimal("20.00"), GiftStatus.GIFTED, "");

        repository.save(zeta);
        repository.save(alpha);
        repository.save(other);

        assertThat(repository.findById(alpha.getId())).contains(alpha);
        assertThat(repository.findByPersonId(personId)).containsExactly(alpha, zeta);
        assertThat(repository.findAll()).containsExactlyInAnyOrder(zeta, alpha, other);

        repository.deleteById(zeta.getId());
        assertThat(repository.findById(zeta.getId())).isEmpty();
        repository.deleteById(UUID.randomUUID());
        assertThat(repository.findAll()).containsExactlyInAnyOrder(alpha, other);
    }
}

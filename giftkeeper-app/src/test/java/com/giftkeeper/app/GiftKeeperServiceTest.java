package com.giftkeeper.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.Occasion;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import com.giftkeeper.persistence.api.GiftIdeaRepository;
import com.giftkeeper.persistence.api.OccasionRepository;
import com.giftkeeper.persistence.api.PersonRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("java:S5778")
class GiftKeeperServiceTest {
    @Mock private PersonRepository personRepository;
    @Mock private OccasionRepository occasionRepository;
    @Mock private GiftIdeaRepository giftIdeaRepository;

    private GiftKeeperService service;

    @BeforeEach
    void setUp() {
        service = new GiftKeeperService(personRepository, occasionRepository, giftIdeaRepository);
    }

    @Test
    void shouldCreatePerson() {
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));
        final Person person = service.createPerson("Alice", LocalDate.of(2000, 1, 1));
        assertThat(person.getName()).isEqualTo("Alice");
        verify(personRepository).save(person);
    }

    @Test
    void shouldCreateOccasionForExistingPerson() {
        final UUID personId = UUID.randomUUID();
        when(personRepository.findById(personId)).thenReturn(Optional.of(new Person(personId, "Alice", LocalDate.of(2000, 1, 1))));
        when(occasionRepository.save(any(Occasion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Occasion occasion = service.createOccasion(personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");

        assertThat(occasion.getPersonId()).isEqualTo(personId);
        assertThat(occasion.getType()).isEqualTo(OccasionType.BIRTHDAY);
        verify(occasionRepository).save(occasion);
    }

    @Test
    void shouldRejectCreatingOccasionForMissingPerson() {
        final UUID personId = UUID.randomUUID();
        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createOccasion(personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("person not found");
    }

    @Test
    void shouldCreateGiftIdeaWithoutOccasion() {
        final UUID personId = UUID.randomUUID();
        when(personRepository.findById(personId)).thenReturn(Optional.of(new Person(personId, "Alice", LocalDate.of(2000, 1, 1))));
        when(giftIdeaRepository.save(any(GiftIdea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final GiftIdea giftIdea = service.createGiftIdea(personId, null, "Book", new BigDecimal("20.00"), "Nice");

        assertThat(giftIdea.getPersonId()).isEqualTo(personId);
        assertThat(giftIdea.getOccasionId()).isNull();
        assertThat(giftIdea.getStatus()).isEqualTo(GiftStatus.PLANNED);
        verify(giftIdeaRepository).save(giftIdea);
    }

    @Test
    void shouldCreateGiftIdeaForExistingOccasionOwnedByPerson() {
        final UUID personId = UUID.randomUUID();
        final UUID occasionId = UUID.randomUUID();
        when(personRepository.findById(personId)).thenReturn(Optional.of(new Person(personId, "Alice", LocalDate.of(2000, 1, 1))));
        when(occasionRepository.findById(occasionId)).thenReturn(Optional.of(new Occasion(occasionId, personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 1, 1), "Birthday")));
        when(giftIdeaRepository.save(any(GiftIdea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final GiftIdea giftIdea = service.createGiftIdea(personId, occasionId, "Book", new BigDecimal("20.00"), "Nice");

        assertThat(giftIdea.getOccasionId()).isEqualTo(occasionId);
        verify(giftIdeaRepository).save(giftIdea);
    }

    @Test
    void shouldRejectGiftForMissingPerson() {
        final UUID personId = UUID.randomUUID();
        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createGiftIdea(personId, null, "Book", new BigDecimal("20.00"), "Nice"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("person not found");
    }

    @Test
    void shouldRejectGiftForMissingOccasion() {
        final UUID personId = UUID.randomUUID();
        final UUID occasionId = UUID.randomUUID();
        when(personRepository.findById(personId)).thenReturn(Optional.of(new Person(personId, "Alice", LocalDate.of(2000, 1, 1))));
        when(occasionRepository.findById(occasionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createGiftIdea(personId, occasionId, "Book", new BigDecimal("20.00"), "Nice"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("occasion not found");
    }

    @Test
    void shouldRejectGiftWithOccasionOwnedByAnotherPerson() {
        final UUID personId = UUID.randomUUID();
        final UUID occasionId = UUID.randomUUID();
        when(personRepository.findById(personId)).thenReturn(Optional.of(new Person(personId, "Alice", LocalDate.of(2000, 1, 1))));
        when(occasionRepository.findById(occasionId)).thenReturn(Optional.of(new Occasion(occasionId, UUID.randomUUID(), OccasionType.BIRTHDAY, LocalDate.of(2026, 1, 1), "Birthday")));

        assertThatThrownBy(() -> service.createGiftIdea(personId, occasionId, "Book", new BigDecimal("20.00"), "Nice"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("does not belong");
    }

    @Test
    void shouldChangeGiftStatus() {
        final UUID giftId = UUID.randomUUID();
        final GiftIdea giftIdea = new GiftIdea(giftId, UUID.randomUUID(), null, "Book", new BigDecimal("10.00"), GiftStatus.PLANNED, "");
        when(giftIdeaRepository.findById(giftId)).thenReturn(Optional.of(giftIdea));
        when(giftIdeaRepository.save(giftIdea)).thenReturn(giftIdea);

        final GiftIdea updated = service.changeGiftStatus(giftId, GiftStatus.BOUGHT);

        assertThat(updated.getStatus()).isEqualTo(GiftStatus.BOUGHT);
        verify(giftIdeaRepository).save(giftIdea);
    }

    @Test
    void shouldRejectChangingStatusOfMissingGift() {
        final UUID giftId = UUID.randomUUID();
        when(giftIdeaRepository.findById(giftId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.changeGiftStatus(giftId, GiftStatus.BOUGHT))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("gift not found");
    }

    @Test
    void shouldDelegateListOperations() {
        final UUID personId = UUID.randomUUID();
        final List<Person> people = List.of(new Person(personId, "Alice", LocalDate.of(2000, 1, 1)));
        final List<Occasion> occasions = List.of(new Occasion(UUID.randomUUID(), personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday"));
        final List<GiftIdea> gifts = List.of(new GiftIdea(UUID.randomUUID(), personId, null, "Book", new BigDecimal("10.00"), GiftStatus.PLANNED, ""));
        when(personRepository.findAll()).thenReturn(people);
        when(occasionRepository.findAll()).thenReturn(occasions);
        when(giftIdeaRepository.findAll()).thenReturn(gifts);
        when(occasionRepository.findByPersonId(personId)).thenReturn(occasions);
        when(giftIdeaRepository.findByPersonId(personId)).thenReturn(gifts);

        assertThat(service.listPeople()).containsExactlyElementsOf(people);
        assertThat(service.listOccasions()).containsExactlyElementsOf(occasions);
        assertThat(service.listGifts()).containsExactlyElementsOf(gifts);
        assertThat(service.listOccasionsForPerson(personId)).containsExactlyElementsOf(occasions);
        assertThat(service.listGiftsForPerson(personId)).containsExactlyElementsOf(gifts);
    }
}

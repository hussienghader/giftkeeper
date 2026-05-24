package com.giftkeeper.app;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.Occasion;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import com.giftkeeper.persistence.api.GiftIdeaRepository;
import com.giftkeeper.persistence.api.OccasionRepository;
import com.giftkeeper.persistence.api.PersonRepository;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class GiftKeeperService implements GiftKeeperUseCases {
    private final PersonRepository personRepository;
    private final OccasionRepository occasionRepository;
    private final GiftIdeaRepository giftIdeaRepository;

    @Inject
    public GiftKeeperService(final PersonRepository personRepository,
                             final OccasionRepository occasionRepository,
                             final GiftIdeaRepository giftIdeaRepository) {
        this.personRepository = personRepository;
        this.occasionRepository = occasionRepository;
        this.giftIdeaRepository = giftIdeaRepository;
    }

    @Override
    public Person createPerson(final String name, final LocalDate birthDate) {
        final Person person = new Person(UUID.randomUUID(), name, birthDate);
        return personRepository.save(person);
    }

    @Override
    public Occasion createOccasion(final UUID personId, final OccasionType type, final LocalDate date, final String description) {
        ensurePersonExists(personId);
        final Occasion occasion = new Occasion(UUID.randomUUID(), personId, type, date, description);
        return occasionRepository.save(occasion);
    }

    @Override
    public GiftIdea createGiftIdea(final UUID personId, final UUID occasionId, final String title,
                                   final BigDecimal estimatedPrice, final String notes) {
        ensurePersonExists(personId);
        if (occasionId != null) {
            final Occasion occasion = occasionRepository.findById(occasionId)
                .orElseThrow(() -> new IllegalArgumentException("occasion not found: " + occasionId));
            if (!occasion.getPersonId().equals(personId)) {
                throw new IllegalArgumentException("occasion does not belong to person: " + personId);
            }
        }
        final GiftIdea giftIdea = new GiftIdea(UUID.randomUUID(), personId, occasionId, title, estimatedPrice, GiftStatus.PLANNED, notes);
        return giftIdeaRepository.save(giftIdea);
    }

    @Override
    public GiftIdea changeGiftStatus(final UUID giftId, final GiftStatus nextStatus) {
        final GiftIdea giftIdea = giftIdeaRepository.findById(giftId)
            .orElseThrow(() -> new IllegalArgumentException("gift not found: " + giftId));
        giftIdea.transitionTo(nextStatus);
        return giftIdeaRepository.save(giftIdea);
    }

    @Override
    public List<Person> listPeople() {
        return personRepository.findAll();
    }

    @Override
    public List<Occasion> listOccasions() {
        return occasionRepository.findAll();
    }

    @Override
    public List<GiftIdea> listGifts() {
        return giftIdeaRepository.findAll();
    }

    @Override
    public List<Occasion> listOccasionsForPerson(final UUID personId) {
        return occasionRepository.findByPersonId(personId);
    }

    @Override
    public List<GiftIdea> listGiftsForPerson(final UUID personId) {
        return giftIdeaRepository.findByPersonId(personId);
    }

    private void ensurePersonExists(final UUID personId) {
        personRepository.findById(personId)
            .orElseThrow(() -> new IllegalArgumentException("person not found: " + personId));
    }
}

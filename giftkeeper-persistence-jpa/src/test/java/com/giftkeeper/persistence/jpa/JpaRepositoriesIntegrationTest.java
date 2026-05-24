package com.giftkeeper.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.Occasion;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class JpaRepositoriesIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
        .withDatabaseName("giftkeeper")
        .withUsername("giftkeeper")
        .withPassword("giftkeeper");

    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @Test
    void shouldPersistLoadDeleteAndUpdateWholeGiftFlow() {
        entityManager = JpaPersistence.createEntityManager(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        final var personRepository = new JpaPersonRepository(entityManager);
        final var occasionRepository = new JpaOccasionRepository(entityManager);
        final var giftIdeaRepository = new JpaGiftIdeaRepository(entityManager);

        final Person person = personRepository.save(new Person(UUID.randomUUID(), "Alice", LocalDate.of(1998, 6, 8)));
        final Occasion occasion = occasionRepository.save(new Occasion(UUID.randomUUID(), person.getId(), OccasionType.BIRTHDAY,
            LocalDate.of(2026, 6, 8), "Birthday"));
        final GiftIdea giftIdea = giftIdeaRepository.save(new GiftIdea(UUID.randomUUID(), person.getId(), occasion.getId(),
            "Book", new BigDecimal("19.99"), GiftStatus.PLANNED, "Sci-fi"));

        assertThat(personRepository.findAll()).hasSize(1);
        assertThat(personRepository.findById(person.getId()))
            .isPresent()
            .get()
            .extracting(Person::getId, Person::getName, Person::getBirthDate)
            .containsExactly(person.getId(), person.getName(), person.getBirthDate());
        assertThat(personRepository.findById(UUID.randomUUID())).isEmpty();
        assertThat(occasionRepository.findByPersonId(person.getId())).hasSize(1);
        assertThat(occasionRepository.findById(occasion.getId()))
            .isPresent()
            .get()
            .extracting(Occasion::getId, Occasion::getPersonId, Occasion::getType, Occasion::getDate, Occasion::getDescription)
            .containsExactly(occasion.getId(), occasion.getPersonId(), occasion.getType(), occasion.getDate(), occasion.getDescription());
        assertThat(occasionRepository.findAll()).hasSize(1);
        assertThat(giftIdeaRepository.findByPersonId(person.getId())).singleElement().extracting(GiftIdea::getTitle).isEqualTo("Book");
        assertThat(giftIdeaRepository.findById(giftIdea.getId())).isPresent();
        assertThat(giftIdeaRepository.findAll()).hasSize(1);

        final Person updatedPerson = new Person(person.getId(), "Alice Updated", person.getBirthDate());
        personRepository.save(updatedPerson);
        assertThat(personRepository.findById(person.getId())).get().extracting(Person::getName).isEqualTo("Alice Updated");

        giftIdeaRepository.deleteById(giftIdea.getId());
        assertThat(giftIdeaRepository.findById(giftIdea.getId())).isEmpty();
        giftIdeaRepository.deleteById(UUID.randomUUID());
        occasionRepository.deleteById(occasion.getId());
        assertThat(occasionRepository.findById(occasion.getId())).isEmpty();
        occasionRepository.deleteById(UUID.randomUUID());
        personRepository.deleteById(person.getId());
        assertThat(personRepository.findById(person.getId())).isEmpty();
        personRepository.deleteById(UUID.randomUUID());
    }
}

package com.giftkeeper.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.Occasion;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

@SuppressWarnings("java:S5778")
class JpaRepositoryUnitTest {
    @Test
    void shouldMapEntitiesToDomainObjects() {
        final JpaPersonEntity personEntity = new JpaPersonEntity();
        personEntity.setId(UUID.randomUUID());
        personEntity.setName("Alice");
        personEntity.setBirthDate(LocalDate.of(1998, 6, 8));

        final JpaOccasionEntity occasionEntity = new JpaOccasionEntity();
        occasionEntity.setId(UUID.randomUUID());
        occasionEntity.setPerson(personEntity);
        occasionEntity.setType(OccasionType.BIRTHDAY);
        occasionEntity.setDate(LocalDate.of(2026, 6, 8));
        occasionEntity.setDescription("Birthday");

        final JpaGiftIdeaEntity giftEntity = new JpaGiftIdeaEntity();
        giftEntity.setId(UUID.randomUUID());
        giftEntity.setPerson(personEntity);
        giftEntity.setOccasion(occasionEntity);
        giftEntity.setTitle("Book");
        giftEntity.setEstimatedPrice(new BigDecimal("9.99"));
        giftEntity.setStatus(GiftStatus.BOUGHT);
        giftEntity.setNotes("Sci-fi");

        assertThat(DomainMapper.toDomain(personEntity).getName()).isEqualTo("Alice");
        assertThat(DomainMapper.toDomain(occasionEntity).getPersonId()).isEqualTo(personEntity.getId());
        assertThat(DomainMapper.toDomain(giftEntity).getOccasionId()).isEqualTo(occasionEntity.getId());

        giftEntity.setOccasion(null);
        assertThat(DomainMapper.toDomain(giftEntity).getOccasionId()).isNull();
    }

    @Test
    void shouldCreatePersistenceFactoryAndEntityManagerViaStaticPersistenceCalls() {
        final EntityManagerFactory factory = mock(EntityManagerFactory.class);
        final EntityManager entityManager = mock(EntityManager.class);
        try (MockedStatic<Persistence> persistence = mockStatic(Persistence.class)) {
            persistence.when(() -> Persistence.createEntityManagerFactory("giftkeeper-pu")).thenReturn(factory);
            persistence.when(() -> Persistence.createEntityManagerFactory(eq("giftkeeper-pu"), any(java.util.Map.class))).thenReturn(factory);
            when(factory.createEntityManager()).thenReturn(entityManager);

            assertThat(JpaPersistence.createDefaultFactory()).isSameAs(factory);
            assertThat(JpaPersistence.createFactory("jdbc:test", "user", "pass")).isSameAs(factory);
            assertThat(JpaPersistence.createEntityManager("jdbc:test", "user", "pass")).isSameAs(entityManager);
        }
    }

    @Test
    void shouldHandleJpaPersonRepositoryBranches() {
        final EntityManager em = mock(EntityManager.class);
        final EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        final JpaPersonRepository repository = new JpaPersonRepository(em);
        final Person person = new Person(UUID.randomUUID(), "Alice", LocalDate.of(1998, 6, 8));

        assertThat(repository.save(person)).isSameAs(person);
        verify(em).merge(any(JpaPersonEntity.class));
        verify(tx).commit();

        final JpaPersonEntity entity = new JpaPersonEntity();
        entity.setId(person.getId()); entity.setName(person.getName()); entity.setBirthDate(person.getBirthDate());
        when(em.find(JpaPersonEntity.class, person.getId())).thenReturn(entity);
        assertThat(repository.findById(person.getId()))
            .isPresent()
            .get()
            .extracting(Person::getId, Person::getName, Person::getBirthDate)
            .containsExactly(person.getId(), person.getName(), person.getBirthDate());
        assertThat(repository.findById(UUID.randomUUID())).isEqualTo(Optional.empty());

        @SuppressWarnings("unchecked") final TypedQuery<JpaPersonEntity> query = mock(TypedQuery.class);
        when(em.createQuery(any(String.class), eq(JpaPersonEntity.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(entity)).thenReturn(List.of());
        assertThat(repository.findAll())
            .singleElement()
            .extracting(Person::getId, Person::getName, Person::getBirthDate)
            .containsExactly(person.getId(), person.getName(), person.getBirthDate());
        assertThat(repository.findAll()).isEmpty();

        repository.deleteById(person.getId());
        verify(em).remove(entity);
        repository.deleteById(UUID.randomUUID());

        when(em.merge(any(JpaPersonEntity.class))).thenThrow(new IllegalStateException("boom"));
        when(tx.isActive()).thenReturn(true);
        assertThatThrownBy(() -> repository.save(person)).isInstanceOf(IllegalStateException.class).hasMessageContaining("boom");
        verify(tx, org.mockito.Mockito.atLeastOnce()).rollback();
    }

    @Test
    void shouldHandleJpaPersonRepositoryRollbackInactiveBranch() {
        final EntityManager em = mock(EntityManager.class);
        final EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(false);
        when(em.merge(any(JpaPersonEntity.class))).thenThrow(new IllegalStateException("boom"));
        final JpaPersonRepository repository = new JpaPersonRepository(em);
        final Person person = new Person(UUID.randomUUID(), "Alice", LocalDate.of(1998, 6, 8));

        assertThatThrownBy(() -> repository.save(person)).isInstanceOf(IllegalStateException.class);
        verify(tx, never()).rollback();
    }

    @Test
    void shouldHandleJpaOccasionRepositoryBranches() {
        final EntityManager em = mock(EntityManager.class);
        final EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        final JpaOccasionRepository repository = new JpaOccasionRepository(em);
        final UUID personId = UUID.randomUUID();
        final Occasion occasion = new Occasion(UUID.randomUUID(), personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");
        final JpaPersonEntity personEntity = new JpaPersonEntity(); personEntity.setId(personId);
        when(em.getReference(JpaPersonEntity.class, personId)).thenReturn(personEntity);

        assertThat(repository.save(occasion)).isSameAs(occasion);
        verify(em).merge(any(JpaOccasionEntity.class));

        final JpaOccasionEntity entity = new JpaOccasionEntity();
        entity.setId(occasion.getId()); entity.setPerson(personEntity); entity.setType(occasion.getType()); entity.setDate(occasion.getDate()); entity.setDescription(occasion.getDescription());
        when(em.find(JpaOccasionEntity.class, occasion.getId())).thenReturn(entity);
        assertThat(repository.findById(occasion.getId()))
            .isPresent()
            .get()
            .extracting(Occasion::getId, Occasion::getPersonId, Occasion::getType, Occasion::getDate, Occasion::getDescription)
            .containsExactly(occasion.getId(), occasion.getPersonId(), occasion.getType(), occasion.getDate(), occasion.getDescription());
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();

        @SuppressWarnings("unchecked") final TypedQuery<JpaOccasionEntity> query = mock(TypedQuery.class);
        when(em.createQuery(any(String.class), eq(JpaOccasionEntity.class))).thenReturn(query);
        when(query.setParameter("personId", personId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(entity)).thenReturn(List.of()).thenReturn(List.of(entity)).thenReturn(List.of());
        assertThat(repository.findByPersonId(personId))
            .singleElement()
            .extracting(Occasion::getId, Occasion::getPersonId, Occasion::getType, Occasion::getDate, Occasion::getDescription)
            .containsExactly(occasion.getId(), occasion.getPersonId(), occasion.getType(), occasion.getDate(), occasion.getDescription());
        assertThat(repository.findByPersonId(personId)).isEmpty();
        assertThat(repository.findAll())
            .singleElement()
            .extracting(Occasion::getId, Occasion::getPersonId, Occasion::getType, Occasion::getDate, Occasion::getDescription)
            .containsExactly(occasion.getId(), occasion.getPersonId(), occasion.getType(), occasion.getDate(), occasion.getDescription());
        assertThat(repository.findAll()).isEmpty();

        repository.deleteById(occasion.getId());
        verify(em).remove(entity);
        repository.deleteById(UUID.randomUUID());

        when(em.merge(any(JpaOccasionEntity.class))).thenThrow(new IllegalStateException("boom"));
        when(tx.isActive()).thenReturn(true);
        assertThatThrownBy(() -> repository.save(occasion)).isInstanceOf(IllegalStateException.class).hasMessageContaining("boom");
    }

    @Test
    void shouldHandleJpaOccasionRepositoryRollbackInactiveBranch() {
        final EntityManager em = mock(EntityManager.class);
        final EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(false);
        when(em.getReference(eq(JpaPersonEntity.class), any(UUID.class))).thenReturn(new JpaPersonEntity());
        when(em.merge(any(JpaOccasionEntity.class))).thenThrow(new IllegalStateException("boom"));
        final JpaOccasionRepository repository = new JpaOccasionRepository(em);
        final Occasion occasion = new Occasion(UUID.randomUUID(), UUID.randomUUID(), OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");

        assertThatThrownBy(() -> repository.save(occasion)).isInstanceOf(IllegalStateException.class);
        verify(tx, never()).rollback();
    }

    @Test
    void shouldHandleJpaGiftIdeaRepositoryBranches() {
        final EntityManager em = mock(EntityManager.class);
        final EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        final JpaGiftIdeaRepository repository = new JpaGiftIdeaRepository(em);
        final UUID personId = UUID.randomUUID();
        final UUID occasionId = UUID.randomUUID();
        final JpaPersonEntity personEntity = new JpaPersonEntity(); personEntity.setId(personId);
        final JpaOccasionEntity occasionEntity = new JpaOccasionEntity(); occasionEntity.setId(occasionId); occasionEntity.setPerson(personEntity); occasionEntity.setType(OccasionType.BIRTHDAY); occasionEntity.setDate(LocalDate.of(2026,6,8)); occasionEntity.setDescription("Birthday");
        when(em.getReference(JpaPersonEntity.class, personId)).thenReturn(personEntity);
        when(em.getReference(JpaOccasionEntity.class, occasionId)).thenReturn(occasionEntity);
        final GiftIdea gift = new GiftIdea(UUID.randomUUID(), personId, occasionId, "Book", new BigDecimal("9.99"), GiftStatus.PLANNED, "Sci-fi");

        assertThat(repository.save(gift)).isSameAs(gift);
        verify(em).merge(any(JpaGiftIdeaEntity.class));

        final JpaGiftIdeaEntity entity = new JpaGiftIdeaEntity();
        entity.setId(gift.getId()); entity.setPerson(personEntity); entity.setOccasion(occasionEntity); entity.setTitle(gift.getTitle()); entity.setEstimatedPrice(gift.getEstimatedPrice()); entity.setStatus(gift.getStatus()); entity.setNotes(gift.getNotes());
        when(em.find(JpaGiftIdeaEntity.class, gift.getId())).thenReturn(entity);
        assertThat(repository.findById(gift.getId()))
            .isPresent()
            .get()
            .extracting(GiftIdea::getId, GiftIdea::getPersonId, GiftIdea::getOccasionId, GiftIdea::getTitle, GiftIdea::getEstimatedPrice, GiftIdea::getStatus, GiftIdea::getNotes)
            .containsExactly(gift.getId(), gift.getPersonId(), gift.getOccasionId(), gift.getTitle(), gift.getEstimatedPrice(), gift.getStatus(), gift.getNotes());
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();

        @SuppressWarnings("unchecked") final TypedQuery<JpaGiftIdeaEntity> query = mock(TypedQuery.class);
        when(em.createQuery(any(String.class), eq(JpaGiftIdeaEntity.class))).thenReturn(query);
        when(query.setParameter("personId", personId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(entity)).thenReturn(List.of()).thenReturn(List.of(entity)).thenReturn(List.of());
        assertThat(repository.findByPersonId(personId))
            .singleElement()
            .extracting(GiftIdea::getId, GiftIdea::getPersonId, GiftIdea::getOccasionId, GiftIdea::getTitle, GiftIdea::getEstimatedPrice, GiftIdea::getStatus, GiftIdea::getNotes)
            .containsExactly(gift.getId(), gift.getPersonId(), gift.getOccasionId(), gift.getTitle(), gift.getEstimatedPrice(), gift.getStatus(), gift.getNotes());
        assertThat(repository.findByPersonId(personId)).isEmpty();
        assertThat(repository.findAll())
            .singleElement()
            .extracting(GiftIdea::getId, GiftIdea::getPersonId, GiftIdea::getOccasionId, GiftIdea::getTitle, GiftIdea::getEstimatedPrice, GiftIdea::getStatus, GiftIdea::getNotes)
            .containsExactly(gift.getId(), gift.getPersonId(), gift.getOccasionId(), gift.getTitle(), gift.getEstimatedPrice(), gift.getStatus(), gift.getNotes());
        assertThat(repository.findAll()).isEmpty();

        repository.deleteById(gift.getId());
        verify(em).remove(entity);
        repository.deleteById(UUID.randomUUID());

        final GiftIdea noOccasion = new GiftIdea(UUID.randomUUID(), personId, null, "Pen", new BigDecimal("1.00"), GiftStatus.PLANNED, "");
        assertThat(repository.save(noOccasion)).isSameAs(noOccasion);

        when(em.merge(any(JpaGiftIdeaEntity.class))).thenThrow(new IllegalStateException("boom"));
        when(tx.isActive()).thenReturn(true);
        assertThatThrownBy(() -> repository.save(gift)).isInstanceOf(IllegalStateException.class).hasMessageContaining("boom");
    }

    @Test
    void shouldHandleJpaGiftIdeaRepositoryRollbackInactiveBranch() {
        final EntityManager em = mock(EntityManager.class);
        final EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(false);
        final UUID personId = UUID.randomUUID();
        when(em.getReference(JpaPersonEntity.class, personId)).thenReturn(new JpaPersonEntity());
        when(em.merge(any(JpaGiftIdeaEntity.class))).thenThrow(new IllegalStateException("boom"));
        final JpaGiftIdeaRepository repository = new JpaGiftIdeaRepository(em);
        final GiftIdea gift = new GiftIdea(UUID.randomUUID(), personId, null, "Book", new BigDecimal("9.99"), GiftStatus.PLANNED, "Sci-fi");

        assertThatThrownBy(() -> repository.save(gift)).isInstanceOf(IllegalStateException.class);
        verify(tx, never()).rollback();
    }
}

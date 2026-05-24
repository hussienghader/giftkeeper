package com.giftkeeper.persistence.jpa;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.persistence.api.GiftIdeaRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JpaGiftIdeaRepository implements GiftIdeaRepository {
    private final EntityManager entityManager;

    public JpaGiftIdeaRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public GiftIdea save(final GiftIdea giftIdea) {
        final JpaGiftIdeaEntity entity = new JpaGiftIdeaEntity();
        entity.setId(giftIdea.getId());
        entity.setPerson(entityManager.getReference(JpaPersonEntity.class, giftIdea.getPersonId()));
        entity.setOccasion(giftIdea.getOccasionId() == null ? null : entityManager.getReference(JpaOccasionEntity.class, giftIdea.getOccasionId()));
        entity.setTitle(giftIdea.getTitle());
        entity.setEstimatedPrice(giftIdea.getEstimatedPrice());
        entity.setStatus(giftIdea.getStatus());
        entity.setNotes(giftIdea.getNotes());
        inTransaction(() -> entityManager.merge(entity));
        return giftIdea;
    }

    @Override
    public Optional<GiftIdea> findById(final UUID id) {
        return Optional.ofNullable(entityManager.find(JpaGiftIdeaEntity.class, id)).map(DomainMapper::toDomain);
    }

    @Override
    public List<GiftIdea> findByPersonId(final UUID personId) {
        return entityManager.createQuery("select g from JpaGiftIdeaEntity g where g.person.id = :personId order by g.title", JpaGiftIdeaEntity.class)
            .setParameter("personId", personId)
            .getResultList().stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<GiftIdea> findAll() {
        return entityManager.createQuery("select g from JpaGiftIdeaEntity g order by g.title", JpaGiftIdeaEntity.class)
            .getResultList().stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public void deleteById(final UUID id) {
        inTransaction(() -> {
            final JpaGiftIdeaEntity entity = entityManager.find(JpaGiftIdeaEntity.class, id);
            if (entity != null) {
                entityManager.remove(entity);
            }
        });
    }

    private void inTransaction(final Runnable runnable) {
        final var tx = entityManager.getTransaction();
        tx.begin();
        try {
            runnable.run();
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }
}

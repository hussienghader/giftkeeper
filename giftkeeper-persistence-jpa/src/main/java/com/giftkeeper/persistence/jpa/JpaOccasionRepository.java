package com.giftkeeper.persistence.jpa;

import com.giftkeeper.domain.Occasion;
import com.giftkeeper.persistence.api.OccasionRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JpaOccasionRepository implements OccasionRepository {
    private final EntityManager entityManager;

    public JpaOccasionRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Occasion save(final Occasion occasion) {
        final JpaOccasionEntity entity = new JpaOccasionEntity();
        entity.setId(occasion.getId());
        entity.setPerson(entityManager.getReference(JpaPersonEntity.class, occasion.getPersonId()));
        entity.setType(occasion.getType());
        entity.setDate(occasion.getDate());
        entity.setDescription(occasion.getDescription());
        inTransaction(() -> entityManager.merge(entity));
        return occasion;
    }

    @Override
    public Optional<Occasion> findById(final UUID id) {
        return Optional.ofNullable(entityManager.find(JpaOccasionEntity.class, id)).map(DomainMapper::toDomain);
    }

    @Override
    public List<Occasion> findByPersonId(final UUID personId) {
        return entityManager.createQuery("select o from JpaOccasionEntity o where o.person.id = :personId order by o.date", JpaOccasionEntity.class)
            .setParameter("personId", personId)
            .getResultList().stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<Occasion> findAll() {
        return entityManager.createQuery("select o from JpaOccasionEntity o order by o.date", JpaOccasionEntity.class)
            .getResultList().stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public void deleteById(final UUID id) {
        inTransaction(() -> {
            final JpaOccasionEntity entity = entityManager.find(JpaOccasionEntity.class, id);
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

package com.giftkeeper.persistence.jpa;

import com.giftkeeper.domain.Person;
import com.giftkeeper.persistence.api.PersonRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JpaPersonRepository implements PersonRepository {
    private final EntityManager entityManager;

    public JpaPersonRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Person save(final Person person) {
        final JpaPersonEntity entity = new JpaPersonEntity();
        entity.setId(person.getId());
        entity.setName(person.getName());
        entity.setBirthDate(person.getBirthDate());
        inTransaction(() -> entityManager.merge(entity));
        return person;
    }

    @Override
    public Optional<Person> findById(final UUID id) {
        return Optional.ofNullable(entityManager.find(JpaPersonEntity.class, id)).map(DomainMapper::toDomain);
    }

    @Override
    public List<Person> findAll() {
        return entityManager.createQuery("select p from JpaPersonEntity p order by p.name", JpaPersonEntity.class)
            .getResultList().stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public void deleteById(final UUID id) {
        inTransaction(() -> {
            final JpaPersonEntity entity = entityManager.find(JpaPersonEntity.class, id);
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

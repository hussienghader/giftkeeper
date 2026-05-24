package com.giftkeeper.app;

import com.giftkeeper.persistence.api.GiftIdeaRepository;
import com.giftkeeper.persistence.api.OccasionRepository;
import com.giftkeeper.persistence.api.PersonRepository;
import com.giftkeeper.persistence.jpa.JpaGiftIdeaRepository;
import com.giftkeeper.persistence.jpa.JpaOccasionRepository;
import com.giftkeeper.persistence.jpa.JpaPersistence;
import com.giftkeeper.persistence.jpa.JpaPersonRepository;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class GiftKeeperJpaModule extends AbstractModule {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public GiftKeeperJpaModule(final String jdbcUrl, final String username, final String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    protected void configure() {
        bind(GiftKeeperUseCases.class).to(GiftKeeperService.class);
        bind(GiftSuggestionCatalog.class).to(RuleBasedGiftSuggestionCatalog.class);
    }

    @Provides
    @Singleton
    EntityManagerFactory provideEntityManagerFactory() {
        return JpaPersistence.createFactory(jdbcUrl, username, password);
    }

    @Provides
    @Singleton
    EntityManager provideEntityManager(final EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Provides
    @Singleton
    PersonRepository providePersonRepository(final EntityManager entityManager) {
        return new JpaPersonRepository(entityManager);
    }

    @Provides
    @Singleton
    OccasionRepository provideOccasionRepository(final EntityManager entityManager) {
        return new JpaOccasionRepository(entityManager);
    }

    @Provides
    @Singleton
    GiftIdeaRepository provideGiftIdeaRepository(final EntityManager entityManager) {
        return new JpaGiftIdeaRepository(entityManager);
    }
}

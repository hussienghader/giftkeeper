package com.giftkeeper.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.giftkeeper.persistence.api.GiftIdeaRepository;
import com.giftkeeper.persistence.api.OccasionRepository;
import com.giftkeeper.persistence.api.PersonRepository;
import com.giftkeeper.persistence.jpa.JpaPersistence;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ApplicationAndModuleTest {
    @Test
    void shouldCreateInMemoryInjectorAndResolveBindings() {
        final Injector injector = GiftKeeperApplication.createInMemoryInjector();
        assertThat(injector.getInstance(GiftKeeperUseCases.class)).isInstanceOf(GiftKeeperService.class);
        assertThat(injector.getInstance(PersonRepository.class)).isInstanceOf(InMemoryPersonRepository.class);
        assertThat(injector.getInstance(OccasionRepository.class)).isInstanceOf(InMemoryOccasionRepository.class);
        assertThat(injector.getInstance(GiftIdeaRepository.class)).isInstanceOf(InMemoryGiftIdeaRepository.class);
    }

    @Test
    void shouldCreateJpaInjectorWithoutResolvingDatabaseConnections() {
        final Injector injector = GiftKeeperApplication.createJpaInjector();
        assertThat(injector.getBinding(GiftKeeperUseCases.class)).isNotNull();
    }

    @Test
    void shouldProvideJpaDependenciesThroughModuleMethods() {
        final EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        final EntityManager entityManager = mock(EntityManager.class);
        final GiftKeeperJpaModule module = new GiftKeeperJpaModule("jdbc:test", "user", "pass");

        try (MockedStatic<JpaPersistence> persistence = mockStatic(JpaPersistence.class)) {
            persistence.when(() -> JpaPersistence.createFactory("jdbc:test", "user", "pass")).thenReturn(entityManagerFactory);
            org.mockito.Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

            assertThat(module.provideEntityManagerFactory()).isSameAs(entityManagerFactory);
            assertThat(module.provideEntityManager(entityManagerFactory)).isSameAs(entityManager);
            assertThat(module.providePersonRepository(entityManager)).isNotNull().isInstanceOf(com.giftkeeper.persistence.jpa.JpaPersonRepository.class);
            assertThat(module.provideOccasionRepository(entityManager)).isNotNull().isInstanceOf(com.giftkeeper.persistence.jpa.JpaOccasionRepository.class);
            assertThat(module.provideGiftIdeaRepository(entityManager)).isNotNull().isInstanceOf(com.giftkeeper.persistence.jpa.JpaGiftIdeaRepository.class);
        }
    }

    @Test
    void shouldBindUseCasesInJpaModuleAndInMemoryModule() {
        assertThat(Guice.createInjector(new GiftKeeperInMemoryModule()).getBinding(GiftKeeperUseCases.class)).isNotNull();
        assertThat(Guice.createInjector(new GiftKeeperJpaModule("jdbc:test", "user", "pass")).getBinding(GiftKeeperUseCases.class)).isNotNull();
    }
}

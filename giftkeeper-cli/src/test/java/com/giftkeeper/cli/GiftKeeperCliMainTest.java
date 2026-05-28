package com.giftkeeper.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.giftkeeper.app.GiftKeeperApplication;
import com.giftkeeper.app.GiftKeeperUseCases;
import com.giftkeeper.domain.Person;
import com.google.inject.Injector;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GiftKeeperCliMainTest {
    @Test
    void shouldCreateDemoUserWhenRepositoryIsEmptyAndPrintPeople() {
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
        final Person person = new Person(UUID.randomUUID(), "Alice", LocalDate.of(2000, 1, 1));
        final List<String> output = new ArrayList<>();
        when(service.listPeople()).thenReturn(List.of()).thenReturn(List.of(person));

        GiftKeeperCliMain.run(service, output::add);

        verify(service).createPerson("CLI Demo User", LocalDate.of(1998, 6, 8));
        assertThat(output)
                .hasSize(1)
                .first()
                .asString()
                .contains("Alice");
    }

    @Test
    void shouldPrintExistingPeopleWithoutCreatingDemoUser() {
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
        final Person person = new Person(UUID.randomUUID(), "Bob", LocalDate.of(1999, 2, 2));
        final List<String> output = new ArrayList<>();
        when(service.listPeople()).thenReturn(List.of(person)).thenReturn(List.of(person));

        GiftKeeperCliMain.run(service, output::add);

        verify(service, never()).createPerson(anyString(), any());
        assertThat(output)
                .hasSize(1)
                .first()
                .asString()
                .contains("Bob");
    }

    @Test
    void shouldResolveServiceAndDelegateFromMain() {
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
        final Injector injector = mock(Injector.class);
        when(injector.getInstance(GiftKeeperUseCases.class)).thenReturn(service);
        when(service.listPeople()).thenReturn(List.of()).thenReturn(List.of());

        try (MockedStatic<GiftKeeperApplication> application = mockStatic(GiftKeeperApplication.class)) {
            application.when(GiftKeeperApplication::createJpaInjector).thenReturn(injector);

            GiftKeeperCliMain.main(new String[0]);

            verify(service).createPerson("CLI Demo User", LocalDate.of(1998, 6, 8));
        }
    }
}

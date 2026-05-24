package com.giftkeeper.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.giftkeeper.app.GiftKeeperApplication;
import com.giftkeeper.app.GiftKeeperUseCases;
import com.giftkeeper.domain.Person;
import com.google.inject.Injector;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GiftKeeperCliMainTest {
	@Test
	@SuppressWarnings("unchecked")
	void shouldCreateDemoUserWhenRepositoryIsEmptyAndPrintPeople() {
		final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
		final Injector injector = mock(Injector.class);
		final Person person = new Person(UUID.randomUUID(), "Alice", LocalDate.of(2000, 1, 1));
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final PrintStream originalOut = System.out;
		System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
		try (MockedStatic<GiftKeeperApplication> application = mockStatic(GiftKeeperApplication.class)) {
			application.when(GiftKeeperApplication::createJpaInjector).thenReturn(injector);
			when(injector.getInstance(GiftKeeperUseCases.class)).thenReturn(service);
			when(service.listPeople()).thenReturn(List.of()).thenReturn(List.of(person));

			GiftKeeperCliMain.main(new String[0]);

			verify(service).createPerson("CLI Demo User", LocalDate.of(1998, 6, 8));
			assertThat(output.toString(StandardCharsets.UTF_8)).contains("Alice");
		} finally {
			System.setOut(originalOut);
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	void shouldPrintExistingPeopleWithoutCreatingDemoUser() {
		final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
		final Injector injector = mock(Injector.class);
		final Person person = new Person(UUID.randomUUID(), "Bob", LocalDate.of(1999, 2, 2));
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final PrintStream originalOut = System.out;
		System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
		try (MockedStatic<GiftKeeperApplication> application = mockStatic(GiftKeeperApplication.class)) {
			application.when(GiftKeeperApplication::createJpaInjector).thenReturn(injector);
			when(injector.getInstance(GiftKeeperUseCases.class)).thenReturn(service);
			when(service.listPeople()).thenReturn(List.of(person)).thenReturn(List.of(person));

			GiftKeeperCliMain.main(new String[0]);

			org.mockito.Mockito.verify(service, org.mockito.Mockito.never()).createPerson(org.mockito.Mockito.anyString(), org.mockito.Mockito.any());
			assertThat(output.toString(StandardCharsets.UTF_8)).contains("Bob");
		} finally {
			System.setOut(originalOut);
		}
	}
}

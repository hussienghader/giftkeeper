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
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GiftKeeperCliMainTest {
    @Test
    @SuppressWarnings("unchecked")
    void shouldCreateDemoUserWhenRepositoryIsEmptyAndPrintPeople() {
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
        final Injector injector = mock(Injector.class);
        final Person person = new Person(UUID.randomUUID(), "Alice", LocalDate.of(2000, 1, 1));
        final TestLogHandler logHandler = attachCliLogHandler();

        try (MockedStatic<GiftKeeperApplication> application = mockStatic(GiftKeeperApplication.class)) {
            application.when(GiftKeeperApplication::createJpaInjector).thenReturn(injector);
            when(injector.getInstance(GiftKeeperUseCases.class)).thenReturn(service);
            when(service.listPeople()).thenReturn(List.of()).thenReturn(List.of(person));

            GiftKeeperCliMain.main(new String[0]);

            verify(service).createPerson("CLI Demo User", LocalDate.of(1998, 6, 8));
            assertThat(logHandler.messages()).contains("Alice");
        } finally {
            detachCliLogHandler(logHandler);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldPrintExistingPeopleWithoutCreatingDemoUser() {
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
        final Injector injector = mock(Injector.class);
        final Person person = new Person(UUID.randomUUID(), "Bob", LocalDate.of(1999, 2, 2));
        final TestLogHandler logHandler = attachCliLogHandler();

        try (MockedStatic<GiftKeeperApplication> application = mockStatic(GiftKeeperApplication.class)) {
            application.when(GiftKeeperApplication::createJpaInjector).thenReturn(injector);
            when(injector.getInstance(GiftKeeperUseCases.class)).thenReturn(service);
            when(service.listPeople()).thenReturn(List.of(person)).thenReturn(List.of(person));

            GiftKeeperCliMain.main(new String[0]);

            verify(service, never()).createPerson(anyString(), any());
            assertThat(logHandler.messages()).contains("Bob");
        } finally {
            detachCliLogHandler(logHandler);
        }
    }

    private static TestLogHandler attachCliLogHandler() {
        final Logger logger = Logger.getLogger(GiftKeeperCliMain.class.getName());
        final TestLogHandler handler = new TestLogHandler();
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.INFO);
        return handler;
    }

    private static void detachCliLogHandler(final TestLogHandler handler) {
        final Logger logger = Logger.getLogger(GiftKeeperCliMain.class.getName());
        logger.removeHandler(handler);
        logger.setUseParentHandlers(true);
    }

    private static final class TestLogHandler extends Handler {
        private final StringBuilder messages = new StringBuilder();

        @Override
        public void publish(final LogRecord record) {
            messages.append(record.getMessage()).append(System.lineSeparator());
        }

        @Override
        public void flush() {
            // Nothing to flush.
        }

        @Override
        public void close() {
            // Nothing to close.
        }

        String messages() {
            return messages.toString();
        }
    }
}

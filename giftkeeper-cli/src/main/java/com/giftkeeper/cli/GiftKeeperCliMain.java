package com.giftkeeper.cli;

import com.giftkeeper.app.GiftKeeperApplication;
import com.giftkeeper.app.GiftKeeperUseCases;
import com.giftkeeper.domain.Person;
import java.time.LocalDate;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GiftKeeperCliMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GiftKeeperCliMain.class);

    private GiftKeeperCliMain() {
    }

    public static void main(final String[] args) {
        final GiftKeeperUseCases service = GiftKeeperApplication.createJpaInjector().getInstance(GiftKeeperUseCases.class);
        run(service, LOGGER::info);
    }

    static void run(final GiftKeeperUseCases service, final Consumer<String> output) {
        if (service.listPeople().isEmpty()) {
            service.createPerson("CLI Demo User", LocalDate.of(1998, 6, 8));
        }
        for (Person person : service.listPeople()) {
            output.accept(formatPerson(person));
        }
    }

    private static String formatPerson(final Person person) {
        return "%s | %s | %s".formatted(person.getId(), person.getName(), person.getBirthDate());
    }
}

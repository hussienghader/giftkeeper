package com.giftkeeper.cli;

import com.giftkeeper.app.GiftKeeperApplication;
import com.giftkeeper.app.GiftKeeperUseCases;
import com.giftkeeper.domain.Person;
import java.time.LocalDate;
import java.util.logging.Logger;

public final class GiftKeeperCliMain {
    private static final Logger LOGGER = Logger.getLogger(GiftKeeperCliMain.class.getName());

    private GiftKeeperCliMain() {
    }

    public static void main(final String[] args) {
        final GiftKeeperUseCases service = GiftKeeperApplication.createJpaInjector().getInstance(GiftKeeperUseCases.class);
        if (service.listPeople().isEmpty()) {
            service.createPerson("CLI Demo User", LocalDate.of(1998, 6, 8));
        }
        for (Person person : service.listPeople()) {
            LOGGER.info(() -> String.format("%s | %s | %s", person.getId(), person.getName(), person.getBirthDate()));
        }
    }
}

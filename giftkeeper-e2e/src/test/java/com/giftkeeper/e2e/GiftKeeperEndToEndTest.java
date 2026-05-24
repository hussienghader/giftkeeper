package com.giftkeeper.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.giftkeeper.app.GiftKeeperApplication;
import com.giftkeeper.app.GiftKeeperUseCases;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.gui.GiftKeeperFrame;
import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class GiftKeeperEndToEndTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
        .withDatabaseName("giftkeeper")
        .withUsername("giftkeeper")
        .withPassword("giftkeeper");

    @Test
    void shouldRunWholeFlowAgainstRealDatabase() {
        System.setProperty("giftkeeper.jdbc.url", postgres.getJdbcUrl());
        System.setProperty("giftkeeper.jdbc.user", postgres.getUsername());
        System.setProperty("giftkeeper.jdbc.password", postgres.getPassword());
        final GiftKeeperUseCases service = GiftKeeperApplication.createJpaInjector()
            .getInstance(GiftKeeperUseCases.class);

        final var person = service.createPerson("Alice", LocalDate.of(1998, 6, 8));
        final var occasion = service.createOccasion(
            person.getId(), OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");
        final var gift = service.createGiftIdea(
            person.getId(), occasion.getId(), "Book", new BigDecimal("10.00"), "Sci-fi");
        service.changeGiftStatus(gift.getId(), GiftStatus.BOUGHT);

        assertThat(service.listPeople()).hasSize(1);
        assertThat(service.listOccasionsForPerson(person.getId())).hasSize(1);
        assertThat(service.listGifts()).hasSize(1);
        assertThat(service.listGifts().get(0).getStatus()).isEqualTo(GiftStatus.BOUGHT);

        if (!GraphicsEnvironment.isHeadless()) {
            final GiftKeeperFrame frame = new GiftKeeperFrame(service);
            frame.refreshAllTables();
            assertThat(frame.getPersonTableModel().getRowCount()).isEqualTo(1);
            assertThat(frame.getGiftTableModel().getRowCount()).isEqualTo(1);
            frame.dispose();
        }
    }
}

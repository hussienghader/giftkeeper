package com.giftkeeper.gui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.mockConstruction;

import com.giftkeeper.app.GiftKeeperApplication;
import com.giftkeeper.app.GiftKeeperUseCases;
import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.Occasion;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import com.google.inject.Injector;
import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class GiftKeeperFrameTest {
    @Test
    void shouldRefreshPersonTableAfterAdd() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "GUI test skipped in headless environment");
        final GiftKeeperUseCases service = GiftKeeperApplication.createInMemoryInjector().getInstance(GiftKeeperUseCases.class);
        final GiftKeeperFrame frame = new GiftKeeperFrame(service);
        frame.getPersonNameField().setText("Alice");
        frame.getPersonBirthDateField().setText("1998-06-08");
        frame.getAddPersonButton().doClick();
        assertThat(frame.getPersonTableModel().getRowCount()).isEqualTo(1);
        frame.dispose();
    }

    @Test
    void shouldRefreshAllTablesAndHandleAllActions() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "GUI test skipped in headless environment");
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
        final UUID personId = UUID.randomUUID();
        final UUID occasionId = UUID.randomUUID();
        final UUID giftId = UUID.randomUUID();
        final Person person = new Person(personId, "Alice", LocalDate.of(1998, 6, 8));
        final Occasion occasion = new Occasion(occasionId, personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");
        final GiftIdea gift = new GiftIdea(giftId, personId, occasionId, "Book", new BigDecimal("10.00"), GiftStatus.PLANNED, "Sci-fi");
        when(service.listPeople()).thenReturn(List.of(person));
        when(service.listOccasions()).thenReturn(List.of(occasion));
        when(service.listGifts()).thenReturn(List.of(gift));
        when(service.createPerson(anyString(), any(LocalDate.class))).thenReturn(person);
        when(service.createOccasion(eq(personId), eq(OccasionType.BIRTHDAY), eq(LocalDate.of(2026, 6, 8)), eq("Birthday"))).thenReturn(occasion);
        when(service.createGiftIdea(eq(personId), eq(occasionId), eq("Book"), eq(new BigDecimal("10.00")), eq("Sci-fi"))).thenReturn(gift);
        when(service.changeGiftStatus(giftId, GiftStatus.PLANNED)).thenReturn(gift);

        final GiftKeeperFrame frame = new GiftKeeperFrame(service);
        frame.refreshAllTables();
        assertThat(frame.getPersonTableModel().getRowCount()).isEqualTo(1);
        assertThat(frame.getGiftTableModel().getRowCount()).isEqualTo(1);

        frame.getPersonNameField().setText("Alice");
        frame.getPersonBirthDateField().setText("1998-06-08");
        frame.getAddPersonButton().doClick();

        frame.getOccasionPersonIdField().setText(personId.toString());
        frame.getOccasionTypeCombo().setSelectedItem(OccasionType.BIRTHDAY);
        frame.getOccasionDateField().setText("2026-06-08");
        frame.getOccasionDescriptionField().setText("Birthday");
        frame.getAddOccasionButton().doClick();

        frame.getGiftPersonIdField().setText(personId.toString());
        frame.getGiftOccasionIdField().setText(occasionId.toString());
        frame.getGiftTitleField().setText("Book");
        frame.getGiftPriceField().setText("10.00");
        frame.getGiftNotesField().setText("Sci-fi");
        frame.getAddGiftButton().doClick();

        frame.getGiftIdField().setText(giftId.toString());
        frame.getChangeStatusButton().doClick();

        verify(service).createPerson("Alice", LocalDate.of(1998, 6, 8));
        verify(service).createOccasion(personId, OccasionType.BIRTHDAY, LocalDate.of(2026, 6, 8), "Birthday");
        verify(service).createGiftIdea(personId, occasionId, "Book", new BigDecimal("10.00"), "Sci-fi");
        verify(service).changeGiftStatus(giftId, GiftStatus.PLANNED);
        assertThat(frame.getOccasionPersonIdField()).isNotNull();
        frame.dispose();
    }

    @Test
    void shouldHandleUiExceptionsWithoutPropagatingAndCoverBlankOccasionBranch() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "GUI test skipped in headless environment");
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
        when(service.listPeople()).thenReturn(List.of());
        when(service.listOccasions()).thenReturn(List.of());
        when(service.listGifts()).thenReturn(List.of());
        doAnswer(invocation -> { throw new IllegalArgumentException("bad input"); }).when(service).createPerson(anyString(), any(LocalDate.class));
        when(service.createGiftIdea(any(UUID.class), eq(null), anyString(), any(BigDecimal.class), anyString()))
            .thenReturn(new GiftIdea(UUID.randomUUID(), UUID.randomUUID(), null, "Gift", new BigDecimal("1.00"), GiftStatus.PLANNED, ""));

        try (MockedStatic<JOptionPane> dialog = mockStatic(JOptionPane.class)) {
            final GiftKeeperFrame frame = new GiftKeeperFrame(service);
            frame.getPersonNameField().setText("Alice");
            frame.getPersonBirthDateField().setText("1998-06-08");
            frame.getAddPersonButton().doClick();
            assertThat(frame.getPersonTableModel().getRowCount()).isZero();

            final UUID personId = UUID.randomUUID();
            frame.getGiftPersonIdField().setText(personId.toString());
            frame.getGiftOccasionIdField().setText("   ");
            frame.getGiftTitleField().setText("Gift");
            frame.getGiftPriceField().setText("1.00");
            frame.getGiftNotesField().setText("");
            frame.getAddGiftButton().doClick();

            verify(service).createGiftIdea(personId, null, "Gift", new BigDecimal("1.00"), "");
            dialog.verify(() -> JOptionPane.showMessageDialog(eq(frame), eq("bad input"), eq("GiftKeeper error"), eq(JOptionPane.ERROR_MESSAGE)));
            frame.dispose();
        }
    }

    @Test
    void shouldLaunchMainAndShowFrame() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "GUI test skipped in headless environment");
        final Injector injector = mock(Injector.class);
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);
        try (MockedStatic<GiftKeeperApplication> application = mockStatic(GiftKeeperApplication.class);
             MockedStatic<GiftKeeperFrame> frame = mockStatic(GiftKeeperFrame.class)) {
            application.when(GiftKeeperApplication::createJpaInjector).thenReturn(injector);
            when(injector.getInstance(GiftKeeperUseCases.class)).thenReturn(service);

            GiftKeeperGuiMain.main(new String[0]);

            frame.verify(() -> GiftKeeperFrame.showFrame(service));
        }
    }

    @Test
    void shouldInvokeShowFrameThroughSwingUtilities() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "GUI test skipped in headless environment");
        final GiftKeeperUseCases service = mock(GiftKeeperUseCases.class);

        try (MockedStatic<SwingUtilities> swing = mockStatic(SwingUtilities.class);
             MockedConstruction<GiftKeeperFrame> construction = mockConstruction(GiftKeeperFrame.class)) {
            swing.when(() -> SwingUtilities.invokeLater(any(Runnable.class))).thenAnswer(invocation -> {
                Runnable runnable = invocation.getArgument(0);
                runnable.run();
                return null;
            });

            GiftKeeperFrame.showFrame(service);

            swing.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)));
            assertThat(construction.constructed()).hasSize(1);
            verify(construction.constructed().get(0)).setVisible(true);
        }
    }
}

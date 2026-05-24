package com.giftkeeper.gui;

import com.giftkeeper.app.GiftKeeperUseCases;
import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GiftKeeperFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private final GiftKeeperUseCases service;
    private final DefaultTableModel personTableModel = new DefaultTableModel(new Object[]{"Id", "Name", "Birth date"}, 0);
    private final DefaultTableModel occasionTableModel = new DefaultTableModel(new Object[]{"Id", "Person", "Type", "Date", "Description"}, 0);
    private final DefaultTableModel giftTableModel = new DefaultTableModel(new Object[]{"Id", "Person", "Occasion", "Title", "Price", "Status"}, 0);

    private final JTextField personNameField = new JTextField();
    private final JTextField personBirthDateField = new JTextField("1998-06-08");
    private final JButton addPersonButton = new JButton("Add person");

    private final JTextField occasionPersonIdField = new JTextField();
    private final JComboBox<OccasionType> occasionTypeCombo = new JComboBox<>(OccasionType.values());
    private final JTextField occasionDateField = new JTextField("2026-06-08");
    private final JTextField occasionDescriptionField = new JTextField("Birthday");
    private final JButton addOccasionButton = new JButton("Add occasion");

    private final JTextField giftPersonIdField = new JTextField();
    private final JTextField giftOccasionIdField = new JTextField();
    private final JTextField giftTitleField = new JTextField();
    private final JTextField giftPriceField = new JTextField("10.00");
    private final JTextField giftNotesField = new JTextField();
    private final JButton addGiftButton = new JButton("Add gift");

    private final JComboBox<GiftStatus> giftStatusCombo = new JComboBox<>(new DefaultComboBoxModel<>(GiftStatus.values()));
    private final JTextField giftIdField = new JTextField();
    private final JButton changeStatusButton = new JButton("Change gift status");

    public GiftKeeperFrame(final GiftKeeperUseCases service) {
        super("GiftKeeper");
        this.service = service;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        buildUi();
        wireActions();
        refreshAllTables();
    }

    private void buildUi() {
        setLayout(new BorderLayout());

        final JPanel forms = new JPanel(new GridLayout(4, 1));
        forms.add(buildPersonPanel());
        forms.add(buildOccasionPanel());
        forms.add(buildGiftPanel());
        forms.add(buildStatusPanel());
        add(forms, BorderLayout.NORTH);

        final JPanel tables = new JPanel(new GridLayout(1, 3));
        tables.add(new JScrollPane(new JTable(personTableModel)));
        tables.add(new JScrollPane(new JTable(occasionTableModel)));
        tables.add(new JScrollPane(new JTable(giftTableModel)));
        add(tables, BorderLayout.CENTER);
    }

    private JPanel buildPersonPanel() {
        final JPanel panel = new JPanel(new GridLayout(1, 5));
        panel.add(new JLabel("Person name"));
        panel.add(personNameField);
        panel.add(new JLabel("Birth date (YYYY-MM-DD)"));
        panel.add(personBirthDateField);
        panel.add(addPersonButton);
        return panel;
    }

    private JPanel buildOccasionPanel() {
        final JPanel panel = new JPanel(new GridLayout(1, 6));
        panel.add(new JLabel("Person id"));
        panel.add(occasionPersonIdField);
        panel.add(occasionTypeCombo);
        panel.add(occasionDateField);
        panel.add(occasionDescriptionField);
        panel.add(addOccasionButton);
        return panel;
    }

    private JPanel buildGiftPanel() {
        final JPanel panel = new JPanel(new GridLayout(1, 7));
        panel.add(giftPersonIdField);
        panel.add(giftOccasionIdField);
        panel.add(giftTitleField);
        panel.add(giftPriceField);
        panel.add(giftNotesField);
        panel.add(addGiftButton);
        panel.add(new JLabel("personId, occasionId(optional), title, price, notes"));
        return panel;
    }

    private JPanel buildStatusPanel() {
        final JPanel panel = new JPanel(new GridLayout(1, 4));
        panel.add(giftIdField);
        panel.add(giftStatusCombo);
        panel.add(changeStatusButton);
        panel.add(new JLabel("giftId + target status"));
        return panel;
    }

    private void wireActions() {
        addPersonButton.addActionListener(e -> safeUiAction(() -> {
            service.createPerson(personNameField.getText(), LocalDate.parse(personBirthDateField.getText().trim()));
            refreshAllTables();
        }));
        addOccasionButton.addActionListener(e -> safeUiAction(() -> {
            service.createOccasion(UUID.fromString(occasionPersonIdField.getText().trim()),
                (OccasionType) occasionTypeCombo.getSelectedItem(),
                LocalDate.parse(occasionDateField.getText().trim()),
                occasionDescriptionField.getText());
            refreshAllTables();
        }));
        addGiftButton.addActionListener(e -> safeUiAction(() -> {
            final String occasionIdText = giftOccasionIdField.getText().trim();
            service.createGiftIdea(UUID.fromString(giftPersonIdField.getText().trim()),
                occasionIdText.isBlank() ? null : UUID.fromString(occasionIdText),
                giftTitleField.getText(),
                new BigDecimal(giftPriceField.getText().trim()),
                giftNotesField.getText());
            refreshAllTables();
        }));
        changeStatusButton.addActionListener(e -> safeUiAction(() -> {
            service.changeGiftStatus(UUID.fromString(giftIdField.getText().trim()), (GiftStatus) giftStatusCombo.getSelectedItem());
            refreshAllTables();
        }));
    }

    private void safeUiAction(final Runnable action) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "GiftKeeper error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshAllTables() {
        personTableModel.setRowCount(0);
        occasionTableModel.setRowCount(0);
        giftTableModel.setRowCount(0);

        for (Person person : service.listPeople()) {
            personTableModel.addRow(new Object[]{person.getId(), person.getName(), person.getBirthDate()});
        }
        service.listOccasions().forEach(occasion -> occasionTableModel.addRow(
            new Object[]{occasion.getId(), occasion.getPersonId(), occasion.getType(), occasion.getDate(), occasion.getDescription()}));
        for (GiftIdea gift : service.listGifts()) {
            giftTableModel.addRow(new Object[]{gift.getId(), gift.getPersonId(), gift.getOccasionId(), gift.getTitle(), gift.getEstimatedPrice(), gift.getStatus()});
        }
    }

    public JTextField getPersonNameField() { return personNameField; }
    public JTextField getPersonBirthDateField() { return personBirthDateField; }
    public JButton getAddPersonButton() { return addPersonButton; }
    public JTextField getOccasionPersonIdField() { return occasionPersonIdField; }
    public JTextField getGiftPersonIdField() { return giftPersonIdField; }
    public JTextField getOccasionDateField() { return occasionDateField; }
    public JTextField getOccasionDescriptionField() { return occasionDescriptionField; }
    public JComboBox<OccasionType> getOccasionTypeCombo() { return occasionTypeCombo; }
    public JButton getAddOccasionButton() { return addOccasionButton; }
    public JTextField getGiftOccasionIdField() { return giftOccasionIdField; }
    public JTextField getGiftTitleField() { return giftTitleField; }
    public JTextField getGiftPriceField() { return giftPriceField; }
    public JTextField getGiftNotesField() { return giftNotesField; }
    public JButton getAddGiftButton() { return addGiftButton; }
    public JTextField getGiftIdField() { return giftIdField; }
    public JButton getChangeStatusButton() { return changeStatusButton; }
    public DefaultTableModel getPersonTableModel() { return personTableModel; }
    public DefaultTableModel getGiftTableModel() { return giftTableModel; }

    public static void showFrame(final GiftKeeperUseCases service) {
        SwingUtilities.invokeLater(() -> new GiftKeeperFrame(service).setVisible(true));
    }
}

package com.giftkeeper.persistence.jpa;

import com.giftkeeper.domain.OccasionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "occasions")
public class JpaOccasionEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private JpaPersonEntity person;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OccasionType type;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 255)
    private String description;

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = id; }
    public JpaPersonEntity getPerson() { return person; }
    public void setPerson(final JpaPersonEntity person) { this.person = person; }
    public OccasionType getType() { return type; }
    public void setType(final OccasionType type) { this.type = type; }
    public LocalDate getDate() { return date; }
    public void setDate(final LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(final String description) { this.description = description; }
}

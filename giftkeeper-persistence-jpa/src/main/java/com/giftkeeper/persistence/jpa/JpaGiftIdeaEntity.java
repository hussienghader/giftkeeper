package com.giftkeeper.persistence.jpa;

import com.giftkeeper.domain.GiftStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "gift_ideas")
public class JpaGiftIdeaEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private JpaPersonEntity person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occasion_id")
    private JpaOccasionEntity occasion;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(name = "estimated_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GiftStatus status;

    @Column(nullable = false, length = 500)
    private String notes;

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = id; }
    public JpaPersonEntity getPerson() { return person; }
    public void setPerson(final JpaPersonEntity person) { this.person = person; }
    public JpaOccasionEntity getOccasion() { return occasion; }
    public void setOccasion(final JpaOccasionEntity occasion) { this.occasion = occasion; }
    public String getTitle() { return title; }
    public void setTitle(final String title) { this.title = title; }
    public BigDecimal getEstimatedPrice() { return estimatedPrice; }
    public void setEstimatedPrice(final BigDecimal estimatedPrice) { this.estimatedPrice = estimatedPrice; }
    public GiftStatus getStatus() { return status; }
    public void setStatus(final GiftStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(final String notes) { this.notes = notes; }
}

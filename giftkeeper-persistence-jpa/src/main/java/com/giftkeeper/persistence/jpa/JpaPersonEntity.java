package com.giftkeeper.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "persons")
public class JpaPersonEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(final LocalDate birthDate) { this.birthDate = birthDate; }
}

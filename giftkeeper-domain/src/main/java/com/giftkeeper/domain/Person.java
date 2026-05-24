package com.giftkeeper.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Person {
    private final UUID id;
    private String name;
    private LocalDate birthDate;

    public Person(final UUID id, final String name, final LocalDate birthDate) {
        this(id, name, birthDate, Clock.systemDefaultZone());
    }

    Person(final UUID id, final String name, final LocalDate birthDate, final Clock clock) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        setName(name);
        setBirthDate(birthDate, clock);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setName(final String name) {
        final String normalized = name == null ? "" : name.trim();
        if (normalized.isBlank()) {
            throw new DomainException("name must not be blank");
        }
        if (normalized.length() > 100) {
            throw new DomainException("name must be at most 100 characters");
        }
        this.name = normalized;
    }

    public void setBirthDate(final LocalDate birthDate) {
        setBirthDate(birthDate, Clock.systemDefaultZone());
    }

    void setBirthDate(final LocalDate birthDate, final Clock clock) {
        final LocalDate value = Objects.requireNonNull(birthDate, "birthDate must not be null");
        if (value.isAfter(LocalDate.now(clock))) {
            throw new DomainException("birthDate must not be in the future");
        }
        this.birthDate = value;
    }
}

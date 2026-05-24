package com.giftkeeper.app;

import com.giftkeeper.domain.Person;
import com.giftkeeper.persistence.api.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPersonRepository implements PersonRepository {
    private final Map<UUID, Person> store = new ConcurrentHashMap<>();

    @Override
    public Person save(final Person person) {
        store.put(person.getId(), person);
        return person;
    }

    @Override
    public Optional<Person> findById(final UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Person> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(final UUID id) {
        store.remove(id);
    }
}

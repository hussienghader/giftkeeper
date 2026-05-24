package com.giftkeeper.app;

import com.giftkeeper.domain.Occasion;
import com.giftkeeper.persistence.api.OccasionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOccasionRepository implements OccasionRepository {
    private final Map<UUID, Occasion> store = new ConcurrentHashMap<>();

    @Override
    public Occasion save(final Occasion occasion) {
        store.put(occasion.getId(), occasion);
        return occasion;
    }

    @Override
    public Optional<Occasion> findById(final UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Occasion> findByPersonId(final UUID personId) {
        return store.values().stream().filter(o -> o.getPersonId().equals(personId)).sorted((a, b) -> a.getDate().compareTo(b.getDate())).toList();
    }

    @Override
    public List<Occasion> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(final UUID id) {
        store.remove(id);
    }
}

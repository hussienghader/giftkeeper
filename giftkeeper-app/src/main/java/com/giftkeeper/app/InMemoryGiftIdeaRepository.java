package com.giftkeeper.app;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.persistence.api.GiftIdeaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryGiftIdeaRepository implements GiftIdeaRepository {
    private final Map<UUID, GiftIdea> store = new ConcurrentHashMap<>();

    @Override
    public GiftIdea save(final GiftIdea giftIdea) {
        store.put(giftIdea.getId(), giftIdea);
        return giftIdea;
    }

    @Override
    public Optional<GiftIdea> findById(final UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<GiftIdea> findByPersonId(final UUID personId) {
        return store.values().stream().filter(g -> g.getPersonId().equals(personId)).sorted((a, b) -> a.getTitle().compareTo(b.getTitle())).toList();
    }

    @Override
    public List<GiftIdea> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(final UUID id) {
        store.remove(id);
    }
}

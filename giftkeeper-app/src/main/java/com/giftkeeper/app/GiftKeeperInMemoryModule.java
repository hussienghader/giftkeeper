package com.giftkeeper.app;

import com.giftkeeper.persistence.api.GiftIdeaRepository;
import com.giftkeeper.persistence.api.OccasionRepository;
import com.giftkeeper.persistence.api.PersonRepository;
import com.google.inject.AbstractModule;

public class GiftKeeperInMemoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PersonRepository.class).to(InMemoryPersonRepository.class);
        bind(OccasionRepository.class).to(InMemoryOccasionRepository.class);
        bind(GiftIdeaRepository.class).to(InMemoryGiftIdeaRepository.class);
        bind(GiftKeeperUseCases.class).to(GiftKeeperService.class);
        bind(GiftSuggestionCatalog.class).to(RuleBasedGiftSuggestionCatalog.class);
    }
}

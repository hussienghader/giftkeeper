package com.giftkeeper.app;

import com.giftkeeper.domain.Budget;
import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.persistence.api.GiftIdeaRepository;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Application-level budget calculation based on stored gift ideas.
 */
public class BudgetService {
    private final GiftIdeaRepository giftIdeaRepository;

    @Inject
    public BudgetService(final GiftIdeaRepository giftIdeaRepository) {
        this.giftIdeaRepository = giftIdeaRepository;
    }

    public BigDecimal totalPlannedForPerson(final UUID personId) {
        return giftIdeaRepository.findByPersonId(personId).stream()
            .map(GiftIdea::getEstimatedPrice)
            .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }

    public boolean exceedsBudget(final Budget budget) {
        return budget.isExceededBy(totalPlannedForPerson(budget.getPersonId()));
    }
}

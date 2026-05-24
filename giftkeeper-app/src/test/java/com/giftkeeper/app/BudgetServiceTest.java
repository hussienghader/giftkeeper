package com.giftkeeper.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.giftkeeper.domain.Budget;
import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.persistence.api.GiftIdeaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {
    @Mock
    private GiftIdeaRepository giftIdeaRepository;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void calculatesTotalAndBudgetStatus() {
        UUID personId = UUID.randomUUID();
        when(giftIdeaRepository.findByPersonId(personId)).thenReturn(List.of(
            new GiftIdea(UUID.randomUUID(), personId, null, "Book", new BigDecimal("15.50"), GiftStatus.PLANNED, ""),
            new GiftIdea(UUID.randomUUID(), personId, null, "Mug", new BigDecimal("10.00"), GiftStatus.PLANNED, "")
        ));

        assertThat(budgetService.totalPlannedForPerson(personId)).isEqualByComparingTo("25.50");
        assertThat(budgetService.exceedsBudget(new Budget(personId, new BigDecimal("20.00")))).isTrue();
    }
}

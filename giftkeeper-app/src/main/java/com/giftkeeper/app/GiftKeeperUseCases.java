package com.giftkeeper.app;

import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.Occasion;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GiftKeeperUseCases {
    Person createPerson(String name, LocalDate birthDate);
    Occasion createOccasion(UUID personId, OccasionType type, LocalDate date, String description);
    GiftIdea createGiftIdea(UUID personId, UUID occasionId, String title, BigDecimal estimatedPrice, String notes);
    GiftIdea changeGiftStatus(UUID giftId, GiftStatus nextStatus);
    List<Person> listPeople();
    List<Occasion> listOccasions();
    List<GiftIdea> listGifts();
    List<Occasion> listOccasionsForPerson(UUID personId);
    List<GiftIdea> listGiftsForPerson(UUID personId);
}

package com.giftkeeper.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import com.giftkeeper.app.GiftKeeperApplication;
import com.giftkeeper.app.GiftKeeperUseCases;
import com.giftkeeper.domain.GiftIdea;
import com.giftkeeper.domain.GiftStatus;
import com.giftkeeper.domain.OccasionType;
import com.giftkeeper.domain.Person;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PersonCreationSteps {
    private GiftKeeperUseCases service;
    private Person currentPerson;
    private GiftIdea currentGift;

    @Given("an empty GiftKeeper application")
    public void anEmptyGiftKeeperApplication() {
        service = GiftKeeperApplication.createInMemoryInjector().getInstance(GiftKeeperUseCases.class);
    }

    @When("I add a person named {string} born on {string}")
    public void iAddAPersonNamedBornOn(final String name, final String birthDate) {
        currentPerson = service.createPerson(name, LocalDate.parse(birthDate));
    }

    @Then("the application should contain {int} person")
    public void theApplicationShouldContainPerson(final int expectedCount) {
        assertThat(service.listPeople()).hasSize(expectedCount);
    }

    @And("a person named {string} born on {string} exists")
    public void aPersonNamedBornOnExists(final String name, final String birthDate) {
        currentPerson = service.createPerson(name, LocalDate.parse(birthDate));
    }

    @When("I add a BIRTHDAY occasion on {string} with description {string}")
    public void iAddABirthdayOccasionOnWithDescription(final String date, final String description) {
        service.createOccasion(currentPerson.getId(), OccasionType.BIRTHDAY, LocalDate.parse(date), description);
    }

    @Then("the application should contain {int} occasion for that person")
    public void theApplicationShouldContainOccasionForThatPerson(final int expectedCount) {
        assertThat(service.listOccasionsForPerson(currentPerson.getId())).hasSize(expectedCount);
    }

    @When("I add a gift idea titled {string} with price {string} for that person")
    public void iAddAGiftIdeaTitledWithPriceForThatPerson(final String title, final String price) {
        currentGift = service.createGiftIdea(currentPerson.getId(), null, title, new BigDecimal(price), "");
    }

    @Then("the application should contain {int} gift idea for that person")
    public void theApplicationShouldContainGiftIdeaForThatPerson(final int expectedCount) {
        assertThat(service.listGiftsForPerson(currentPerson.getId())).hasSize(expectedCount);
    }

    @And("a gift idea titled {string} with price {string} for that person exists")
    public void aGiftIdeaTitledWithPriceForThatPersonExists(final String title, final String price) {
        currentGift = service.createGiftIdea(currentPerson.getId(), null, title, new BigDecimal(price), "");
    }

    @When("I change the gift status to BOUGHT")
    public void iChangeTheGiftStatusToBought() {
        currentGift = service.changeGiftStatus(currentGift.getId(), GiftStatus.BOUGHT);
    }

    @Then("the gift status should be BOUGHT")
    public void theGiftStatusShouldBeBought() {
        assertThat(currentGift.getStatus()).isEqualTo(GiftStatus.BOUGHT);
    }
}

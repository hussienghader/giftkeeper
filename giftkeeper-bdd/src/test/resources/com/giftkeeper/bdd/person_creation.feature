Feature: Manage people and gift ideas

  Scenario: Add a person to the application
    Given an empty GiftKeeper application
    When I add a person named "Alice" born on "1998-06-08"
    Then the application should contain 1 person

  Scenario: Create an occasion for a person
    Given an empty GiftKeeper application
    And a person named "Bob" born on "1990-03-15" exists
    When I add a BIRTHDAY occasion on "2026-03-15" with description "Birthday party"
    Then the application should contain 1 occasion for that person

  Scenario: Create a gift idea without an occasion
    Given an empty GiftKeeper application
    And a person named "Carol" born on "1995-07-20" exists
    When I add a gift idea titled "Book" with price "15.00" for that person
    Then the application should contain 1 gift idea for that person

  Scenario: Change the status of a gift
    Given an empty GiftKeeper application
    And a person named "Dave" born on "1988-11-05" exists
    And a gift idea titled "Watch" with price "99.00" for that person exists
    When I change the gift status to BOUGHT
    Then the gift status should be BOUGHT

Feature: SIM Card Activation

  Scenario: Successfully activate a SIM card
    Given I have a SIM card with ICCID "1255789453849037777" and customer email "test@success.com"
    When I submit an activation request
    Then the response should indicate success
    And the database should store the SIM card as active

  Scenario: Failed SIM card activation
    Given I have a SIM card with ICCID "8944500102198304826" and customer email "test@failure.com"
    When I submit an activation request
    Then the response should indicate
    And the database should store the SIM card as inactive
Feature: Trainer Management

  Background:
    Given a registered trainer with username "trainer.one"
    And a valid JWT token for "trainer.one"

  Scenario: Get trainer profile (positive)
    When the client requests trainer profile for "trainer.one"
    Then the response status should be 200
    And the response should contain trainer first name "Trainer"

  Scenario: Get trainer profile (negative - not found)
    When the client requests trainer profile for "ghost.trainer"
    Then the response status should be 404

  Scenario: Update trainer profile (positive)
    When the client updates trainer profile for "trainer.one" with new first name "Updated"
    Then the response status should be 200
    And the response should contain trainer first name "Updated"

  Scenario: Update trainer profile (negative - missing fields)
    When the client updates trainer profile with missing required fields
    Then the response status should be 400

  Scenario: Get unassigned trainers for trainee
    When the client requests unassigned trainers for trainee "john.doe"
    Then the response status should be 200

  Scenario: Change status of trainer (positive)
    When the client changes trainer status of "trainer.one" to false
    Then the response status should be 200

  Scenario: Change status (negative - trainer not found)
    When the client changes trainer status of "ghost.trainer" to true
    Then the response status should be 404

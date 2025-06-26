Feature: Trainee Management

  Background:
    Given a registered trainee with username "john.doe"
    And a valid JWT token for "john.doe"

  Scenario: Get trainee profile (positive)
    When the client requests trainee profile for "john.doe"
    Then the response status should be 200
    And the response should contain trainee first name "John"

  Scenario: Get trainee profile (negative - not found)
    When the client requests trainee profile for "non.existent"
    Then the response status should be 404

  Scenario: Update trainee profile (positive)
    When the client updates trainee profile for "john.doe" with new first name "Johnny"
    Then the response status should be 200
    And the response should contain trainee first name "Johnny"

  Scenario: Update trainee profile (negative - invalid input)
    When the client updates trainee profile with missing required fields
    Then the response status should be 400

  Scenario: Delete trainee (positive)
    When the client deletes trainee with username "john.doe"
    Then the response status should be 200

  Scenario: Delete trainee (negative - not found)
    When the client deletes trainee with username "invalid.user"
    Then the response status should be 404

  Scenario: Update trainer list for trainee (positive)
    When the client assigns trainers "trainer.one,trainer.two" to trainee "john.doe"
    Then the response status should be 200

  Scenario: Update trainer list (negative - trainee not found)
    When the client assigns trainers "trainer.one" to trainee "ghost.user"
    Then the response status should be 404

  Scenario: Change status of trainee (positive)
    When the client changes status of trainee "john.doe" to "false"
    Then the response status should be 200

  Scenario: Change status (negative - user not found)
    When the client changes status of trainee "ghost.user" to "true"
    Then the response status should be 404

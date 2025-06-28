Feature: Training management

  Scenario: Create a training dynamically
    Given a trainee is registered
    And a trainer is registered with specialization "boxing"
    And a training type "boxing" exists
    When a training is created
    Then the training should exist in the database

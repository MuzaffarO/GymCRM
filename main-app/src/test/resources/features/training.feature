Feature: Training management

  Scenario: Create a training dynamically
    Given a trainee is registered
    And a trainer is registered with specialization "boxing"
    And a training type "boxing" exists
    When a training is created
    Then the training should exist in the database

  Scenario: Fail to create training when trainer specialization does not match
    Given a trainee is registered
    And a trainer is registered with specialization "boxing"
    And a training type "karate" exists
    When a training is attempted with mismatched specialization
    Then the training should not be created

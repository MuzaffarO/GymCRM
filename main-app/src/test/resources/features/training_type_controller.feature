Feature: Training Type Management

  Background:
   Given an admin user exists
   And a valid JWT token for "admin"

  Scenario: Get all training types
    When the client requests the list of training types
    Then the response status should be 200

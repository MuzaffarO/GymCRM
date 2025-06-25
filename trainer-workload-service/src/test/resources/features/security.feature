Feature: JWT security enforcement

  Scenario: Access with valid JWT
    Given a valid JWT token for user "john.doe"
    When the client sends a GET request to "/api/workload/test" with the token
    Then the REST response status should be 200

  Scenario: Access with invalid JWT
    Given an invalid JWT token
    When the client sends a GET request to "/api/workload/test" with the token
    Then the REST response status should be 401

  Scenario: Access without JWT
    When the client sends a GET request to "/api/workload/test" without token
    Then the REST response status should be 401

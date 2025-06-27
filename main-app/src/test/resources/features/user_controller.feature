Feature: User Management
  Covers all core functionality of user registration, login, password changes, and logout.

  Scenario: Successfully register a trainer
    When the client registers a trainer with first name "John" and last name "Doe" and specialization "Fitness"
    Then the response status should be 200
    And the response should contain "John"

  Scenario: Successfully register a trainee
    When the client registers a trainee with first name "Jane" and last name "Smith" and date of birth "1990-01-01" and address "123 Main St"
    Then the response status should be 200
    And the response should contain "Jane"

  Scenario: Login with valid credentials
    Given a registered user with username "login.user" and password "secret123"
    When the client attempts to log in with username "login.user" and password "secret123"
    Then the response status should be 200
    And the response should contain "token"

  Scenario: Login with invalid credentials
    When the client attempts to log in with username "wrong.user" and password "wrongpass"
    Then the response status should be 401

  Scenario: Change password with valid old password
    Given a registered user with username "old.user" and password "password123"
    When the client changes password with old password "password123" and new password "password456"
    Then the response status should be 200

  Scenario: Change password with wrong old password
    Given a registered user with username "reset.user" and password "oldpass"
    When the client changes password with old password "wrongpass" and new password "newpass"
    Then the response status should be 401

  Scenario: Logout with valid token
    Given a valid JWT token for "logout.user"
    When the client logs out
    Then the response status should be 200
    And the response should contain "Logged out"

  Scenario: Logout with no token
    When the client logs out with no token
    Then the response status should be 400
    And the response should contain "No token provided"

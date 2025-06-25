Feature: Trainer Workload Summary

  Scenario: Add training workload
    Given a trainer with username "john.doe"
    When they perform a training on 2025-06-17 with duration 2.5 hours
    Then the total training hours for "john.doe" in June 2025 should be 2.5

  Scenario: Add another training
    Given a trainer with username "john.doe"
    When they perform a training on 2025-06-17 with duration 1.5 hours
    Then the total training hours for "john.doe" in June 2025 should be 1.5

  Scenario: Delete a training
    Given a trainer with username "john.doe"
    When a training on 2025-06-17 with duration 1.0 hour is deleted
    Then the total training hours for "john.doe" in June 2025 should be 0.0

  Scenario: Get training summary for unknown trainer
    When they request summary for "nonexistent.user" in June 2025
    Then the response status should be 404
    And the error message should contain "Trainer not found"

  Scenario: Get training summary for month with no data
    Given a trainer with username "john.doe"
    When they request summary for "john.doe" in February 2027
    Then the response status should be 404
    And the error message should contain "Trainer not found"

  Scenario: Submit workload with invalid JWT
    Given a malformed JWT token
    When a training workload is submitted
    Then the response status should be 401

Feature: Login and Logout for multiple roles

  Scenario Outline: Login and logout flow for roles
    Given I login as <role>
    Then I should get a valid access token
    When I call logout API
    Then I should see a successful logout response

    Examples:
      | role       |
      | "sales"    |
      | "admin"    |
      | "accountman" |


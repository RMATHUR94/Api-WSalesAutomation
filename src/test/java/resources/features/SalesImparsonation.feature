Feature: Impersonate the sales user for Essex Brownell customers Rahul.

  Background:
    Given I login as "sales"
    Then I should get a valid access token

  Scenario: Impersonate the sales user for Essex Brownell customers Rahul
    Given I checking the customer list for Essex Brownell
    Then the customerList response status code should be 200
    And the response message should be present
    And I extract the total number of mapped customers
    And I loop through the customer records and print details
    And I store the first customer record for impersonation


#    And I extract the total number of mapped customers
#    And I loop through the customer records and print details
#    And I store the first customer record for impersonation
#    When I impersonate the first customer with device and component details
#    Then the impersonation response status code should be 200
#    And the impersonation token should be present
#    And the impersonation message should be printed

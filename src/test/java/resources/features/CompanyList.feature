Feature: Login with sales user and getting the company list for assigned company

  Background:
    Given I login as "sales"
    Then I should get a valid access token

  Scenario: Fetch company list under a sales user
    When I request the company list with parameters
      | page      | 1        |
      | limit     | 10       |
      | search    |          |
      | type      | business |
      | sortBy    | id       |
      | sortOrder | desc     |
    Then the response status code should be 200
    And the response message should be "Customers fetched successfully"
    And I extract the details of the first customer
    And I extract the meta information from the response


  Scenario: As a sales user I can impersonate a customer named Rahul Mathur from Essex Brownell company and able to place a final order on the storefront/mother portal.
    When I checking the customer list for Essex Brownell
      | search        |            |
      | type          | individual |
      | company_id_id | 65df068605226f001269bf30 |
    Then the customerList response status code should be 200
    And the response message should be present
    And I extract the total number of mapped customers
    And I loop through the customer records and print details
    And I store the first customer record for impersonation
      # And the impersonation token extraction using otp from response saving the token
    When I impersonate the customer under Essex Brownell company
    Then I have received imparsonation otp and message
    When I verify the impersonation otp for the customer
    Then I should get a imparsonation access token and message
    Given When I add a product to the cart on storefront as an impersonated user
    Then The status code should be 200 and show the message
    When final order placement on storefront as an impersonated user
    Then final order status code should be 200 and show the message




    #    When I impersonate the first customer with device and component details
#    Then the impersonation response status code should be 200
#    And the impersonation message should be printed
#    And I verify the impersonation details


#Scenario: As a sales user I can impersonate the Rahul customer under Essex Brownell company.
#  When I impersonate the first customer with device and component details
#  Then the impersonation response status code should be 200
#  And the impersonation token should be present
#  And the impersonation message should be printed

#  Scenario: Fetch Essex Brownell customers and impersonate a sales user
#    When I request the customer-sales-user-mapping with parameters
#      | search        |            |
#      | type          | individual |
#      | company_id_id | 65df068605226f001269bf30 |
#    Then the customerList response status code should be 200
#    And the response message should be present
#    And I extract the total number of mapped customers
#    And I loop through the customer records and print details
#    And I store the first customer record for impersonation
#    When I impersonate the first customer with device and component details
#    Then the impersonation response status code should be 200
#    And the impersonation token should be present
#    And the impersonation message should be printed




Feature: Login into Mother portal

  Scenario Outline: Place temp order and final order on mother portal.
    Given Logged in with "<username>" and "<password>" on mother portal.
    When getting token and check the response status code should be 200
    When I add a <product> to the cart and save the order.
    When I place <product> final order on mother portal.
    Then final order status code should be 200 and show the message.

    Examples:
      | username        |password|
      | rahul.mathur@codeclouds.in   | cispl123 |




Feature: NBA Warriors Website Testing - Core Product
  As a user visiting the NBA Warriors website
  I want to test various functionalities
  So that I can ensure the website works correctly

  Background:
    Given I navigate to the Warriors website

  @TC1 @Shop @Jackets
  Scenario: Test Case 1 - Find all Jackets from Shop Menu
    Given I am on the Warriors homepage
    When I navigate to Shop Menu and click on Men's
    And I click on Jackets category
    Then I should see jacket products
    When I collect all jacket information from all pages
    Then I should store jacket details to a text file
    And the text file should be attached to the report

  @TC2 @NewFeatures @Videos
  Scenario: Test Case 2 - Count Video Feeds in New & Features
    Given I am on the Warriors homepage
    When I hover on menu item
    And I click on New & Features
    Then I should see video feeds
    When I count total number of video feeds
    And I count videos that are present for 3 or more days
    Then I should validate the video counts

  @TC1_Alternative @Shop @Jackets
  Scenario Outline: Test Case 1 Alternative - Find Jackets with different categories
    Given I am on the Warriors homepage
    When I navigate to Shop Menu and click on "<category>"
    And I click on Jackets category
    Then I should see jacket products
    When I collect all jacket information from all pages
    Then I should store jacket details to a text file
    
    Examples:
      | category |
      | Men's    |
      | Women's  |
      | Kids     |

  @TC2_Alternative @NewFeatures @Videos
  Scenario Outline: Test Case 2 Alternative - Count Videos with different time periods
    Given I am on the Warriors homepage
    When I hover on menu item
    And I click on New & Features
    Then I should see video feeds
    When I count total number of video feeds
    And I count videos that are present for "<days>" or more days
    Then I should validate the video counts
    
    Examples:
      | days |
      | 1    |
      | 3    |
      | 7    |

# Created by Vitalinsh at 14.12.2018
Feature: Classification
  user send file with picture to server and get response

  Scenario: Recognize a fruit
    Given server is working
    When user send picture
    Then return classification result

  Scenario: File not found or is not a picture
    Given server is working
    When user send request
    Then return message 'No file received'
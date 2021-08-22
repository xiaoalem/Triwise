# Triwise App - CPSC210 Project
Triwise is a Java Program which **aims to make sharing expenses** for travel, activities and daily life. With this app, 
you can organize your sharing expenses anytime, instead of collecting all the bills and doing calculation in Excel
later on.

This project is aimed to *people who travel together or have group activities need to split bills*. Working on this 
project is interesting mainly because I believe there are people like me who are bad at splitting bills by creating 
formulas in Excel. Thus, I would like to create the formulas in one app and use it for ever.


## User Stories
Phase 1:

- As a user, I want to be able to add a new trip to trip list
- As a user, I want to be able to add a participant to the participant list of a selected trip
- As a user, I want to be able to add an item to the item list of the selected trip
- As a user, I want to be able to view the list of trips I created
- As a user, I want to be able to select a trip and view the list of items in it
- As a user, I want to be able to select a trip and view the amount of money each person need to pay

Phase 2:
- As a user, I want to be able to save my trip list to file
- As a user, when I start the application, I want to be given the option to load all trips from the file

Phase 4: Task 2
 
Improvement of robustness in the class ParticipantListFunction
- Throw DuplicateNameException in method addParticipant when duplicate person name added to a trip or an item
- Override equals in Person class
- Throw EmptyListException in method addParticipantList when an empty participant list is add to a trip or an item

Phase 4: Task 3

In the TriwiseGUI class I noticed there is a poor cohesion. If I had more time, I would make "AddPanel" and "ViewPanel" 
into separated classes.
- If I had more time, I would let class Item and class Trip have a field of ParticipantList instead of extend the 
ParticipantListFunction.

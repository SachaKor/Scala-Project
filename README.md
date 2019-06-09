# SCALA Project - Youglouf

## Goal
Our project is a card game called Youglouf. Our goal is to implement Youglouf as a multiplayer video game written in SCALA.

## Youglouf
Youglouf is a __card game__ for 3 or more players.
It is usually played with a standard 52-card deck.
As most of card games, it is based on luck, but also requires some strategy skills.
Our goal is to create a web version of the game that would replace the real cards and allow playing Youglouf anywhere anytime.

### Rules
In the beginning of the game, every player receives 4 cards and can see 2 of them, but only __once__.
The rest of the cards are placed in the __closed deck__ in middle of the table.
Then the round begins. Every player plays once per round, in the clockwise order.
The card distributor starts the round:
* picks a card from the top of the closed or opened deck and watches it without showing to opponents
* decides whether he wants to replace one of his cards with the picked one or drop it
* drops the card (the newly picked or the replaced) to the __opened deck__ next to the closed one
* if any player has the card with the same rank, he can place it on top of the opened deck

*NOTE*:  if anyone reveals a wrong card (for example, tries to place a wrong card on the top of the opened deck), the punishment card is added to the player's card set, without revealing.

The goal of the game is to finish it with as little __points__ as possible.
The points are calculated as follows:

* from `2` to `10` points are equal to the rank
* `jack`: 11 points
* `queen`: 12 points
* `black king`: 13 points
* `ace`: 1 point
* `red king`: 0 points

So, `red kings` are the most "precious" cards of the game.

In addition, there are several __special cards__ allowing special features.
These features are applied only if the card is picked from the closed deck and directly dropped in the opened deck.
The __special cards__ and their features are listed below:

* `9` and `10`: the player can view one of his cards
* `jack`: the player can view the opponent's card of his choice
* `queen`: the player must exchange one of his cards with one of the opponent's cards, without revealing any of them

The __match finishes__ one round after one of the players declares a __Youglouf__ after finishing his turn.
Once the finish round is over, the points of all remaining cards are summed for each player.
If the player who declared Youglouf has the minimum match score, he earns -10 points.
If he has a match score equal to some other player's one, his score is calculated normally.
Otherwise, his score is equal to the accumulated score + 10.

*NOTE*: During the last round, all the players except the one who declared it cannot drop the last card of the card set, even if the card is of the same rank as on top of the opened deck.

The points of every match are accumulated until one of the players reaches the 100 points threshold.
The __winner__ of the game is the player having the minimum score.  

## Implementation   
Our main goal is to implement a multi-player game using WebSockets to maintain the state of the game across the different players and the server.
All accesses to the database (player registration, scores...) are made via a REST API using the Play framework.  
We use the database to store the players data: score statistics, the users' profiles, etc.
Accesses to the REST API are secured with a Json Web Token (JWT).

### Technologies
* Backend: SCALA Play framework, REST API, JWT   
* Frontend: React with Redux
* Database: Slick

### Backend

#### Model

#### Security
In order to secure the critical routes of our REST API, we used a unique Json Web Token for each user that contains the users' info.   
To implement this functionality, we created a `securedAction` class that contains the logic to validate a users' request providing a JWT, and we use it on our secure routes.
In addition to this, the websocket is secured in the same manner.

### Frontend

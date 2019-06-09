package models

// Represent a database's user entry.
case class User(id: Option[Long], username: String, password: String)

// Login request body
case class Login(username: String, password: String)

// Event web socket
case class InEvent(eventType: String)

// Event web socket
case class OutEvent(eventType: String)
package dao

import javax.inject.{Inject, Singleton}
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the User class with other DAO, thanks to the inheritance.
trait UserComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's users table in a object-oriented entity: the Student model.
  class UsersTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def username = column[String]("USERNAME")
    def password = column[String]("PASSWORD")
    def idx = index("USERNAME_IDX", (username), unique = true)

    // Map the attributes with the model; the ID is optional.
    def * = (id.?, username, password) <> (User.tupled, User.unapply)
  }

}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the users' query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends UserComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val users = TableQuery[UsersTable]

  /** Retrieve the list of users */
  def list(): Future[Seq[User]] = {
    val query = users.sortBy(u => (u.username))
    db.run(query.result)
  }

  /** Retrieve a user from the id. */
  def findByUsernameAndPassword(username: String, password: String): Future[Option[User]] =
    db.run(users.filter( u => u.username === username && u.password === password).result.headOption)

  /** Insert a new user, then return it. */
  def insert(user: User): Future[User] = {
    val insertQuery = users returning users.map(_.id) into ((users, id) => users.copy(Some(id)))
    db.run(insertQuery += user)
  }

  /** Update a user, then return an integer that indicate if the user was found (1) or not (0). */
  def update(id: Long, user: User): Future[Int] = {
    val userToUpdate: User = user.copy(Some(id))
    db.run(users.filter(_.id === id).update(userToUpdate))
  }

  /** Delete a user, then return an integer that indicate if the user was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(users.filter(_.id === id).delete)
}

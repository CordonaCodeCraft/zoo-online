package tech.cordona.zooonline.domain.user.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.common.TestAssets.INVALID_LONG_NAME
import tech.cordona.zooonline.common.TestAssets.INVALID_SHORT_NAME
import tech.cordona.zooonline.common.TestAssets.MISPELLED
import tech.cordona.zooonline.domain.user.entity.Authority
import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.model.AuthenticatedUserDetails
import tech.cordona.zooonline.domain.user.model.UserModel
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.validation.FailReport

internal class UserServiceTest(
	@Autowired private val userService: UserService,
	@Autowired private val passwordEncoder: BCryptPasswordEncoder
) : PersistenceTest() {

	@AfterEach
	fun afterEach() = userService.deleteAll()

	@Nested
	@DisplayName("User creation tests")
	inner class UserCreation {

		@Test
		@DisplayName("Successfully encodes password")
		fun `successfully encodes password`() {
			userService.createUser(userModel)
				.run { assertThat(passwordEncoder.matches(userModel.password, this.password)).isTrue }
		}

		@Test
		@DisplayName("Successfully creates new user")
		fun `successfully creates new user`() {

			userService.createUser(userModel)
				.run {
					assertThat(this.id).isNotNull
					assertThat(this.email).isEqualTo(userModel.email)
				}

			userService.createUser(user)
				.run {
					assertThat(this.id).isNotNull
					assertThat(this.email).isEqualTo(user.email)
				}
		}

		@Test
		@DisplayName("Successfully creates multiple users")
		fun `successfully creates multiple users`() {
			userService.createUsers(listOf(user, secondUser))
				.run { assertThat(this.size).isEqualTo(2) }
		}

		@ParameterizedTest(name = "Validated.Invalid name: {arguments}")
		@DisplayName("Throws when creating user with invalid properties")
		@ValueSource(strings = [INVALID_SHORT_NAME, INVALID_LONG_NAME])
		fun `throws when creating user with invalid properties`(invalidName: String) {

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(userModel.copy(firstName = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(userModel.copy(middleName = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(userModel.copy(lastName = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(userModel.copy(email = MISPELLED)) }
				.withMessageContaining(FailReport.invalidEmail())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(user.copy(firstName = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(user.copy(middleName = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(user.copy(lastName = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(user.copy(email = MISPELLED)) }
				.withMessageContaining(FailReport.invalidEmail())
		}

		@Test
		@DisplayName("Throws when creating user with non unique username")
		fun `throws when creating user with non unique username`() {
			userService.createUser(userModel)
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(userModel) }
				.withMessageContaining(FailReport.existingEmail(userModel.email))

			userService.createUser(user)
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { userService.createUser(user) }
				.withMessageContaining(FailReport.existingEmail(user.email))
		}

		@Test
		@DisplayName("Successfully inits user")
		fun `successfully inits user`() {
			userService.createUser(userModel)
				.let { created -> userService.initUser(created.id.toString()) }
				.run { assertThat(this.confirmed).isTrue }
		}

		@Test
		@DisplayName("Successfully converts retrieved user to authenticated user")
		fun `successfully converts retrieved user to authenticated user`() {
			val created = userService.createUser(userModel)
			val loaded = userService.loadUserByUsername(created.email) as AuthenticatedUserDetails

			assertThat(loaded.id).isEqualTo(created.id)
			assertThat(loaded.email).isEqualTo(created.email)
			assertThat(loaded.authority).isEqualTo(Authority.VISITOR)
			assertThat(passwordEncoder.matches(userModel.password, loaded.password)).isTrue
		}
	}

	@Nested
	@DisplayName("User retrieval tests")
	inner class UserRetrieval {

		@Test
		@DisplayName("Successfully retrieves user by username")
		fun `successfully retrieves user by username`() {
			userService.createUser(userModel)
				.let { created -> userService.findByUserName(userModel.email) }
				.run { assertThat(this.email).isEqualTo(userModel.email) }
		}

		@Test
		@DisplayName("Throws when retrieves user by wrong username")
		fun `throws when retrieves user by wrong username`() {
			userService.createUsers(listOf(user, secondUser))
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { userService.findByUserName(MISPELLED) }
				.withMessageContaining(FailReport.entityNotFound(entity = "User", idType = "username", id = MISPELLED))
		}

		@Test
		@DisplayName("Successfully retrieves user by ID")
		fun `successfully retrieves user by ID`() {
			userService.createUser(userModel)
				.let { created -> userService.findById(created.id.toString()) }
				.run { assertThat(this.email).isEqualTo(userModel.email) }
		}

		@Test
		@DisplayName("Throws when retrieves user by wrong ID")
		fun `throws when retrieves user by wrong ID`() {
			val wrongID = ObjectId.get().toString()
			userService.createUsers(listOf(user, secondUser))
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { userService.findById(wrongID) }
				.withMessageContaining(FailReport.entityNotFound(entity = "User", idType = "username", id = wrongID))
		}
	}

	companion object {

		val userModel = UserModel(
			firstName = "ModelFirstName",
			middleName = "ModelMiddleName",
			lastName = "ModelLastName",
			email = "model.first.last@foo.com",
			password = "ModelPassword"
		)

		val user = User(
			firstName = "UserFirstName",
			middleName = "UserMiddleName",
			lastName = "USerLastName",
			email = "user.first.last@foo.com",
			password = "UserPassword"
		)

		val secondUser = User(
			firstName = "SecondUserFirstName",
			middleName = "SecondUserMiddleName",
			lastName = "SecondUSerLastName",
			email = "second.user.first.last@foo.com",
			password = "SecondUserPassword"
		)
	}

}



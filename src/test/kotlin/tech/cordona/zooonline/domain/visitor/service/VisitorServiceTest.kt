package tech.cordona.zooonline.domain.visitor.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.common.TestAssets
import tech.cordona.zooonline.common.TestAssets.MISSPELLED
import tech.cordona.zooonline.common.TestAssets.amurTigerTU
import tech.cordona.zooonline.common.TestAssets.andeanBearTU
import tech.cordona.zooonline.common.TestAssets.carnivoreTU
import tech.cordona.zooonline.common.TestAssets.grizzlyBearTU
import tech.cordona.zooonline.common.TestAssets.wrongID
import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.service.UserService
import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.validation.FailReport
import tech.cordona.zooonline.validation.FailReport.entityNotFound

internal class VisitorServiceTest(
	@Autowired private val userService: UserService,
	@Autowired private val visitorService: VisitorService
) : PersistenceTest() {

	@BeforeAll
	fun beforeAll() = setupContext()

	@AfterEach
	fun afterEach() = clearContextAfterTest()

	@AfterAll
	fun afterAll() = clearContextAfterClass()


	@Nested
	@DisplayName("Visitor creation tests")
	inner class VisitorCreation {

		@Test
		@DisplayName("Successfully creates new visitor")
		fun `successfully creates new visitor`() {
			createVisitor()
				.run {
					assertThat(this.firstName).isEqualTo(visitor.firstName)
					assertThat(this.lastName).isEqualTo(visitor.lastName)
				}
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws when creating visitor with invalid properties")
		@ValueSource(strings = [TestAssets.INVALID_SHORT_NAME, TestAssets.INVALID_LONG_NAME])
		fun `throws when creating visitor with invalid properties`(invalidName: String) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { visitorService.create(visitor.copy(userId = userId, firstName = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { visitorService.create(visitor.copy(userId = userId, lastName = invalidName)) }
				.withMessageContaining(FailReport.invalidName())
		}

		@Test
		@DisplayName("Throws when creating visitor for non existing user")
		fun `Throws when creating visitor for non existing user`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { visitorService.create(visitor) }
				.withMessage(entityNotFound(entity = "User", idType = "ID", id = visitor.userId.toString()))
		}

		@Test
		@DisplayName("Throws when creating visitor for already existing user")
		fun `Throws when creating visitor for already existing user`() {
			createVisitor()
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { visitorService.create(visitor.copy(userId = userId)) }
				.withMessage(FailReport.existingUserId(userId.toString()))
		}
	}

	@Nested
	@DisplayName("Visitor retrieval tests")
	inner class VisitorRetrieval {

		@Test
		@DisplayName("Successfully retrieves Visitor by ID")
		fun `successfully retrieves Visitor by ID`() {
			val created = createVisitor()
			val retrieved = visitorService.findVisitorByUserId(created.userId.toString())
			assertThat(created.id).isEqualTo(retrieved.id)
		}

		@Test
		@DisplayName("Throws when retrieves Visitor by wrong ID")
		fun `throws when retrieves Visitor by wrong ID`() {
			createVisitor()
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { visitorService.findVisitorByUserId(wrongID.toString()) }
				.withMessage(entityNotFound(entity = "Visitor", idType = "ID", id = wrongID.toString()))
		}
	}

	@Nested
	@DisplayName("Visitor modification tests")
	inner class VisitorModification {

		@Test
		@DisplayName("Successfully adds and updates favorites")
		fun `successfully adds and updates favorites`() {

			val visitor = createVisitor()

			val firstUpdate =
				visitorService.addFavorites(visitor.userId.toString(), setOf(andeanBearTU.name, grizzlyBearTU.name))

			val secondUpdate =
				visitorService.addFavorites(visitor.userId.toString(), setOf(andeanBearTU.name, amurTigerTU.name))

			assertThat(firstUpdate.favorites.containsAll(setOf(andeanBearTU.name, grizzlyBearTU.name)))
			assertThat(secondUpdate.favorites.containsAll(setOf(andeanBearTU.name, grizzlyBearTU.name, amurTigerTU)))
			assertThat(secondUpdate.favorites.size).isEqualTo(3)
		}

		@Test
		@DisplayName("Throws when adds to favorites not existing animal")
		fun `throws when adds to favorites not existing animal`() {

			val visitor = createVisitor()

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy {
					visitorService.addFavorites(visitor.userId.toString(), setOf(andeanBearTU.name, MISSPELLED))
				}
				.withMessage(FailReport.invalidTaxonomyDetails())
		}

		@Test
		@DisplayName("Successfully removes favorites")
		fun `successfully removes favorites`() {

			val visitor = createVisitor()

			visitorService.addFavorites(visitor.userId.toString(), setOf(andeanBearTU.name, grizzlyBearTU.name))
				.let { visitorWithFavorites ->
					visitorService.removeFavorites(visitorWithFavorites.userId.toString(), setOf(andeanBearTU.name))
				}
				.run {
					assertThat(this.favorites.contains(andeanBearTU.name)).isFalse
					assertThat(this.favorites.size).isEqualTo(1)
				}
		}

		@Test
		@DisplayName("Throws when removes not existing animal")
		fun `throws when removes not existing animal`() {

			val visitor = createVisitor()

			visitorService.addFavorites(visitor.userId.toString(), setOf(andeanBearTU.name, grizzlyBearTU.name))
				.run {
					assertThatExceptionOfType(EntityNotFoundException::class.java)
						.isThrownBy {
							visitorService.removeFavorites(this.userId.toString(), setOf(andeanBearTU.name, MISSPELLED))
						}
						.withMessage(FailReport.invalidTaxonomyDetails())
				}
		}
	}

	override fun setupContext() {
		createTaxonomyUnits(carnivoreTU, andeanBearTU, grizzlyBearTU, amurTigerTU)
		createUser().also { userId = it.id!! }
	}

	override fun clearContextAfterTest() = visitorService.deleteAll()

	override fun clearContextAfterClass() {
		taxonomyUnitService.deleteAll()
		userService.deleteAll()
	}

	private fun createUser() = userService.createUser(user)

	private fun createVisitor() = visitorService.create(visitor.copy(userId = userId))

	companion object {

		lateinit var userId: ObjectId

		private val user = User(
			firstName = "FirstName",
			middleName = "MiddleName",
			lastName = "LastName",
			email = "user.first.last@zoo-online.com",
			password = "UserPassword"
		)

		private val visitor = Visitor(
			userId = ObjectId.get(),
			firstName = user.firstName,
			lastName = user.lastName,
			favorites = mutableSetOf()
		)
	}
}
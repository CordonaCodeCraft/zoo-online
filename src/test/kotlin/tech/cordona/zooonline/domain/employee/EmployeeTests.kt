package tech.cordona.zooonline.domain.employee

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
import tech.cordona.zooonline.common.TestAssets.africanElephantSpecie
import tech.cordona.zooonline.common.TestAssets.africanElephantTU
import tech.cordona.zooonline.common.TestAssets.amurTigerSpecie
import tech.cordona.zooonline.common.TestAssets.amurTigerTU
import tech.cordona.zooonline.common.TestAssets.andeanBearSpecie
import tech.cordona.zooonline.common.TestAssets.andeanBearTU
import tech.cordona.zooonline.common.TestAssets.carnivoreArea
import tech.cordona.zooonline.common.TestAssets.carnivoreTU
import tech.cordona.zooonline.common.TestAssets.elephantArea
import tech.cordona.zooonline.common.TestAssets.elephantTU
import tech.cordona.zooonline.common.TestAssets.grizzlyBearSpecie
import tech.cordona.zooonline.common.TestAssets.grizzlyBearTU
import tech.cordona.zooonline.common.TestAssets.group
import tech.cordona.zooonline.common.TestAssets.indianElephantSpecie
import tech.cordona.zooonline.common.TestAssets.indianElephantTU
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.CARNIVORE
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.ELEPHANT
import tech.cordona.zooonline.domain.trainer.entity.Trainer
import tech.cordona.zooonline.domain.trainer.service.TrainerService
import tech.cordona.zooonline.domain.user.entity.Authority.TRAINER
import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.service.UserService
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.validation.FailReport
import tech.cordona.zooonline.validation.FailReport.entityNotFound

internal class EmployeeTests(
	@Autowired private val userService: UserService,
	@Autowired private val trainerService: TrainerService,
	@Autowired private val areaService: AreaService,
	@Autowired private val animalService: AnimalService,
	@Autowired private val cellService: CellService,
) : PersistenceTest() {

	@BeforeAll
	fun beforeAll() = setupContext()

	@AfterEach
	fun afterEach() = clearContextAfterTest()

	@AfterAll
	fun afterAll() = clearContextAfterClass()

	@Nested
	@DisplayName("Employee creation tests")
	inner class EmployeeCreation {

		@Test
		@DisplayName("Successfully creates new employee")
		fun `successfully creates new employee`() {
			createEmployee()
				.run {
					assertThat(this.firstName).isEqualTo(employee.firstName)
					assertThat(this.middleName).isEqualTo(employee.middleName)
					assertThat(this.firstName).isEqualTo(employee.firstName)
				}
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws when creating employee with invalid properties")
		@ValueSource(strings = [TestAssets.INVALID_SHORT_NAME, TestAssets.INVALID_LONG_NAME])
		fun `throws when creating employee with invalid properties`(invalidName: String) {

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					trainerService.create(employee.copy(userId = userId, firstName = invalidName))
				}
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					trainerService.create(employee.copy(userId = userId, middleName = invalidName))
				}
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					trainerService.create(employee.copy(userId = userId, lastName = invalidName))
				}
				.withMessageContaining(FailReport.invalidName())
		}

		@Test
		@DisplayName("Throws when creating employee for non existing user")
		fun `Throws when creating employee for non existing user`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { trainerService.create(employee) }
				.withMessage(entityNotFound(entity = "User", idType = "ID", id = employee.userId.toString()))
		}

		@Test
		@DisplayName("Throws when creating employee for non existing area")
		fun `Throws when creating employee for non existing area`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy {
					trainerService.create(employee.copy(userId = userId, area = MISSPELLED))
				}
				.withMessage(entityNotFound(entity = "Area", idType = "name", id = MISSPELLED))
		}
	}

	override fun setupContext() {

		createTaxonomyUnits(
			carnivoreTU,
			elephantTU,
			andeanBearTU,
			grizzlyBearTU,
			amurTigerTU,
			africanElephantTU,
			indianElephantTU
		)

		createAnimals(
			grizzlyBearSpecie,
			andeanBearSpecie,
			amurTigerSpecie,
			africanElephantSpecie,
			indianElephantSpecie
		)

		val carnivoreCells = createCells(
			CARNIVORE.name,
			grizzlyBearSpecie,
			andeanBearSpecie,
			amurTigerSpecie
		)
		val elephantCells = createCells(
			ELEPHANT.name,
			africanElephantSpecie,
			indianElephantSpecie
		)

		createAreas(carnivoreArea.copy(cells = carnivoreCells), elephantArea.copy(cells = elephantCells))

		createUser(user).also { userId = it.id!! }
	}

	override fun clearContextAfterTest() = trainerService.deleteAll()

	override fun clearContextAfterClass() {
		taxonomyUnitService.deleteAll()
		areaService.deleteAll()
		userService.deleteAll()
	}

	private fun createAnimals(vararg animals: Animal) = animalService.createMany(animals.toList())

	private fun createCells(animalType: String, vararg species: Animal) =
		species
			.map { specie -> createCell(specie, animalType) }
			.let { cells -> cellService.createMany(cells) }
			.map { created -> created.id!! }
			.toSet()

	private fun createCell(specie: Animal, animalType: String) =
		Cell(
			animalGroup = group.name.asTitlecase(),
			animalType = animalType.asTitlecase(),
			specie = specie.taxonomyDetails.name,
			species = animalService.findAllBySpecie(specie.taxonomyDetails.name).map { it.id!! }.toMutableSet()
		)

	private fun createAreas(vararg areas: Area) = areaService.createMany(areas.toList())

	private fun createUser(user: User) = userService.createUser(user)

	protected fun createEmployee() = trainerService.create(employee.copy(userId = userId))

	companion object {

		lateinit var userId: ObjectId

		val user = User(
			firstName = "FirstName",
			middleName = "MiddleName",
			lastName = "LastName",
			email = "user.first.last@zoo-online.com",
			password = "UserPassword",
			authority = TRAINER
		)

		val employee = Trainer(
			userId = ObjectId.get(),
			firstName = user.firstName,
			middleName = user.middleName,
			lastName = user.lastName,
			area = CARNIVORE.name.asTitlecase(),
			position = TRAINER.name.asTitlecase(),
			animals = mutableSetOf()
		)
	}
}
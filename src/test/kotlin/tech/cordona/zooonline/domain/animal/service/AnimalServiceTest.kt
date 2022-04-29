package tech.cordona.zooonline.domain.animal.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.entity.enums.Gender.MALE
import tech.cordona.zooonline.domain.animal.entity.enums.HealthStatus
import tech.cordona.zooonline.domain.animal.entity.enums.TrainingStatus
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceTest.Companion.child
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceTest.Companion.invalidLongName
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceTest.Companion.invalidShortName
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceTest.Companion.validChainOfUnits
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_NAME_LENGTH
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_WEIGHT
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_NAME_LENGTH
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_WEIGHT

internal class AnimalServiceTest(
	@Autowired private val animalService: AnimalService,
	@Autowired private val taxonomyUnitService: TaxonomyUnitService,
) : PersistenceTest() {

	@BeforeAll
	fun beforeAll() {
		taxonomyUnitService.createMany(validChainOfUnits)
	}

	@BeforeEach
	fun beforeEach() {
		animalService.deleteAll()
	}

	@Nested
	@DisplayName("Animal creation tests")
	@TestInstance(PER_CLASS)
	inner class AnimalCreation {

		@Test
		@DisplayName("Successfully creates a valid animal")
		fun `Successfully creates animal`() {
			animalService.create(animal)
				.let { animalService.findById(it.id!!) }
				.run { assertThat(this.name).isEqualTo(animal.name) }
		}

		@Test
		@DisplayName("Successfully creates multiple animals")
		fun `successfully creates multiple animals`() {
			animalService.createMany(listOf(animal.copy(name = "First"), animal.copy(name = "Second")))
				.let { animalService.findAll() }
				.run { assertThat(this.size).isEqualTo(2) }
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws if name is not valid")
		@ValueSource(strings = [invalidShortName, invalidLongName])
		fun `throws if name is not valid`(invalidName: String) {
			Assertions.assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(animal.copy(name = invalidName))
				}
				.withMessageContaining("The name must be between $MIN_NAME_LENGTH and $MAX_NAME_LENGTH characters long")
		}

		@ParameterizedTest(name = "Invalid age: {arguments}")
		@DisplayName("Throws if age is not valid")
		@ValueSource(ints = [MIN_AGE - 1, MAX_AGE + 1])
		fun `throws if age is not valid`(invalidAge: Int) {
			Assertions.assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(animal.copy(age = invalidAge))
				}
				.withMessageContaining("The animal's age must be an integer between $MIN_AGE and $MAX_AGE")
		}

		@ParameterizedTest(name = "Invalid weight: {arguments}")
		@DisplayName("Throws if weight is not valid")
		@ValueSource(doubles = [MIN_WEIGHT - 1.0, MAX_WEIGHT + 1.0])
		fun `throws if weight is not valid`(invalidWeight: Double) {
			Assertions.assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(animal.copy(weight = invalidWeight))
				}
				.withMessageContaining("The animal's weight must be a double between $MIN_WEIGHT and $MAX_WEIGHT")
		}

		@ParameterizedTest(name = "Invalid health points: {arguments}")
		@DisplayName("Throws if health points are not valid")
		@ValueSource(ints = [MIN_HEALTH_POINTS - 1, MAX_HEALTH_POINTS + 1])
		fun `throws if health points are not valid`(invalidHealthPoints: Int) {
			Assertions.assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(animal.copy(healthStatistics = healthStatistics.copy(healthPoints = invalidHealthPoints)))
				}
				.withMessageContaining("The health points must be an integer between $MIN_HEALTH_POINTS and $MAX_HEALTH_POINTS")
		}

		@ParameterizedTest(name = "Invalid training points: {arguments}")
		@DisplayName("Throws if training points are not valid")
		@ValueSource(ints = [MIN_TRAINING_POINTS - 1, MAX_TRAINING_POINTS + 1])
		fun `throws if training points are not valid`(invalidTrainingPoints: Int) {
			Assertions.assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(animal.copy(healthStatistics = healthStatistics.copy(trainingPoints = invalidTrainingPoints)))
				}
				.withMessageContaining("The training points must be an integer between $MIN_TRAINING_POINTS and $MAX_TRAINING_POINTS")
		}

		@Test
		@DisplayName("Throws if taxonomy unit is  not valid")
		fun `throws if taxonomy unit is not valid`() {
			Assertions.assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(animal.copy(taxonomyDetails = child.copy(name = "Invalid")))
				}
				.withMessageContaining("Taxonomy details are not valid: missing or misspelled taxonomy unit and/or parent")
		}

		@Test
		@DisplayName("Throws if url is  not valid")
		fun `throws if url is not not valid`() {
			Assertions.assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(animal.copy(url = "animal.org"))
				}
				.withMessageContaining("URL is not valid")
		}
	}

	@Nested
	@DisplayName("Animal retrieval tests")
	@TestInstance(PER_CLASS)
	inner class AnimalRetrieval {

		@Test
		@DisplayName("Throws with wrong ID")
		fun `throws with wrong ID`() {
			val wrongId = ObjectId.get()

			animalService.createMany(
				listOf(
					animal.copy(name = "First"),
					animal.copy(name = "Second"),
					animal.copy(name = "Third")
				)
			)

			Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy {
					animalService.findById(wrongId)
				}
				.withMessage("Animal with ID: $wrongId not found")
		}

		@Test
		@DisplayName("Finds all by IDs")
		fun `finds all by IDs`() {

			val retrieved = animalService.createMany(
				listOf(
					animal.copy(name = "First"),
					animal.copy(name = "Second"),
					animal.copy(name = "Third")
				)
			)
				.map { animal -> animal.id.toString() }
				.let { animalService.findAllByIds(it) }

			SoftAssertions()
				.apply {
					assertThat(retrieved.find { animal -> animal.name == "First" }).isNotNull
					assertThat(retrieved.find { animal -> animal.name == "Second" }).isNotNull
					assertThat(retrieved.find { animal -> animal.name == "Third" }).isNotNull
				}
				.assertAll()
		}
	}

	companion object {
		private val healthStatistics = HealthStatistics(
			trainingPoints = 5,
			trainingStatus = TrainingStatus.TRAINED.name.asTitlecase(),
			healthPoints = 1,
			healthStatus = HealthStatus.SICK.name.asTitlecase()
		)

		private val animal = Animal(
			name = "Animal",
			age = 5,
			weight = 10.0,
			gender = MALE.name.asTitlecase(),
			taxonomyDetails = child,
			healthStatistics = healthStatistics,
			url = "https://www.animal.org/animal"
		)
	}
}
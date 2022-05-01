package tech.cordona.zooonline.domain.animal.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
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
import tech.cordona.zooonline.common.TestAssets.andeanBearSpecie
import tech.cordona.zooonline.common.TestAssets.carnivoreTU
import tech.cordona.zooonline.common.TestAssets.healthStatistics
import tech.cordona.zooonline.common.TestAssets.invalidLongName
import tech.cordona.zooonline.common.TestAssets.invalidShortName
import tech.cordona.zooonline.common.TestAssets.validChainOfUnits
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.validation.FailReport
import tech.cordona.zooonline.validation.FailReport.entityNotFound
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_WEIGHT
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_WEIGHT

@TestInstance(PER_CLASS)
internal class AnimalServiceTest(
	@Autowired private val animalService: AnimalService,
	@Autowired private val taxonomyUnitService: TaxonomyUnitService,
) : PersistenceTest() {

	@BeforeAll
	fun beforeAll() {
		taxonomyUnitService.deleteAll()
		taxonomyUnitService.createMany(validChainOfUnits)
	}

	@BeforeEach
	fun beforeEach() = animalService.deleteAll()

	@Nested
	@DisplayName("Animal creation tests")
	inner class AnimalCreation {

		@Test
		@DisplayName("Successfully creates a valid animal")
		fun `Successfully creates animal`() {
			animalService.create(andeanBearSpecie)
				.let { animalService.findById(it.id!!) }
				.run { assertThat(this.name).isEqualTo(andeanBearSpecie.name) }
		}

		@Test
		@DisplayName("Successfully creates multiple animals")
		fun `successfully creates multiple animals`() {
			animalService.createMany(
				listOf(
					andeanBearSpecie.copy(name = "First"),
					andeanBearSpecie.copy(name = "Second")
				)
			)
				.let { animalService.findAll() }
				.run { assertThat(this.size).isEqualTo(2) }
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws if name is not valid")
		@ValueSource(strings = [invalidShortName, invalidLongName])
		fun `throws if name is not valid`(invalidName: String) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { animalService.create(andeanBearSpecie.copy(name = invalidName)) }
				.withMessageContaining(FailReport.invalidName())
		}

		@ParameterizedTest(name = "Invalid age: {arguments}")
		@DisplayName("Throws if age is not valid")
		@ValueSource(ints = [MIN_AGE - 1, MAX_AGE + 1])
		fun `throws if age is not valid`(invalidAge: Int) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { animalService.create(andeanBearSpecie.copy(age = invalidAge)) }
				.withMessageContaining(FailReport.invalidAge())
		}

		@ParameterizedTest(name = "Invalid weight: {arguments}")
		@DisplayName("Throws if weight is not valid")
		@ValueSource(doubles = [MIN_WEIGHT - 1.0, MAX_WEIGHT + 1.0])
		fun `throws if weight is not valid`(invalidWeight: Double) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { animalService.create(andeanBearSpecie.copy(weight = invalidWeight)) }
				.withMessageContaining(FailReport.invalidWeight())
		}

		@ParameterizedTest(name = "Invalid health points: {arguments}")
		@DisplayName("Throws if health points are not valid")
		@ValueSource(ints = [MIN_HEALTH_POINTS - 1, MAX_HEALTH_POINTS + 1])
		fun `throws if health points are not valid`(invalidHealthPoints: Int) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(
						andeanBearSpecie.copy(
							healthStatistics = healthStatistics.copy(
								healthPoints = invalidHealthPoints
							)
						)
					)
				}
				.withMessageContaining(FailReport.invalidHealthPoints())
		}

		@ParameterizedTest(name = "Invalid training points: {arguments}")
		@DisplayName("Throws if training points are not valid")
		@ValueSource(ints = [MIN_TRAINING_POINTS - 1, MAX_TRAINING_POINTS + 1])
		fun `throws if training points are not valid`(invalidTrainingPoints: Int) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy {
					animalService.create(
						andeanBearSpecie.copy(
							healthStatistics = healthStatistics.copy(
								trainingPoints = invalidTrainingPoints
							)
						)
					)
				}
				.withMessageContaining(FailReport.invalidTrainingPoints())
		}

		@Test
		@DisplayName("Throws if taxonomy unit is  not valid")
		fun `throws if taxonomy unit is not valid`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { animalService.create(andeanBearSpecie.copy(taxonomyDetails = carnivoreTU.copy(name = "Invalid"))) }
				.withMessageContaining(FailReport.invalidTaxonomyDetails())
		}

		@Test
		@DisplayName("Throws if url is not valid")
		fun `throws if url is not not valid`() {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { animalService.create(andeanBearSpecie.copy(url = "animal.org")) }
				.withMessageContaining(FailReport.invalidURL())
		}
	}

	@Nested
	@DisplayName("Animal retrieval tests")
	inner class AnimalRetrieval {

		@Test
		@DisplayName("Throws with wrong ID")
		fun `throws with wrong ID`() {
			val wrongId = ObjectId.get()

			animalService.createMany(
				listOf(
					andeanBearSpecie.copy(name = "First"),
					andeanBearSpecie.copy(name = "Second"),
					andeanBearSpecie.copy(name = "Third")
				)
			)

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { animalService.findById(wrongId) }
				.withMessage(entityNotFound(entity = "Animal", idType = "ID", id = wrongId.toString()))
		}

		@Test
		@DisplayName("Finds all by IDs")
		fun `finds all by IDs`() {

			val retrieved = animalService.createMany(
				listOf(
					andeanBearSpecie.copy(name = "First"),
					andeanBearSpecie.copy(name = "Second"),
					andeanBearSpecie.copy(name = "Third")
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
}
package tech.cordona.zooonline.domain.area.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.common.TestAssets.invalidLongName
import tech.cordona.zooonline.common.TestAssets.invalidShortName
import tech.cordona.zooonline.common.TestAssets.validChainOfUnits
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.AreaStaff
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Group
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.CARNIVORE
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.ELEPHANT
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.validation.FailReport
import tech.cordona.zooonline.validation.FailReport.entityNotFound

internal class AreaServiceTest(
	@Autowired private val areaService: AreaService,
	@Autowired private val taxonomyUnitService: TaxonomyUnitService
) : PersistenceTest() {

	@BeforeEach
	fun beforeEach() = areaService.deleteAll().also { taxonomyUnitService.deleteAll() }

	@Nested
	@DisplayName("Area creation tests")
	inner class AreaCreation {

		@Test
		@DisplayName("Successfully creates area")
		fun `successfully creates area`() {
			taxonomyUnitService.createMany(validChainOfUnits)
				.let { areaService.create(carnivoreArea) }
				.run { assertThat(this.name).isEqualTo(CARNIVORE.name.asTitlecase()) }
		}

		@Test
		@DisplayName("Successfully creates multiple areas")
		fun `successfully creates multiple areas`() {
			mutableListOf(validChainOfUnits, listOf(elephantTU))
				.flatten()
				.also { taxonomyUnitService.createMany(it) }
				.let { areaService.createMany(listOf(carnivoreArea, elephantArea)) }
				.also {
					SoftAssertions()
						.apply {
							assertThat(it.find { area -> area.name == CARNIVORE.name.asTitlecase() }).isNotNull
							assertThat(it.find { area -> area.name == ELEPHANT.name.asTitlecase() }).isNotNull
						}
						.assertAll()
				}
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws when area name is not valid string")
		@ValueSource(strings = [invalidShortName, invalidLongName])
		fun `throws when area name is not valid string`(invalidName: String) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { areaService.create(carnivoreArea.copy(name = invalidName)) }
				.withMessageContaining(FailReport.invalidName())
		}

		@Test
		@DisplayName("Throws when area name is not unique")
		fun `throws when area name is not unique`() {
			taxonomyUnitService.createMany(validChainOfUnits)
				.also { areaService.create(carnivoreArea) }
				.run {
					assertThatExceptionOfType(InvalidEntityException::class.java)
						.isThrownBy { areaService.create(carnivoreArea) }
						.withMessageContaining(FailReport.existingArea(carnivoreArea.name))
				}
		}

		@Test
		@DisplayName("Throws when taxonomy unit with this name does not exist")
		fun `throws when taxonomy unit with this name does not exist`() {
			taxonomyUnitService.createMany(validChainOfUnits)
				.also { areaService.create(carnivoreArea) }
				.run {
					assertThatExceptionOfType(EntityNotFoundException::class.java)
						.isThrownBy { areaService.create(carnivoreArea.copy(name = "Missing name")) }
						.withMessageContaining(FailReport.invalidTaxonomyDetails())
				}
		}
	}

	@Nested
	@DisplayName("Area retrieval tests")
	inner class AreaRetrieval {

		@Test
		@DisplayName("Successfully retrieves all areas")
		fun `successfully retrieves all areas`() {
			mutableListOf(validChainOfUnits, listOf(elephantTU))
				.flatten()
				.also { taxonomyUnitService.createMany(it) }
				.also { areaService.createMany(listOf(carnivoreArea, elephantArea)) }
				.let { areaService.findAll() }
				.run { assertThat(this.size).isEqualTo(2) }
		}

		@Test
		@DisplayName("Successfully retrieves area by name")
		fun `successfully retrieves area by name`() {
			taxonomyUnitService.createMany(validChainOfUnits)
				.also { areaService.create(carnivoreArea) }
				.let { areaService.findAreaByName(carnivoreArea.name) }
				.run { assertThat(this.name).isEqualTo(carnivoreArea.name) }
		}

		@Test
		@DisplayName("Successfully retrieves all areas by name")
		fun `successfully retrieves all areas by name`() {
			mutableListOf(validChainOfUnits, listOf(elephantTU))
				.flatten()
				.also { taxonomyUnitService.createMany(it) }
				.also { areaService.createMany(listOf(carnivoreArea, elephantArea)) }
				.let { areaService.findAllByNames(listOf(carnivoreArea.name, elephantArea.name)) }
				.run { assertThat(this.size).isEqualTo(2) }
		}

		@Test
		@DisplayName("Throws when area name is not valid")
		fun `throws when area name is not valid`() {
			mutableListOf(validChainOfUnits, listOf(elephantTU))
				.flatten()
				.also { taxonomyUnitService.createMany(it) }
				.also { areaService.createMany(listOf(carnivoreArea, elephantArea)) }
				.also {
					assertThatExceptionOfType(EntityNotFoundException::class.java)
						.isThrownBy { areaService.findAreaByName("Invalid name") }
						.withMessageContaining(entityNotFound(entity = "Area", idType = "name", id = "Invalid name"))
				}
		}
	}

	companion object {
		private val staff = AreaStaff(
			trainers = mutableSetOf(),
			doctors = mutableSetOf(),
			guards = mutableSetOf()
		)
		private val carnivoreArea = Area(
			name = CARNIVORE.name.asTitlecase(),
			cells = setOf(),
			staff = staff
		)

		private val elephantArea = Area(
			name = ELEPHANT.name.asTitlecase(),
			cells = setOf(),
			staff = staff
		)

		private val elephantTU = TaxonomyUnit(
			name = ELEPHANT.name.asTitlecase(),
			parent = Group.MAMMAL.name.asTitlecase(),
			children = mutableSetOf()
		)
	}
}












package tech.cordona.zooonline.domain.area.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.common.TestAssets.INVALID_LONG_NAME
import tech.cordona.zooonline.common.TestAssets.INVALID_SHORT_NAME
import tech.cordona.zooonline.common.TestAssets.MISPELLED
import tech.cordona.zooonline.common.TestAssets.carnivoreTU
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.AreaStaff
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Group
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.CARNIVORE
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.ELEPHANT
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.validation.FailReport
import tech.cordona.zooonline.validation.FailReport.entityNotFound

internal class AreaServiceTest(@Autowired private val areaService: AreaService) : PersistenceTest() {

	@AfterEach
	fun afterEach() = taxonomyUnitService.deleteAll().also { areaService.deleteAll() }

	@Nested
	@DisplayName("Area creation tests")
	inner class AreaCreation {

		@Test
		@DisplayName("Successfully creates area")
		fun `successfully creates area`() {
			persistTaxonomyUnits(carnivoreTU)
				.let { areaService.create(carnivoreArea) }
				.run { assertThat(this.name).isEqualTo(CARNIVORE.name.asTitlecase()) }
		}

		@Test
		@DisplayName("Successfully creates multiple areas")
		fun `successfully creates multiple areas`() {
			persistTaxonomyUnits(carnivoreTU, elephantTU)
				.also {
					assertThat(it.find { area -> area.name == CARNIVORE.name.asTitlecase() }).isNotNull
					assertThat(it.find { area -> area.name == ELEPHANT.name.asTitlecase() }).isNotNull
				}
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws when area name is not valid string")
		@ValueSource(strings = [INVALID_SHORT_NAME, INVALID_LONG_NAME])
		fun `throws when area name is not valid string`(invalidName: String) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { areaService.create(carnivoreArea.copy(name = invalidName)) }
				.withMessageContaining(FailReport.invalidName())
		}

		@Test
		@DisplayName("Throws when area name is not unique")
		fun `throws when area name is not unique`() {
			persistTaxonomyUnits(carnivoreTU)
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
			persistTaxonomyUnits(carnivoreTU)
				.also { areaService.create(carnivoreArea) }
				.run {
					assertThatExceptionOfType(EntityNotFoundException::class.java)
						.isThrownBy { areaService.create(carnivoreArea.copy(name = MISPELLED)) }
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
			persistTaxonomyUnits(carnivoreTU, elephantTU)
				.also { areaService.createMany(listOf(carnivoreArea, elephantArea)) }
				.let { areaService.findAll() }
				.run { assertThat(this.size).isEqualTo(2) }
		}

		@Test
		@DisplayName("Successfully retrieves area by name")
		fun `successfully retrieves area by name`() {
			persistTaxonomyUnits(carnivoreTU)
				.also { areaService.create(carnivoreArea) }
				.let { areaService.findAreaByName(carnivoreArea.name) }
				.run { assertThat(this.name).isEqualTo(carnivoreArea.name) }
		}

		@Test
		@DisplayName("Successfully retrieves all areas by name")
		fun `successfully retrieves all areas by name`() {
			persistTaxonomyUnits(carnivoreTU, elephantTU)
				.also { areaService.createMany(listOf(carnivoreArea, elephantArea)) }
				.let { areaService.findAllByNames(listOf(carnivoreArea.name, elephantArea.name)) }
				.run { assertThat(this.size).isEqualTo(2) }
		}

		@Test
		@DisplayName("Throws when area name is not valid")
		fun `throws when area name is not valid`() {
			persistTaxonomyUnits(carnivoreTU, elephantTU)
				.also { areaService.createMany(listOf(carnivoreArea, elephantArea)) }
				.also {
					assertThatExceptionOfType(EntityNotFoundException::class.java)
						.isThrownBy { areaService.findAreaByName(MISPELLED) }
						.withMessageContaining(entityNotFound(entity = "Area", idType = "name", id = MISPELLED))
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












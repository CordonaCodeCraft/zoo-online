package tech.cordona.zooonline.domain.cell.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.common.TestAssets.INVALID_LONG_NAME
import tech.cordona.zooonline.common.TestAssets.INVALID_SHORT_NAME
import tech.cordona.zooonline.common.TestAssets.MiSPELLED
import tech.cordona.zooonline.common.TestAssets.amurTigerTU
import tech.cordona.zooonline.common.TestAssets.andeanBearSpecie
import tech.cordona.zooonline.common.TestAssets.andeanBearTU
import tech.cordona.zooonline.common.TestAssets.carnivoreTU
import tech.cordona.zooonline.common.TestAssets.grizzlyBearTU
import tech.cordona.zooonline.common.TestAssets.group
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.validation.FailReport

internal class CellServiceTest : PersistenceTest() {

	@AfterEach
	fun afterEach() = taxonomyUnitService.deleteAll().also { cellService.deleteAll() }

	@Nested
	@DisplayName("Cell creation tests")
	inner class CellCreation {

		@Test
		@DisplayName("Successfully creates a cell")
		fun `successfully creates a cell`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU)
				.let { cellService.create(andeanBearCell) }
				.let { createdCell -> cellService.findCellBySpecie(createdCell.specie) }
				?.run { assertThat(this.specie).isEqualTo(andeanBearCell.specie) }
		}

		@Test
		@DisplayName("Successfully creates a cell with animal")
		fun `successfully creates a cell with animal`() {
			var animalId: ObjectId
			persistTaxonomyUnits(carnivoreTU, andeanBearTU)
				.also { animalService.create(andeanBearSpecie).also { animalId = it.id!! } }
				.let { cellService.create(andeanBearCell.copy(species = mutableSetOf(animalId))) }
				.let { createdCell -> cellService.findCellBySpecie(createdCell.specie) }
				?.run { assertThat(this.species.contains(animalId)).isTrue }
		}

		@Test
		@DisplayName("Throws when animal ID is not valid")
		fun `Throws when animal ID is not valid`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU)
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(species = mutableSetOf(ObjectId.get()))) }
				.withMessageContaining(FailReport.animalNotFound())
		}

		@Test
		@DisplayName("Successfully creates multiple cells")
		fun `successfully creates multiple cells`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU, grizzlyBearTU, amurTigerTU)
				.also { cellService.createMany(listOf(andeanBearCell, grizzlyBearCell, amurTigerCell)) }
				.run { assertThat(cellService.findAll().size).isEqualTo(3) }
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws when cell group or cell type are not valid strings")
		@ValueSource(strings = [INVALID_SHORT_NAME, INVALID_LONG_NAME])
		fun `throws when cell group or cell type are not valid strings`(invalidName: String) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(animalGroup = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(animalType = invalidName)) }
				.withMessageContaining(FailReport.invalidName())
		}

		@Test
		@DisplayName("Throws when cell specie is not unique")
		fun `throws when cell specie is not unique`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU).also { cellService.create(andeanBearCell) }
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell) }
				.withMessageContaining(FailReport.existingCell(andeanBearCell.specie))
		}

		@Test
		@DisplayName("Throws when cell specie does not exist")
		fun `throws when cell specie does not exist`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(MiSPELLED)) }
				.withMessageContaining(FailReport.invalidTaxonomyDetails())
		}

		@Test
		@DisplayName("Throws when cell group or cell type do not exist")
		fun `throws when cell group or cell type do not exist`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(animalGroup = MiSPELLED)) }
				.withMessageContaining(FailReport.invalidTaxonomyDetails())
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(animalType = MiSPELLED)) }
				.withMessageContaining(FailReport.invalidTaxonomyDetails())
		}
	}

	@Nested
	@DisplayName("Cell retrieval tests")
	inner class CellRetrieval {

		@Test
		@DisplayName("Retrieves all by ID")
		fun `retrieves all by ID`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU, grizzlyBearTU, amurTigerTU)
				.let { cellService.createMany(listOf(andeanBearCell, grizzlyBearCell, amurTigerCell)) }
				.let { cellService.findAllById(it.map { cell -> cell.id.toString() }) }
				.map { it.id.toString() }
				.run { assertThat(this.size).isEqualTo(3) }
		}

		@Test
		@DisplayName("Retrieves all by animal type")
		fun `retrieves all by animal type`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU, grizzlyBearTU, amurTigerTU)
				.also { cellService.createMany(listOf(andeanBearCell, grizzlyBearCell, amurTigerCell)) }
				.let { cellService.findAllByAnimalType(carnivoreTU.name) }
				.run { assertThat(this.size).isEqualTo(3) }
		}

		@Test
		@DisplayName("Returns empty collection when animal type is wrong")
		fun `retrieves empty collection when animal type is wrong`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU, grizzlyBearTU, amurTigerTU)
				.also { cellService.createMany(listOf(andeanBearCell, grizzlyBearCell, amurTigerCell)) }
				.let { cellService.findAllByAnimalType(MiSPELLED) }
				.run { assertThat(this.isEmpty()).isTrue }
		}

		@Test
		@DisplayName("Successfully retrieves cell by specie")
		fun `successfully retrieves cell by specie`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU)
				.let { cellService.create(andeanBearCell) }
				.let { cell -> cellService.findCellBySpecie(cell.specie) }
				?.run { assertThat(this.specie).isEqualTo(andeanBearCell.specie) }
		}

		@Test
		@DisplayName("Throws when retrieves cell by wrong specie")
		fun `throws when retrieves cell by wrong specie`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU, grizzlyBearTU, amurTigerTU)
				.also { cellService.createMany(listOf(andeanBearCell, grizzlyBearCell, amurTigerCell)) }
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { cellService.findCellBySpecie(MiSPELLED) }
				.withMessageContaining(FailReport.entityNotFound(entity = "Cell", idType = "specie", id = MiSPELLED))
		}
	}

	companion object {
		private val andeanBearCell = Cell(
			animalGroup = group.name,
			animalType = carnivoreTU.name,
			specie = andeanBearTU.name,
			species = mutableSetOf()
		)
		private val grizzlyBearCell = Cell(
			animalGroup = group.name,
			animalType = carnivoreTU.name,
			specie = grizzlyBearTU.name,
			species = mutableSetOf()
		)
		private val amurTigerCell = Cell(
			animalGroup = group.name,
			animalType = carnivoreTU.name,
			specie = amurTigerTU.name,
			species = mutableSetOf()
		)
	}
}
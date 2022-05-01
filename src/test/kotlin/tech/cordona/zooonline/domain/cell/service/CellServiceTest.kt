package tech.cordona.zooonline.domain.cell.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.bson.types.ObjectId
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
import tech.cordona.zooonline.common.TestAssets.amurTigerUnit
import tech.cordona.zooonline.common.TestAssets.andeanBearUnit
import tech.cordona.zooonline.common.TestAssets.animal
import tech.cordona.zooonline.common.TestAssets.grizzlyBearUnit
import tech.cordona.zooonline.common.TestAssets.group
import tech.cordona.zooonline.common.TestAssets.invalidLongName
import tech.cordona.zooonline.common.TestAssets.invalidShortName
import tech.cordona.zooonline.common.TestAssets.type
import tech.cordona.zooonline.common.TestAssets.validChainOfUnits
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_NAME_LENGTH
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_NAME_LENGTH

internal class CellServiceTest(
	@Autowired private val cellService: CellService,
	@Autowired private val animalService: AnimalService,
	@Autowired private val taxonomyUnitService: TaxonomyUnitService
) : PersistenceTest() {

	@BeforeEach
	fun beforeEach() = taxonomyUnitService.deleteAll().also { cellService.deleteAll() }

	@Nested
	@DisplayName("Cell creation tests")
	@TestInstance(PER_CLASS)
	inner class CellCreation {

		@Test
		@DisplayName("Successfully creates a cell")
		fun `successfully creates a cell`() {
			taxonomyUnitService.createMany(validChainOfUnits)
				.let { cellService.create(andeanBearCell) }
				.let { createdCell -> cellService.findCellBySpecie(createdCell.specie) }
				?.run { assertThat(this.specie).isEqualTo(andeanBearCell.specie) }
		}

		@Test
		@DisplayName("Successfully creates a cell with animal")
		fun `successfully creates a cell with animal`() {
			var animalId: ObjectId
			taxonomyUnitService.createMany(validChainOfUnits)
				.also { animalService.create(animal).also { animalId = it.id!! } }
				.let { cellService.create(andeanBearCell.copy(species = mutableSetOf(animalId))) }
				.let { createdCell -> cellService.findCellBySpecie(createdCell.specie) }
				?.run { assertThat(this.species.contains(animalId)).isTrue }
		}

		@Test
		@DisplayName("Throws when animal ID is not valid")
		fun `Throws when animal ID is not valid`() {
			taxonomyUnitService.createMany(validChainOfUnits)
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(species = mutableSetOf(ObjectId.get()))) }
				.withMessageContaining("Invalid animals ID(s)")
		}

		@Test
		@DisplayName("Successfully creates multiple cells")
		fun `successfully creates multiple cells`() {
			mutableListOf(validChainOfUnits, listOf(grizzlyBearUnit, amurTigerUnit))
				.flatten()
				.also { taxonomyUnits -> taxonomyUnitService.createMany(taxonomyUnits) }
				.also { cellService.createMany(listOf(andeanBearCell, grizzlyBearCell, amurTigerCell)) }
				.run { assertThat(cellService.findAll().size).isEqualTo(3) }
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws when cell group or cell type are not valid strings")
		@ValueSource(strings = [invalidShortName, invalidLongName])
		fun `throws when cell group or cell type are not valid strings`(invalidName: String) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(animalGroup = invalidName)) }
				.withMessageContaining("The name must be between $MIN_NAME_LENGTH and $MAX_NAME_LENGTH characters long")

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(animalType = invalidName)) }
				.withMessageContaining("The name must be between $MIN_NAME_LENGTH and $MAX_NAME_LENGTH characters long")
		}

		@Test
		@DisplayName("Throws when cell specie is not unique")
		fun `throws when cell specie is not unique`() {
			taxonomyUnitService.createMany(validChainOfUnits).also { cellService.create(andeanBearCell) }
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell) }
				.withMessageContaining("Cell with specie: ${andeanBearCell.specie} already exists")
		}

		@Test
		@DisplayName("Throws when cell specie does not exist")
		fun `throws when cell specie does not exist`() {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy("Does not exist")) }
				.withMessageContaining("Invalid taxonomy unit")
		}

		@Test
		@DisplayName("Throws when cell group or cell type do not exist")
		fun `throws when cell group or cell type do not exist`() {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(animalGroup = "Does not exist")) }
				.withMessageContaining("Invalid taxonomy unit")
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { cellService.create(andeanBearCell.copy(animalType = "Does not exist")) }
				.withMessageContaining("Invalid taxonomy unit")
		}
	}

	@Nested
	@DisplayName("Cell retrieval tests")
	@TestInstance(PER_CLASS)
	inner class CellRetrieval {

		@Test
		@DisplayName("Retrieves all by ID")
		fun `retrieves all by ID`() {
			mutableListOf(validChainOfUnits, listOf(grizzlyBearUnit, amurTigerUnit))
				.flatten()
				.let { units -> taxonomyUnitService.createMany(units) }
				.let { cellService.createMany(listOf(andeanBearCell, grizzlyBearCell, amurTigerCell)) }
				.let { cellService.findAllById(it.map { cell -> cell.id.toString() }) }
				.map { it.id.toString() }
				.run { assertThat(this.size).isEqualTo(3) }
		}

		@Test
		@DisplayName("Successfully retrieves cell by specie")
		fun `successfully retrieves cell by specie`() {
			taxonomyUnitService.createMany(validChainOfUnits)
				.let { cellService.create(andeanBearCell) }
				.let { cell -> cellService.findCellBySpecie(cell.specie) }
				?.run { assertThat(this.specie).isEqualTo(andeanBearCell.specie) }
		}

		@Test
		@DisplayName("Throws when cell specie is wrong")
		fun `Throws when cell specie is wrong`() {
			mutableListOf(validChainOfUnits, listOf(grizzlyBearUnit, amurTigerUnit))
				.flatten()
				.let { units -> taxonomyUnitService.createMany(units) }
				.also { cellService.createMany(listOf(andeanBearCell, grizzlyBearCell, amurTigerCell)) }

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { cellService.findCellBySpecie("Invalid") }
				.withMessageContaining("Cell with specie Invalid not found")
		}
	}

	companion object {
		private val andeanBearCell = Cell(
			animalGroup = group.name,
			animalType = type.name,
			specie = andeanBearUnit.name,
			species = mutableSetOf()
		)
		private val grizzlyBearCell = Cell(
			animalGroup = group.name,
			animalType = type.name,
			specie = grizzlyBearUnit.name,
			species = mutableSetOf()
		)
		private val amurTigerCell = Cell(
			animalGroup = group.name,
			animalType = type.name,
			specie = amurTigerUnit.name,
			species = mutableSetOf()
		)
	}
}
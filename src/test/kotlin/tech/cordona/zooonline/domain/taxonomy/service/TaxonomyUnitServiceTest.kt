package tech.cordona.zooonline.domain.taxonomy.taxonomyUnitService

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.common.TestAssets.AMUR_TIGER
import tech.cordona.zooonline.common.TestAssets.ANDEAN_BEAR
import tech.cordona.zooonline.common.TestAssets.GRIZZLY_BEAR
import tech.cordona.zooonline.common.TestAssets.INVALID_LONG_NAME
import tech.cordona.zooonline.common.TestAssets.INVALID_SHORT_NAME
import tech.cordona.zooonline.common.TestAssets.MiSPELLED
import tech.cordona.zooonline.common.TestAssets.amurTigerTU
import tech.cordona.zooonline.common.TestAssets.andeanBearTU
import tech.cordona.zooonline.common.TestAssets.carnivoreTU
import tech.cordona.zooonline.common.TestAssets.grizzlyBearTU
import tech.cordona.zooonline.common.TestAssets.invalidGraphOfTaxonomyUnits
import tech.cordona.zooonline.common.TestAssets.kingdom
import tech.cordona.zooonline.common.TestAssets.phylum
import tech.cordona.zooonline.common.TestAssets.root
import tech.cordona.zooonline.common.TestAssets.validGraphOfTaxonomyUnits
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.validation.FailReport
import tech.cordona.zooonline.validation.FailReport.entityNotFound

internal class TaxonomyUnitServiceTest : PersistenceTest() {

	@AfterEach
	fun afterEach() = taxonomyUnitService.deleteAll()

	@Nested
	@DisplayName("Taxonomy unit creation tests")
	inner class TaxonomyUnitCreation {

		@Test
		@DisplayName("Successfully creates and associates properly chained taxonomy units")
		fun `Successfully saves and associates properly chained taxonomy units`() {

			taxonomyUnitService.createMany(validGraphOfTaxonomyUnits)

			assertThat(taxonomyUnitService.findByName(root.name)!!.children).contains(kingdom.name)
			assertThat(taxonomyUnitService.findByName(kingdom.name)!!.parent).isEqualTo(root.name)
			assertThat(taxonomyUnitService.findByName(kingdom.name)!!.children).contains(phylum.name)
			assertThat(taxonomyUnitService.findByName(phylum.name)!!.parent).isEqualTo(kingdom.name)
		}

		@Test
		@DisplayName("Throws when taxonomy unit's name is not unique")
		fun `throws when taxonomy unit's name is not unique`() {

			taxonomyUnitService.create(root)

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { taxonomyUnitService.create(root) }
				.withMessage(FailReport.existingTaxonomyUnit(root.name))
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws when taxonomy unit name or parent are not valid strings")
		@ValueSource(strings = [INVALID_SHORT_NAME, INVALID_LONG_NAME])
		fun `throws when taxonomy unit name or parent are not valid strings`(invalidName: String) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { taxonomyUnitService.create(root.copy(name = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { taxonomyUnitService.create(root.copy(parent = invalidName)) }
				.withMessageContaining(FailReport.invalidName())
		}

		@Test
		@DisplayName("Throws when association fails due to parent taxonomy unit missing")
		fun `throws when association fails due to parent taxonomy unit missing`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { taxonomyUnitService.create(phylum) }
				.withMessage(entityNotFound(entity = "Taxonomy unit", idType = "name", id = phylum.parent))
		}

		@Test
		@DisplayName("Throws when association fails due to parent taxonomy unit misspelled")
		fun `throws when association fails due to parent taxonomy unit misspelled`() {

			taxonomyUnitService.create(root)
			taxonomyUnitService.create(kingdom)

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { taxonomyUnitService.create(phylum.copy(parent = MiSPELLED)) }
				.withMessage(entityNotFound(entity = "Taxonomy unit", idType = "name", id = MiSPELLED))
		}

		@Test
		@DisplayName("Successfully creates chain of valid taxonomy units")
		fun `successfully creates chain of valid taxonomy units`() {
			persistTaxonomyUnits().also { assertThat(it.size).isEqualTo(validGraphOfTaxonomyUnits.size) }
		}

		@Test
		@DisplayName("Throws while creating chain of wrongly ordered taxonomy units")
		fun `throws while creating chain of wrongly ordered taxonomy units`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { taxonomyUnitService.createMany(invalidGraphOfTaxonomyUnits) }
		}
	}

	@Nested
	@DisplayName("Taxonomy unit retrieval tests")
	inner class TaxonomyUnitRetrieval {

		@Test
		@DisplayName("Retrieves all taxonomy units")
		fun `retrieves all taxonomy units`() {
			persistTaxonomyUnits()
			assertThat(taxonomyUnitService.findAll().size).isEqualTo(validGraphOfTaxonomyUnits.size)
		}

		@Test
		@DisplayName("Retrieves all animals")
		fun `retrieves all animals`() {

			taxonomyUnitService.createMany(
				listOf(
					root,
					kingdom,
					phylum.copy(name = ANDEAN_BEAR),
					phylum.copy(name = GRIZZLY_BEAR),
					phylum.copy(name = AMUR_TIGER)
				)
			)

			val animals = taxonomyUnitService.findAllAnimals()

			assertThat(animals.find { animal -> animal.name == ANDEAN_BEAR }).isNotNull
			assertThat(animals.find { animal -> animal.name == GRIZZLY_BEAR }).isNotNull
			assertThat(animals.find { animal -> animal.name == AMUR_TIGER }).isNotNull
			assertThat(animals.find { animal -> animal.name == root.name }).isNull()
			assertThat(animals.find { animal -> animal.name == kingdom.name }).isNull()
			assertThat(animals[0].children).isEmpty()
			assertThat(animals[1].children).isEmpty()
			assertThat(animals[2].children).isEmpty()
		}

		@Test
		@DisplayName("Returns the parent of a child")
		fun `returns the parent of a child`() {

			taxonomyUnitService.createMany(validGraphOfTaxonomyUnits)

			assertThat(taxonomyUnitService.findParentOf(kingdom.name).name).isEqualTo(root.name)
			assertThat(taxonomyUnitService.findParentOf(phylum.name).name).isEqualTo(kingdom.name)
		}

		@Test
		@DisplayName("Retrieves the children of a parent")
		fun `retrieves the children of a parent`() {
			persistTaxonomyUnits(carnivoreTU, andeanBearTU, grizzlyBearTU, amurTigerTU)
				.let { taxonomyUnitService.findChildrenOf(carnivoreTU.name) }
				.run {
					val children = taxonomyUnitService.findByName(carnivoreTU.name)!!.children
					assertThat(children).isEqualTo(this.map { child -> child.name }.toMutableSet())
				}
		}

		@Test
		@DisplayName("Throws if child is missing or child name is misspelled")
		fun `throws if child is missing or child name is misspelled`() {
			taxonomyUnitService.createMany(validGraphOfTaxonomyUnits)

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { taxonomyUnitService.findParentOf(MiSPELLED) }
				.withMessage(entityNotFound(entity = "Taxonomy unit", idType = "name", id = MiSPELLED))
		}
	}
}



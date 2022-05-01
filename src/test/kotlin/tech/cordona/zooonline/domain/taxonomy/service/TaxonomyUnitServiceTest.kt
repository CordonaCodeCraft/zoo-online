package tech.cordona.zooonline.domain.taxonomy.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.SoftAssertions
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
import tech.cordona.zooonline.common.TestAssets.amurTiger
import tech.cordona.zooonline.common.TestAssets.andeanBear
import tech.cordona.zooonline.common.TestAssets.grizzlyBear
import tech.cordona.zooonline.common.TestAssets.invalidChainOfUnits
import tech.cordona.zooonline.common.TestAssets.invalidLongName
import tech.cordona.zooonline.common.TestAssets.invalidShortName
import tech.cordona.zooonline.common.TestAssets.kingdom
import tech.cordona.zooonline.common.TestAssets.misspelled
import tech.cordona.zooonline.common.TestAssets.phylum
import tech.cordona.zooonline.common.TestAssets.root
import tech.cordona.zooonline.common.TestAssets.validChainOfUnits
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.validation.FailReport
import tech.cordona.zooonline.validation.FailReport.entityNotFound

internal class TaxonomyUnitServiceTest(@Autowired private val service: TaxonomyUnitService) : PersistenceTest() {

	@BeforeEach
	fun beforeEach() = service.deleteAll()

	@Nested
	@DisplayName("Taxonomy unit creation tests")
	@TestInstance(PER_CLASS)
	inner class TaxonomyUnitCreation {

		@Test
		@DisplayName("Successfully creates and associates properly chained taxonomy units")
		fun `Successfully saves and associates properly chained taxonomy units`() {

			service.createMany(validChainOfUnits)

			SoftAssertions()
				.apply {
					assertThat(service.findByName(root.name)!!.children).contains(kingdom.name)
					assertThat(service.findByName(kingdom.name)!!.parent).isEqualTo(root.name)
					assertThat(service.findByName(kingdom.name)!!.children).contains(phylum.name)
					assertThat(service.findByName(phylum.name)!!.parent).isEqualTo(kingdom.name)
				}
				.assertAll()
		}

		@Test
		@DisplayName("Throws when taxonomy unit's name is not unique")
		fun `throws when taxonomy unit's name is not unique`() {

			service.create(root)

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { service.create(root) }
				.withMessage(FailReport.existingTaxonomyUnit(root.name))
		}

		@ParameterizedTest(name = "Invalid name: {arguments}")
		@DisplayName("Throws when taxonomy unit name or parent are not valid strings")
		@ValueSource(strings = [invalidShortName, invalidLongName])
		fun `throws when taxonomy unit name or parent are not valid strings`(invalidName: String) {
			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { service.create(root.copy(name = invalidName)) }
				.withMessageContaining(FailReport.invalidName())

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { service.create(root.copy(parent = invalidName)) }
				.withMessageContaining(FailReport.invalidName())
		}

		@Test
		@DisplayName("Throws when association fails due to parent taxonomy unit missing")
		fun `throws when association fails due to parent taxonomy unit missing`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { service.create(phylum) }
				.withMessage(entityNotFound(entity = "Taxonomy unit", idType = "name", id = phylum.parent))
		}

		@Test
		@DisplayName("Throws when association fails due to parent taxonomy unit misspelled")
		fun `throws when association fails due to parent taxonomy unit misspelled`() {

			service.create(root)
			service.create(kingdom)

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { service.create(phylum.copy(parent = misspelled)) }
				.withMessage(entityNotFound(entity = "Taxonomy unit", idType = "name", id = misspelled))
		}

		@Test
		@DisplayName("Successfully creates chain of valid taxonomy units")
		fun `successfully creates chain of valid taxonomy units`() {
			service.createMany(validChainOfUnits).also { assertThat(it.size).isEqualTo(validChainOfUnits.size) }
		}

		@Test
		@DisplayName("Throws while creating chain of wrongly ordered taxonomy units")
		fun `throws while creating chain of wrongly ordered taxonomy units`() {
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { service.createMany(invalidChainOfUnits) }
		}
	}

	@Nested
	@DisplayName("Taxonomy unit retrieval tests")
	@TestInstance(PER_CLASS)
	inner class TaxonomyUnitRetrieval {

		@Test
		@DisplayName("Retrieves all taxonomy units")
		fun `retrieves all taxonomy units`() {
			service.createMany(validChainOfUnits)
			assertThat(service.findAll().size).isEqualTo(validChainOfUnits.size)
		}

		@Test
		@DisplayName("Retrieves all animals")
		fun `retrieves all animals`() {

			service.createMany(
				listOf(
					root,
					kingdom,
					phylum.copy(name = andeanBear),
					phylum.copy(name = grizzlyBear),
					phylum.copy(name = amurTiger)
				)
			)

			val animals = service.findAllAnimals()

			SoftAssertions()
				.apply {
					assertThat(animals.find { animal -> animal.name == andeanBear }).isNotNull
					assertThat(animals.find { animal -> animal.name == grizzlyBear }).isNotNull
					assertThat(animals.find { animal -> animal.name == amurTiger }).isNotNull
					assertThat(animals.find { animal -> animal.name == root.name }).isNull()
					assertThat(animals.find { animal -> animal.name == kingdom.name }).isNull()
					assertThat(animals[0].children).isEmpty()
					assertThat(animals[1].children).isEmpty()
					assertThat(animals[2].children).isEmpty()
				}
				.assertAll()
		}

		@Test
		@DisplayName("Returns the parent of a child")
		fun `returns the parent of a child`() {

			service.createMany(validChainOfUnits)

			SoftAssertions()
				.apply {
					Assertions.assertThat(service.findParentOf(kingdom.name).name).isEqualTo(root.name)
					Assertions.assertThat(service.findParentOf(phylum.name).name).isEqualTo(kingdom.name)
				}
				.assertAll()
		}

		@Test
		@DisplayName("Throws if child is missing or child name is misspelled")
		fun `throws if child is missing or child name is misspelled`() {
			service.createMany(validChainOfUnits)

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { service.findParentOf(misspelled) }
				.withMessage(entityNotFound(entity = "Taxonomy unit", idType = "name", id = misspelled))
		}
	}
}



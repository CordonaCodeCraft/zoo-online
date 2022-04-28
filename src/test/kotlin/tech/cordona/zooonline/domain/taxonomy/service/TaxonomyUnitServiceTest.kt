package tech.cordona.zooonline.domain.taxonomy.service

import exceptions.EntityNotFoundException
import exceptions.InvalidEntityException
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
import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer.Companion.ROOT
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

internal class TaxonomyUnitServiceTest(@Autowired private val service: TaxonomyUnitService) : PersistenceTest() {

	@Nested
	@DisplayName("Taxonomy unit creation tests")
	@TestInstance(PER_CLASS)
	inner class TaxonomyUnitCreation {

		@BeforeEach
		fun beforeEach() = service.deleteAll()

		@Test
		@DisplayName("Successfully saves and associates properly chained taxonomy units")
		fun `Successfully saves and associates properly chained taxonomy units`() {

			val root = service.create(root)
			val parent = service.create(parent)
			val child = service.create(child)

			SoftAssertions()
				.apply {
					assertThat(service.findByName(root.name)!!.children).contains(parent.name)
					assertThat(service.findByName(parent.name)!!.parent).isEqualTo(root.name)
					assertThat(service.findByName(parent.name)!!.children).contains(child.name)
					assertThat(service.findByName(child.name)!!.parent).isEqualTo(parent.name)
				}
				.assertAll()
		}

		@Test
		@DisplayName("Throws when taxonomy unit's name is not unique")
		fun `throws when taxonomy unit's name is not unique`() {

			service.create(root)

			assertThatExceptionOfType(InvalidEntityException::class.java)
				.isThrownBy { service.create(root) }
				.withMessage("Taxonomy unit with name: ${root.name} already exists")
		}

		@Test
		@DisplayName("Throws when association fails due to parent taxonomy unit missing")
		fun `throws when association fails due to parent taxonomy unit missing`() {

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy {
					service.create(child)
				}
				.withMessage("Parent taxonomy unit with name: ${child.parent} is wrong or does not exist")
		}

		@Test
		@DisplayName("Throws when association fails due to parent taxonomy unit misspelled")
		fun `throws when association fails due to parent taxonomy unit misspelled`() {

			service.create(root)
			service.create(parent)

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy {
					service.create(child.copy(parent = misspelled))
				}
				.withMessage("Parent taxonomy unit with name: $misspelled is wrong or does not exist")
		}


		@Test
		@DisplayName("Successfully creates chain of valid taxonomy units")
		fun `Successfully creates list of chain taxonomy units`() {
			service.createMany(listOf(root, parent, child)).also { assertThat(it.size).isEqualTo(3) }
		}

		@Test
		@DisplayName("Throws while creating chain of wrongly ordered taxonomy units")
		fun `throws while creating chain of wrongly ordered taxonomy units`() {

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy {
					service.createMany(listOf(root, child, parent))
				}
		}
	}

	@Nested
	@DisplayName("Taxonomy unit retrieval tests")
	@TestInstance(PER_CLASS)
	inner class TaxonomyUnitRetrieval {

		@BeforeEach
		fun beforeEach() = service.deleteAll()


		@Test
		@DisplayName("Retrieves all taxonomy units")
		fun `retrieves all taxonomy units`() {
			service.createMany(listOf(root, parent, child))
			assertThat(service.findAll().size).isEqualTo(3)
		}

		@Test
		@DisplayName("Retrieves all animals")
		fun `retrieves all animals`() {

			service.createMany(
				listOf(
					root,
					parent,
					child.copy(name = firstAnimal),
					child.copy(name = secondAnimal),
					child.copy(name = thirdAnimal)
				)
			)

			val animals = service.findAllAnimals()

			SoftAssertions()
				.apply {
					assertThat(animals.find { animal -> animal.name == firstAnimal }).isNotNull
					assertThat(animals.find { animal -> animal.name == secondAnimal }).isNotNull
					assertThat(animals.find { animal -> animal.name == thirdAnimal }).isNotNull
					assertThat(animals.find { animal -> animal.name == root.name }).isNull()
					assertThat(animals.find { animal -> animal.name == parent.name }).isNull()
					assertThat(animals[0].children).isEmpty()
					assertThat(animals[1].children).isEmpty()
					assertThat(animals[2].children).isEmpty()
				}
				.assertAll()
		}

		@Test
		@DisplayName("Returns the parent of a child")
		fun `returns the parent of a child`() {

			val root = service.create(root)
			val parent = service.create(parent)
			val child = service.create(child)

			SoftAssertions()
				.apply {
					Assertions.assertThat(service.findParentOf(parent.name).name).isEqualTo(root.name)
					Assertions.assertThat(service.findParentOf(child.name).name).isEqualTo(parent.name)
				}
				.assertAll()
		}

		@Test
		@DisplayName("Throws if child is missing or child name is misspelled")
		fun `throws if child is missing or child name is misspelled`() {
			service.createMany(listOf(root, parent, child))

			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { service.findParentOf(misspelled) }
				.withMessage("Child taxonomy unit with name: $misspelled is wrong or does not exist")
		}
	}

	companion object {
		const val firstAnimal = "First animal"
		const val secondAnimal = "Second animal"
		const val thirdAnimal = "Third animal"
		const val misspelled = "Misspelled"
		val root = TaxonomyUnit(name = "Root", parent = ROOT, children = mutableSetOf())
		val parent = TaxonomyUnit(name = "Parent", parent = "Root", children = mutableSetOf())
		val child = TaxonomyUnit(name = "Child", parent = "Parent", children = mutableSetOf())
	}
}



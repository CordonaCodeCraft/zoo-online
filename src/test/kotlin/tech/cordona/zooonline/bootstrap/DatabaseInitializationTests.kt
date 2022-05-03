package tech.cordona.zooonline.bootstrap

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.PersistenceTest
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.doctor.service.DoctorService
import tech.cordona.zooonline.domain.guard.service.GuardService
import tech.cordona.zooonline.domain.taxonomy.enums.Amphibian
import tech.cordona.zooonline.domain.taxonomy.enums.Bird
import tech.cordona.zooonline.domain.taxonomy.enums.Insect
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal
import tech.cordona.zooonline.domain.taxonomy.enums.Reptile
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.domain.trainer.service.TrainerService
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.security.user.service.UserService

@TestInstance(PER_CLASS)

class DatabaseInitializationTests(
	@Autowired private val taxonomyUnitService: TaxonomyUnitService,
	@Autowired private val animalService: AnimalService,
	@Autowired private val cellService: CellService,
	@Autowired private val areaService: AreaService,
	@Autowired private val userService: UserService,
	@Autowired private val trainerService: TrainerService,
	@Autowired private val doctorService: DoctorService,
	@Autowired private val guardService: GuardService
) : PersistenceTest() {

	@BeforeAll
	fun beforeAll() {
		DBInitializer(
			taxonomyUnitService,
			animalService,
			cellService,
			areaService,
			userService,
			trainerService,
			doctorService,
			guardService
		).initializeDatabase()
	}

	@TestInstance(PER_CLASS)
	@Nested
	@DisplayName("Taxonomy units tests")
	inner class TaxonomyUnitsTests {

		@Test
		@DisplayName("Persists all taxonomy units")
		fun `persists all taxonomy units`() {
			assertThat(taxonomyUnitService.findAll().size).isEqualTo(TAXONOMY_UNITS_TOTAL)
		}

		@ParameterizedTest(name = "Base taxonomy unit: {arguments}")
		@ValueSource(strings = ["Eukaryote", "Animalia", "Animal"])
		@DisplayName("Persists all base taxonomy units")
		fun `persists all base taxonomy units`(name: String) {
			assertIsPersistedAndContainsCorrectChildren(name)
		}

		@ParameterizedTest(name = "Mammal: {arguments}")
		@EnumSource(value = Mammal::class)
		@DisplayName("Persists all mammals")
		fun `persists all mammals`(mammal: Mammal) {
			assertIsPersistedAndContainsCorrectChildren(mammal.name.asTitlecase())
			mammal.species.forEach { specie -> assertIsPersisted(specie) }
		}

		@ParameterizedTest(name = "Bird: {arguments}")
		@EnumSource(value = Bird::class)
		@DisplayName("Persists all birds")
		fun `persists all birds`(bird: Bird) {
			assertIsPersistedAndContainsCorrectChildren(bird.name.asTitlecase())
			bird.species.forEach { specie -> assertIsPersisted(specie) }
		}

		@ParameterizedTest(name = "Reptile: {arguments}")
		@EnumSource(value = Reptile::class)
		@DisplayName("Persists all reptiles")
		fun `persists all reptiles`(reptile: Reptile) {
			assertIsPersistedAndContainsCorrectChildren(reptile.name.asTitlecase())
			reptile.species.forEach {specie -> assertIsPersisted(specie) }
		}

		@ParameterizedTest(name = "Insect: {arguments}")
		@EnumSource(value = Insect::class)
		@DisplayName("Persists all insects")
		fun `persists all insects`(insect: Insect) {
			assertIsPersistedAndContainsCorrectChildren(insect.name.asTitlecase())
			insect.species.forEach { specie -> assertIsPersisted(specie) }
		}

		@ParameterizedTest(name = "Amphibian: {arguments}")
		@EnumSource(value = Amphibian::class)
		@DisplayName("Persists all amphibians")
		fun `persists all amphibians`(amphibian: Amphibian) {
			assertIsPersistedAndContainsCorrectChildren(amphibian.name.asTitlecase())
			amphibian.species.forEach { specie -> assertIsPersisted(specie) }
		}
	}

	@TestInstance(PER_CLASS)
	@Nested
	@DisplayName("Cells tests")
	inner class CellsTests {

		@Test
		@DisplayName("Persists cells for all species")
		fun `persists cells for all species`() {
			assertThat(cellService.findAll().size).isEqualTo(taxonomyUnitService.findAllAnimals().size)
		}

		@ParameterizedTest(name = "Mammal: {arguments}")
		@EnumSource(value = Mammal::class)
		@DisplayName("Persists cells for all mammals")
		fun `persists cells for all mammals`(mammal: Mammal) {
			mammal.species.forEach { specie ->
				assertCellIsPersistedWithCorrectDetailsAndAnimals(specie, mammal.name.asTitlecase())
			}
		}

		@ParameterizedTest(name = "Bird: {arguments}")
		@EnumSource(value = Bird::class)
		@DisplayName("Persists cells for all birds")
		fun `persists cells for all birds`(bird: Bird) {
			bird.species.forEach { specie ->
				assertCellIsPersistedWithCorrectDetailsAndAnimals(specie, bird.name.asTitlecase())
			}
		}

		@ParameterizedTest(name = "Reptile: {arguments}")
		@EnumSource(value = Reptile::class)
		@DisplayName("Persists cells for all reptiles")
		fun `persists cells for all reptiles`(reptile: Reptile) {
			reptile.species.forEach { specie ->
				assertCellIsPersistedWithCorrectDetailsAndAnimals(specie, reptile.name.asTitlecase())
			}
		}

		@ParameterizedTest(name = "Insect: {arguments}")
		@EnumSource(value = Insect::class)
		@DisplayName("persists cells for all insects")
		fun `persists cells for all insects`(insect: Insect) {
			insect.species.forEach { specie ->
				assertCellIsPersistedWithCorrectDetailsAndAnimals(specie, insect.name.asTitlecase())
			}
		}

		@ParameterizedTest(name = "Amphibian: {arguments}")
		@EnumSource(value = Amphibian::class)
		@DisplayName("Persists cells for all amphibians")
		fun `persists cells for all amphibians`(amphibian: Amphibian) {
			amphibian.species.forEach { specie ->
				assertCellIsPersistedWithCorrectDetailsAndAnimals(specie, amphibian.name.asTitlecase())
			}
		}
	}

	@TestInstance(PER_CLASS)
	@Nested
	@DisplayName("Area tests")
	inner class AreaTests {
		@Test
		@DisplayName("Persists all areas")
		fun `persists all areas`() {
			val groupsCount =
				Mammal.values().size + Bird.values().size + Reptile.values().size + Insect.values().size + Amphibian.values().size
			assertThat(areaService.findAll().size).isEqualTo(groupsCount)
		}

		@ParameterizedTest(name = "Mammal: {arguments}")
		@EnumSource(value = Mammal::class)
		@DisplayName("Persists areas for all mammals")
		fun `persists areas for all mammals`(mammal: Mammal) {
			areaService.findAreaByName(mammal.name.asTitlecase())
				.run {
					assertCorrectCells(this)
					assertCorrectStaff(this)
				}
		}

		@ParameterizedTest(name = "Bird: {arguments}")
		@EnumSource(value = Bird::class)
		@DisplayName("Persists area for all birds")
		fun `persists areas for all birds`(bird: Bird) {
			areaService.findAreaByName(bird.name.asTitlecase())
				.run {
					assertCorrectCells(this)
					assertCorrectStaff(this)
				}
		}

		@ParameterizedTest(name = "Reptile: {arguments}")
		@EnumSource(value = Reptile::class)
		@DisplayName("Persists area for all reptiles")
		fun `persists areas for all reptiles`(reptile: Reptile) {
			areaService.findAreaByName(reptile.name.asTitlecase())
				.run {
					assertCorrectCells(this)
					assertCorrectStaff(this)
				}
		}

		@ParameterizedTest(name = "Insect: {arguments}")
		@EnumSource(value = Insect::class)
		@DisplayName("Persists area for all insects")
		fun `persists areas for all insects`(insect: Insect) {
			areaService.findAreaByName(insect.name.asTitlecase())
				.run {
					assertCorrectCells(this)
					assertCorrectStaff(this)
				}
		}

		@ParameterizedTest(name = "Amphibian: {arguments}")
		@EnumSource(value = Amphibian::class)
		@DisplayName("Persists area for all amphibians")
		fun `persists areas for all amphibians`(amphibian: Amphibian) {
			areaService.findAreaByName(amphibian.name.asTitlecase())
				.run {
					assertCorrectCells(this)
					assertCorrectStaff(this)
				}
		}
	}


	private fun assertCorrectCells(area: Area) {
		val expected = area.cells
		val actual = cellService.findAllByAnimalType(area.name).map { it.id }.toSet()
		assertThat(expected).isEqualTo(actual)
	}

	private fun assertCorrectStaff(area: Area) {
		area.staff.trainers
			.run {
				val trainer = trainerService.findByTrainerId(this.first().toString())
				assertThat(this.size).isEqualTo(1)
				assertThat(trainer.area).isEqualTo(area.name)
			}
			.also {
				area.staff.doctors
					.run {
						val doctor = doctorService.findByDoctorId(this.first().toString())
						assertThat(this.size).isEqualTo(1)
						assertThat(doctor.area).isEqualTo(area.name)
					}
			}.also {
				area.staff.guards
					.run {
						val guard = guardService.findByGuardId(this.first().toString())
						assertThat(this.size).isEqualTo(1)
						assertThat(guard.area).isEqualTo(area.name)
					}
			}
	}

	private fun assertIsPersistedAndContainsCorrectChildren(name: String) {
		assertIsPersisted(name).also { assertCorrectChildren(name) }

	}

	private fun assertIsPersisted(name: String) {
		assertThat(taxonomyUnitService.findByName(name)!!.name).isEqualTo(name)
	}

	private fun assertCorrectChildren(parent: String) {
		val expected = taxonomyUnitService.findByName(parent)!!.children
		val actual = taxonomyUnitService.findChildrenOf(parent).map { it.name }.toMutableSet()
		assertThat(expected).isEqualTo(actual)
	}

	private fun assertCellIsPersistedWithCorrectDetailsAndAnimals(specie: String, type: String) {
		cellService.findCellBySpecie(specie)
			.run {
				assertThat(this!!.specie).isEqualTo(specie)
				assertThat(this.animalType).isEqualTo(type)
				assertThat(this.animalGroup).isEqualTo(taxonomyUnitService.findParentOf(this.animalType).name)
				assertThat(this.species).isEqualTo(animalService.findAllBySpecie(specie).map { it.id }.toMutableSet())
			}
	}

	companion object {
		const val TAXONOMY_UNITS_TOTAL = 88
	}
}
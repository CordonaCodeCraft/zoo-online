package tech.cordona.zooonline.bootstrap.builders.animal

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.entity.Animal.Gender.MALE
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.HealthStatus.HEALTHY
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.HealthStatus.SICK
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.TrainingStatus.TRAINED
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.TrainingStatus.UNTRAINED
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Group.AMPHIBIAN
import tech.cordona.zooonline.domain.taxonomy.enums.Group.BIRD
import tech.cordona.zooonline.domain.taxonomy.enums.Group.MAMMAL
import tech.cordona.zooonline.domain.taxonomy.enums.Group.REPTILE
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceImpl
import java.util.*
import kotlin.random.Random

object AnimalBuilder {
	private const val NAMES_PATH =
		"/Users/cordona/IdeaProjects/testcontainers-demo/zoo-online/src/main/resources/names/"
	private const val MALE_NAMES_PATH = "${NAMES_PATH}maleNames.csv"
	private const val FEMALE_NAMES_PATH = "${NAMES_PATH}femaleNames.csv"

	private val maleNames = getNames(MALE_NAMES_PATH)
	private val femaleNames = getNames(FEMALE_NAMES_PATH)

	fun buildAnimals(taxonomyUnit: TaxonomyUnit, taxonomyUnitService: TaxonomyUnitServiceImpl): List<Animal> {
		val parent = taxonomyUnitService.findParentOf(taxonomyUnit.parent)
		val count = getCount(parent.name)
		val animals = mutableListOf<Animal>()
		for (i in 1..count) {
			animals.add(buildAnimal(taxonomyUnit, parent))
		}
		return animals
	}

	private fun buildAnimal(taxonomyUnit: TaxonomyUnit, parent: TaxonomyUnit): Animal {
		val gender = Animal.Gender.values()[Random.nextInt(0, 2)].asString
		val animalName = if (gender == MALE.asString) maleNames.pop() else femaleNames.pop()
		val age = getAge(parent.name)
		val weight = getWeight(parent.name)

		val healthPoints = Random.nextInt(1, 11)
		val healthStatus = if (healthPoints <= 5) SICK.asString else HEALTHY.asString

		val trainingPoints = Random.nextInt(1, 11)
		val trainingStatus = if (trainingPoints <= 5) UNTRAINED.asString else TRAINED.asString

		val healthStatistics = HealthStatistics(healthPoints, healthStatus, trainingPoints, trainingStatus)

		val url = "https://peshoIsGreat.org"

		return Animal(animalName, age, weight, gender, taxonomyUnit, healthStatistics, url)
	}

	private fun getNames(path: String): ArrayDeque<String> {
		return ArrayDeque(
			csvReader().open(path) {
				readAllAsSequence()
					.map { row -> row[2] }
					.toList()
			}
		)
	}

	private fun getCount(parent: String): Int {
		return when (parent) {
			MAMMAL.asString -> Random.nextInt(2, 6)
			BIRD.asString -> Random.nextInt(2, 11)
			REPTILE.asString -> Random.nextInt(2, 6)
			AMPHIBIAN.asString -> Random.nextInt(2, 11)
			else -> Random.nextInt(2, 21)
		}
	}

	private fun getWeight(parent: String): Double {
		return when (parent) {
			MAMMAL.asString -> Random.nextDouble(5.0, 60.0)
			BIRD.asString -> Random.nextDouble(0.1, 5.0)
			REPTILE.asString -> Random.nextDouble(0.5, 35.0)
			AMPHIBIAN.asString -> Random.nextDouble(0.2, 8.0)
			else -> Random.nextDouble(0.01, 0.2)
		}
	}

	private fun getAge(parent: String): Int {
		return when (parent) {
			MAMMAL.asString -> Random.nextInt(1, 21)
			BIRD.asString -> Random.nextInt(1, 6)
			REPTILE.asString -> Random.nextInt(1, 11)
			AMPHIBIAN.asString -> Random.nextInt(1, 4)
			else -> Random.nextInt(1, 2)
		}
	}
}
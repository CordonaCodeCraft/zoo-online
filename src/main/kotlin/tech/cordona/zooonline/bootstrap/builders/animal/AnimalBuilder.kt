package tech.cordona.zooonline.bootstrap.builders.animal

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.entity.Gender
import tech.cordona.zooonline.domain.animal.entity.Gender.MALE
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
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import java.util.*
import kotlin.random.Random

object AnimalBuilder {
	private const val NAMES_PATH =
		"/Users/cordona/IdeaProjects/testcontainers-demo/zoo-online/src/main/resources/names"
	private const val MALE_NAMES_PATH = "$NAMES_PATH/maleNames.csv"
	private const val FEMALE_NAMES_PATH = "$NAMES_PATH/femaleNames.csv"

	private val maleNames = getNames(MALE_NAMES_PATH)
	private val femaleNames = getNames(FEMALE_NAMES_PATH)

	fun buildAnimals(taxonomyUnit: TaxonomyUnit, taxonomyUnitService: TaxonomyUnitService) =
		taxonomyUnitService.findParentOf(taxonomyUnit.parent).let { parent ->
			(1..getCount(parent.name)).map { buildAnimal(taxonomyUnit, parent) }
		}

	private fun buildAnimal(taxonomyUnit: TaxonomyUnit, parent: TaxonomyUnit): Animal {
		val gender = Gender.values()[Random.nextInt(0, 2)].asString
		val healthPoints = Random.nextInt(1, 11)
		val trainingPoints = Random.nextInt(1, 11)
		return Animal(
			gender = gender,
			name = if (gender == MALE.asString) maleNames.pop() else femaleNames.pop(),
			age = getAge(parent.name),
			weight = getWeight(parent.name),
			url = "https://peshoIsGreat.org",
			taxonomyDetails = taxonomyUnit,
			healthStatistics = HealthStatistics(
				healthPoints = healthPoints,
				healthStatus = if (healthPoints <= 5) SICK.asString else HEALTHY.asString,
				trainingPoints = trainingPoints,
				trainingStatus = if (trainingPoints <= 5) UNTRAINED.asString else TRAINED.asString
			)
		)
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

	private fun getCount(parent: String) = when (parent) {
		MAMMAL.asString -> Random.nextInt(2, 6)
		BIRD.asString -> Random.nextInt(2, 11)
		REPTILE.asString -> Random.nextInt(2, 6)
		AMPHIBIAN.asString -> Random.nextInt(2, 11)
		else -> Random.nextInt(2, 21)
	}

	private fun getWeight(parent: String) = when (parent) {
		MAMMAL.asString -> Random.nextDouble(5.0, 60.0)
		BIRD.asString -> Random.nextDouble(0.1, 5.0)
		REPTILE.asString -> Random.nextDouble(0.5, 35.0)
		AMPHIBIAN.asString -> Random.nextDouble(0.2, 8.0)
		else -> Random.nextDouble(0.01, 0.2)
	}

	private fun getAge(parent: String) = when (parent) {
		MAMMAL.asString -> Random.nextInt(1, 21)
		BIRD.asString -> Random.nextInt(1, 6)
		REPTILE.asString -> Random.nextInt(1, 11)
		AMPHIBIAN.asString -> Random.nextInt(1, 4)
		else -> Random.nextInt(1, 2)
	}
}
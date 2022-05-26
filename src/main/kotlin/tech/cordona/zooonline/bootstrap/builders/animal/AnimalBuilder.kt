package tech.cordona.zooonline.bootstrap.builders.animal

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.entity.enums.Gender
import tech.cordona.zooonline.domain.animal.entity.enums.Gender.MALE
import tech.cordona.zooonline.domain.animal.entity.enums.HealthStatus.HEALTHY
import tech.cordona.zooonline.domain.animal.entity.enums.HealthStatus.SICK
import tech.cordona.zooonline.domain.animal.entity.enums.TrainingStatus.TRAINED
import tech.cordona.zooonline.domain.animal.entity.enums.TrainingStatus.UNTRAINED
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.Companion.SICK_THRESHOLD
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.Companion.UNTRAINED_THRESHOLD
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Group.AMPHIBIAN
import tech.cordona.zooonline.domain.taxonomy.enums.Group.BIRD
import tech.cordona.zooonline.domain.taxonomy.enums.Group.MAMMAL
import tech.cordona.zooonline.domain.taxonomy.enums.Group.REPTILE
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_WEIGHT
import java.util.*
import kotlin.random.Random

object AnimalBuilder {
	private const val NAMES_PATH =
		"/Users/cordona/IdeaProjects/zoo-online/src/main/resources/names"
	private const val MALE_NAMES_PATH = "$NAMES_PATH/maleNames.csv"
	private const val FEMALE_NAMES_PATH = "$NAMES_PATH/femaleNames.csv"

	private val maleNames = getNames(MALE_NAMES_PATH)
	private val femaleNames = getNames(FEMALE_NAMES_PATH)

	fun buildAnimals(taxonomyUnit: TaxonomyUnit, taxonomyUnitService: TaxonomyUnitService) =
		taxonomyUnitService.findParentOf(taxonomyUnit.parent).let { parent ->
			(1..getCount(parent.name)).map { buildAnimal(taxonomyUnit, parent) }
		}

	private fun buildAnimal(taxonomyUnit: TaxonomyUnit, parent: TaxonomyUnit): Animal {
		val gender = Gender.values()[Random.nextInt(0, 2)].name.asTitlecase()
		val healthPoints = Random.nextInt(1, MAX_HEALTH_POINTS + 1)
		val trainingPoints = Random.nextInt(1, MAX_TRAINING_POINTS + 1)
		return Animal(
			gender = gender,
			name = if (gender == MALE.name.asTitlecase()) maleNames.pop() else femaleNames.pop(),
			age = getAge(parent.name),
			weight = getWeight(parent.name),
			url = "https://peshoIsGreat.org",
			taxonomyDetails = taxonomyUnit,
			healthStatistics = HealthStatistics(
				healthPoints = healthPoints,
				healthStatus = if (healthPoints <= SICK_THRESHOLD) SICK.name.asTitlecase() else HEALTHY.name.asTitlecase(),
				trainingPoints = trainingPoints,
				trainingStatus = if (trainingPoints <= UNTRAINED_THRESHOLD) UNTRAINED.name.asTitlecase() else TRAINED.name.asTitlecase()
			)
		)
	}

	private fun getNames(path: String): ArrayDeque<String> = ArrayDeque(
		csvReader().open(path) {
			readAllAsSequence()
				.map { row -> row[2] }
				.toList()
		}
	)

	private fun getCount(parent: String) = when (parent) {
		MAMMAL.name.asTitlecase() -> Random.nextInt(2, 6)
		BIRD.name.asTitlecase() -> Random.nextInt(2, 11)
		REPTILE.name.asTitlecase() -> Random.nextInt(2, 6)
		AMPHIBIAN.name.asTitlecase() -> Random.nextInt(2, 11)
		else -> Random.nextInt(2, 21)
	}

	private fun getWeight(parent: String) = when (parent) {
		MAMMAL.name.asTitlecase() -> Random.nextDouble(5.0, MAX_WEIGHT)
		BIRD.name.asTitlecase() -> Random.nextDouble(0.1, 5.0)
		REPTILE.name.asTitlecase() -> Random.nextDouble(0.5, 35.0)
		AMPHIBIAN.name.asTitlecase() -> Random.nextDouble(0.2, 8.0)
		else -> Random.nextDouble(0.1, 0.2)
	}

	private fun getAge(parent: String) = when (parent) {
		MAMMAL.name.asTitlecase() -> Random.nextInt(1, MAX_AGE)
		BIRD.name.asTitlecase() -> Random.nextInt(1, 6)
		REPTILE.name.asTitlecase() -> Random.nextInt(1, 11)
		AMPHIBIAN.name.asTitlecase() -> Random.nextInt(1, 4)
		else -> Random.nextInt(1, 2)
	}
}
package tech.cordona.zooonline.bootstrap.objects.animal

import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy.Type.*
import tech.cordona.zooonline.domain.animals.entity.Animal
import tech.cordona.zooonline.domain.animals.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceImpl
import java.util.*
import kotlin.random.Random

object AnimalBuilder {

	fun buildAnimal(taxonomyUnit: TaxonomyUnit, taxonomyUnitService: TaxonomyUnitServiceImpl): Animal {
		val animalName = names.pop()
		val parent = taxonomyUnitService.findParentFor(taxonomyUnit.parent)
		val age = getAge(parent.name)
		val weight = getWeight(parent.name)
		val gender = Animal.Gender.values()[Random.nextInt(0, 2)].asString

		val healthPoints = Random.nextInt(1, 11)
		val healthStatus =
			if (healthPoints <= 5) HealthStatistics.HealthStatus.SICK.asString else HealthStatistics.HealthStatus.HEALTHY.asString

		val trainingPoints = Random.nextInt(1, 11)
		val trainingStatus =
			if (trainingPoints <= 5) HealthStatistics.TrainingStatus.UNTRAINED.asString else HealthStatistics.TrainingStatus.TRAINED.asString

		val healthStatistics = HealthStatistics(healthPoints, healthStatus, trainingPoints, trainingStatus)

		val url = "https://peshoIsGreat.org"

		return Animal(animalName, age, weight, gender, taxonomyUnit, healthStatistics, url)
	}

	private fun getWeight(parent: String): Double {
		return when (parent) {
			MAMMAL.asString -> Random.nextDouble(0.5, 60.0)
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

	private val names = ArrayDeque(
		listOf(
			"Michael",
			"Christopher",
			"Jessica",
			"Matthew",
			"Ashley",
			"Jennifer",
			"Joshua",
			"Amanda",
			"Daniel",
			"David",
			"James",
			"Robert",
			"John",
			"Joseph",
			"Andrew",
			"Ryan",
			"Brandon",
			"Jason",
			"Justin",
			"Sarah",
			"William",
			"Jonathan",
			"Stephanie",
			"Brian",
			"Nicole",
			"Nicholas",
			"Anthony",
			"Heather",
			"Eric",
			"Elizabeth",
			"Adam",
			"Megan",
			"Melissa",
			"Kevin",
			"Steven",
			"Thomas",
			"Timothy",
			"Christina",
			"Kyle",
			"Rachel",
			"Laura",
			"Lauren",
			"Amber",
			"Brittany",
			"Danielle",
			"Richard",
			"Kimberly",
			"Jeffrey",
			"Amy",
			"Crystal",
			"Michelle",
			"Tiffany",
			"Jeremy",
			"Benjamin",
			"Mark",
			"Emily",
			"Aaron",
			"Charles",
			"Rebecca",
			"Jacob",
			"Stephen",
			"Patrick",
			"Sean",
			"Erin",
			"Zachary",
			"Jamie",
			"Kelly",
			"Samantha",
			"Nathan",
			"Sara",
			"Dustin",
			"Paul",
			"Angela",
			"Tyler",
			"Scott",
			"Katherine",
			"Andrea",
			"Gregory",
			"Erica",
			"Mary",
			"Travis",
			"Lisa",
			"Kenneth",
			"Bryan",
			"Lindsey",
			"Kristen",
			"Jose",
			"Alexander",
			"Jesse",
			"Katie",
			"Lindsay",
			"Shannon",
			"Vanessa",
			"Courtney",
			"Christine",
			"Alicia",
		)
	)
}
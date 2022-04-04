package tech.cordona.zooonline.bootstrap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import tech.cordona.zooonline.bootstrap.objects.animal.AnimalData
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy
import tech.cordona.zooonline.domain.animals.entity.Animal
import tech.cordona.zooonline.domain.animals.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.animals.entity.structs.HealthStatistics.HealthStatus.HEALTHY
import tech.cordona.zooonline.domain.animals.entity.structs.HealthStatistics.HealthStatus.SICK
import tech.cordona.zooonline.domain.animals.entity.structs.HealthStatistics.TrainingStatus.TRAINED
import tech.cordona.zooonline.domain.animals.entity.structs.HealthStatistics.TrainingStatus.UNTRAINED
import tech.cordona.zooonline.domain.animals.service.AnimalServiceImpl
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceImpl
import kotlin.random.Random

@Component
class DatabaseInitializer @Autowired constructor(
	val taxonomyUnitService: TaxonomyUnitServiceImpl,
	val animalService: AnimalServiceImpl
) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {
		saveTaxonomyUnitsInDB()
		saveAnimalsInDB()
	}

	private fun saveAnimalsInDB() {
		val animals = taxonomyUnitService.getAllAnimals()
			.map { unit -> createAnimal(unit) }
			.toList()

		animalService.saveAll(animals)
	}

	private fun createAnimal(taxonomyUnit: TaxonomyUnit) : Animal {
		val name = AnimalData.names.pop()
		val age = Random.nextInt(1, 20)
		val weight = Random.nextDouble(0.3, 50.8)
		val gender = Animal.Gender.values()[Random.nextInt(0, 2)].asString

		val healthPoints = Random.nextInt(1, 11)
		val healthStatus = if(healthPoints <= 5) SICK.asString else HEALTHY.asString

		val trainingPoints = Random.nextInt(1, 11)
		val trainingStatus = if(trainingPoints <= 5) UNTRAINED.asString else TRAINED.asString

		val healthStatistics = HealthStatistics(healthPoints, healthStatus, trainingPoints, trainingStatus)

		val url = "https://peshoIsGreat.org"

		return Animal(name, age, weight, gender, taxonomyUnit, healthStatistics, url)
	}


	fun saveTaxonomyUnitsInDB() {
		taxonomyUnitService.saveAll(Taxonomy.getMammalSpecies())
		taxonomyUnitService.saveAll(Taxonomy.getBirdSpecies())
		taxonomyUnitService.saveAll(Taxonomy.getReptileSpecies())
		taxonomyUnitService.saveAll(Taxonomy.getInsectSpecies())
		taxonomyUnitService.saveAll(Taxonomy.getAmphibianSpecies())

		taxonomyUnitService.saveAll(Taxonomy.getMammals())
		taxonomyUnitService.saveAll(Taxonomy.getBirds())
		taxonomyUnitService.saveAll(Taxonomy.getReptiles())
		taxonomyUnitService.saveAll(Taxonomy.getInsects())
		taxonomyUnitService.saveAll(Taxonomy.getAmphibians())

		taxonomyUnitService.saveAll(
			listOf(
				Taxonomy.mammal,
				Taxonomy.bird,
				Taxonomy.reptile,
				Taxonomy.insect,
				Taxonomy.amphibian
			)
		)
		taxonomyUnitService.saveAll(
			listOf(
				Taxonomy.phylum,
				Taxonomy.kingdom,
				Taxonomy.domain
			)
		)
	}
}
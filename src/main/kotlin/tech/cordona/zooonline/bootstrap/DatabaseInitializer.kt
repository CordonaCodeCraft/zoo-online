package tech.cordona.zooonline.bootstrap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import tech.cordona.zooonline.bootstrap.objects.animal.AnimalBuilder
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy
import tech.cordona.zooonline.domain.animals.service.AnimalServiceImpl
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceImpl

@Component
class DatabaseInitializer @Autowired constructor(
	val taxonomyUnitService: TaxonomyUnitServiceImpl,
	val animalService: AnimalServiceImpl
) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {
		saveTaxonomyUnitsInDB()
		taxonomyUnitService.findAllAnimals()
			.map { taxonomyUnit -> AnimalBuilder.buildAnimals(taxonomyUnit, taxonomyUnitService) }
			.flatten()
			.forEach { animal -> animalService.save(animal) }
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
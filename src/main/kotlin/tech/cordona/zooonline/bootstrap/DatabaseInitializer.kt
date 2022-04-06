package tech.cordona.zooonline.bootstrap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import tech.cordona.zooonline.bootstrap.animal.AnimalBuilder
import tech.cordona.zooonline.bootstrap.area.AreaBuilder
import tech.cordona.zooonline.bootstrap.cell.CellBuilder
import tech.cordona.zooonline.bootstrap.taxonomy.Amphibians
import tech.cordona.zooonline.bootstrap.taxonomy.Birds
import tech.cordona.zooonline.bootstrap.taxonomy.Insects
import tech.cordona.zooonline.bootstrap.taxonomy.Mammals
import tech.cordona.zooonline.bootstrap.taxonomy.Reptiles
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy
import tech.cordona.zooonline.domain.animal.service.AnimalServiceImpl
import tech.cordona.zooonline.domain.area.service.AreaServiceImpl
import tech.cordona.zooonline.domain.cell.service.CellServiceImpl
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceImpl

@Component
class DatabaseInitializer @Autowired constructor(
	val taxonomyUnitService: TaxonomyUnitServiceImpl,
	val animalService: AnimalServiceImpl,
	val cellService: CellServiceImpl,
	val areaService : AreaServiceImpl
) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {

		persistTaxonomyUnits()

		val animals = taxonomyUnitService.findAllAnimals()
			.map { animal -> AnimalBuilder.buildAnimals(animal, taxonomyUnitService) }
			.flatten()

		animalService.saveAll(animals)

		val cells = CellBuilder.buildCells(animals, taxonomyUnitService)

		cellService.saveAll(cells)

		val areas = AreaBuilder.buildAreas(cells)

		areaService.saveAll(areas)
	}

	fun persistTaxonomyUnits() {
		taxonomyUnitService.saveAll(Mammals.getMammalSpecies())
		taxonomyUnitService.saveAll(Birds.getBirdSpecies())
		taxonomyUnitService.saveAll(Reptiles.getReptileSpecies())
		taxonomyUnitService.saveAll(Insects.getInsectSpecies())
		taxonomyUnitService.saveAll(Amphibians.getAmphibianSpecies())

		taxonomyUnitService.saveAll(Mammals.getMammalTypes())
		taxonomyUnitService.saveAll(Birds.getBirdTypes())
		taxonomyUnitService.saveAll(Reptiles.getReptileTypes())
		taxonomyUnitService.saveAll(Insects.getInsectTypes())
		taxonomyUnitService.saveAll(Amphibians.getAmphibianTypes())

		taxonomyUnitService.saveAll(
			listOf(
				Mammals.mammalTaxonomyUnit,
				Birds.birdTaxonomyUnit,
				Reptiles.reptileTaxonomyUnit,
				Insects.insectTaxonomyUnit,
				Amphibians.amphibianTaxonomyUnit
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
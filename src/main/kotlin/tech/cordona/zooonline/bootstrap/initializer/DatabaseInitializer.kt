package tech.cordona.zooonline.bootstrap.initializer

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import tech.cordona.zooonline.bootstrap.builders.taxonomy.AmphibianBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.BirdBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.InsectBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.MammalBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.ReptileBuilder
import tech.cordona.zooonline.domain.animal.service.AnimalServiceImpl
import tech.cordona.zooonline.domain.area.service.AreaServiceImpl
import tech.cordona.zooonline.domain.cell.service.CellServiceImpl
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Domain.EUKARYOTE
import tech.cordona.zooonline.domain.taxonomy.enums.Kingdom.ANIMALIA
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceImpl

@Component
class DatabaseInitializer(
	private val taxonomyUnitService: TaxonomyUnitServiceImpl,
	private val animalService: AnimalServiceImpl,
	private val cellService: CellServiceImpl,
	private val areaService: AreaServiceImpl
) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {

		persistTaxonomyUnits()

//		val animals = taxonomyUnitService.findAllAnimals()
//			.map { animal -> AnimalBuilder.buildAnimals(animal, taxonomyUnitService) }
//			.flatten()
//
//		animalService.saveAll(animals)
//
//		val cells = CellBuilder.buildCells(animals, taxonomyUnitService)
//
//		cellService.saveAll(cells)
//
//		val areas = AreaBuilder.buildAreas(cells)

//		areaService.saveAll(areas)
	}

	fun persistTaxonomyUnits() {
		taxonomyUnitService.saveAll(MammalBuilder.getMammalSpecies())
		taxonomyUnitService.saveAll(BirdBuilder.getBirdSpecies())
		taxonomyUnitService.saveAll(ReptileBuilder.getReptileSpecies())
		taxonomyUnitService.saveAll(InsectBuilder.getInsectSpecies())
		taxonomyUnitService.saveAll(AmphibianBuilder.getAmphibianSpecies())

		taxonomyUnitService.saveAll(MammalBuilder.getMammalTypes())
		taxonomyUnitService.saveAll(BirdBuilder.getBirdTypes())
		taxonomyUnitService.saveAll(ReptileBuilder.getReptileTypes())
		taxonomyUnitService.saveAll(InsectBuilder.getInsectTypes())
		taxonomyUnitService.saveAll(AmphibianBuilder.getAmphibianTypes())

		taxonomyUnitService.saveAll(
			listOf(
				MammalBuilder.getMammalTaxonomyUnit(),
				BirdBuilder.getBirdTaxonomyUnit(),
				ReptileBuilder.getReptileTaxonomyUnit(),
				InsectBuilder.getInsectTaxonomyUnit(),
				AmphibianBuilder.getAmphibianTaxonomyUnit()
			)
		)

		val phylum =
			TaxonomyUnit(
				ANIMAL.asString,
				ANIMALIA.asString,
				mutableSetOf(
					MammalBuilder.getMammalTaxonomyUnit().name,
					BirdBuilder.getBirdTaxonomyUnit().name,
					ReptileBuilder.getReptileTaxonomyUnit().name,
					InsectBuilder.getInsectTaxonomyUnit().name,
					AmphibianBuilder.getAmphibianTaxonomyUnit().name
				)
			)

		val kingdom = TaxonomyUnit(ANIMALIA.asString, EUKARYOTE.asString, mutableSetOf(phylum.name))
		val domain = TaxonomyUnit(EUKARYOTE.asString, "Life", mutableSetOf(kingdom.name))

		taxonomyUnitService.saveAll(listOf(phylum, kingdom, domain))
	}
}
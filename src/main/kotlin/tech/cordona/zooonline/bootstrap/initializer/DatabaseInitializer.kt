package tech.cordona.zooonline.bootstrap.initializer

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import tech.cordona.zooonline.bootstrap.builders.animal.AnimalBuilder
import tech.cordona.zooonline.bootstrap.builders.area.AreaBuilder
import tech.cordona.zooonline.bootstrap.builders.cell.CellBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.AmphibianBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.BirdBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.InsectBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.MammalBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.ReptileBuilder
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Domain.EUKARYOTE
import tech.cordona.zooonline.domain.taxonomy.enums.Kingdom.ANIMALIA
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService

@Component
class DatabaseInitializer(
	private val taxonomyUnitService: TaxonomyUnitService,
	private val animalService: AnimalService,
	private val cellService: CellService,
	private val areaService: AreaService
) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {

		persistTaxonomyUnits()

		taxonomyUnitService.findAllAnimals()
			.map { animal -> AnimalBuilder.buildAnimals(animal, taxonomyUnitService) }
			.flatten()
			.let { animals -> animalService.saveAll(animals) }
			.let { animals -> CellBuilder.buildCells(animals, taxonomyUnitService) }
			.let { cells -> cellService.saveAll(cells) }
			.let { cells -> AreaBuilder.buildAreas(cells) }
			.let { areas -> areaService.saveAll(areas) }
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
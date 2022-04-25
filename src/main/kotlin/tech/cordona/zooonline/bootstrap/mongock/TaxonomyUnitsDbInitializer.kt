package tech.cordona.zooonline.bootstrap.mongock


import io.mongock.api.annotations.BeforeExecution
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackBeforeExecution
import io.mongock.api.annotations.RollbackExecution
import org.springframework.data.mongodb.core.MongoTemplate
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


@ChangeUnit(order = "1", id = "taxonomy-units-initialization", author = "Cordona")
class TaxonomyUnitsDbInitializer(
	private val mongoTemplate: MongoTemplate,
	private val taxonomyUnitService: TaxonomyUnitService,
	private val animalService: AnimalService,
	private val cellService: CellService,
	private val areaService: AreaService
) {

	@BeforeExecution
	fun beforeExecution() {
		mongoTemplate.dropCollection(TAXONOMY_UNITS_COLLECTION)
		mongoTemplate.dropCollection(ANIMALS_COLLECTION)
		mongoTemplate.dropCollection(CELLS_COLLECTION)
		mongoTemplate.dropCollection(AREAS_COLLECTION)
		mongoTemplate.createCollection(TAXONOMY_UNITS_COLLECTION)
		mongoTemplate.createCollection(ANIMALS_COLLECTION)
		mongoTemplate.createCollection(CELLS_COLLECTION)
		mongoTemplate.createCollection(AREAS_COLLECTION)
	}

	@RollbackBeforeExecution
	fun rollbackBeforeExecution() {
		mongoTemplate.dropCollection(TAXONOMY_UNITS_COLLECTION)
		mongoTemplate.dropCollection(ANIMALS_COLLECTION)
		mongoTemplate.dropCollection(CELLS_COLLECTION)
		mongoTemplate.dropCollection(AREAS_COLLECTION)
	}

	@RollbackExecution
	fun rollbackExecution() {
		taxonomyUnitService.deleteAll()
		animalService.deleteAll()
		cellService.deleteAll()
		areaService.deleteAll()
	}

	@Execution
	fun initialize() {
		listOf(
			MammalBuilder.getMammalSpecies(),
			BirdBuilder.getBirdSpecies(),
			ReptileBuilder.getReptileSpecies(),
			InsectBuilder.getInsectSpecies(),
			AmphibianBuilder.getAmphibianSpecies(),
			MammalBuilder.getMammalTypes(),
			BirdBuilder.getBirdTypes(),
			ReptileBuilder.getReptileTypes(),
			InsectBuilder.getInsectTypes(),
			AmphibianBuilder.getAmphibianTypes(),
			listOf(
				MammalBuilder.mammalTaxonomyUnit,
				BirdBuilder.birdTaxonomyUnit,
				ReptileBuilder.reptileTaxonomyUnit,
				InsectBuilder.insectTaxonomyUnit,
				AmphibianBuilder.amphibianTaxonomyUnit
			),
			listOf(
				TaxonomyUnit(
					name = ANIMAL.asString,
					parent = ANIMALIA.asString,
					children = mutableSetOf(
						MammalBuilder.mammalTaxonomyUnit.name,
						BirdBuilder.birdTaxonomyUnit.name,
						ReptileBuilder.reptileTaxonomyUnit.name,
						InsectBuilder.insectTaxonomyUnit.name,
						AmphibianBuilder.amphibianTaxonomyUnit.name
					)
				),
				TaxonomyUnit(
					name = ANIMALIA.asString,
					parent = EUKARYOTE.asString,
					children = mutableSetOf(ANIMAL.asString)
				),
				TaxonomyUnit(
					name = EUKARYOTE.asString,
					parent = "Life",
					children = mutableSetOf(ANIMALIA.asString)
				)
			)
		)
			.flatten()
			.let { taxonomyUnits -> taxonomyUnitService.saveAll(taxonomyUnits) }

		taxonomyUnitService.findAllAnimals()
			.map { animal -> AnimalBuilder.buildAnimals(animal, taxonomyUnitService) }
			.flatten()
			.let { animals -> animalService.saveAll(animals) }
			.let { animals -> CellBuilder.buildCells(animals, taxonomyUnitService) }
			.let { cells -> cellService.saveAll(cells) }
			.let { cells -> AreaBuilder.buildAreas(cells) }
			.let { areas -> areaService.saveAll(areas) }
	}

	companion object {
		const val TAXONOMY_UNITS_COLLECTION = "TaxonomyUnits"
		const val ANIMALS_COLLECTION = "Animals"
		const val CELLS_COLLECTION = "Cells"
		const val AREAS_COLLECTION = "Areas"
	}
}

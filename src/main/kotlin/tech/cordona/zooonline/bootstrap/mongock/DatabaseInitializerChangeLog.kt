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
import tech.cordona.zooonline.domain.taxonomy.enums.Domain
import tech.cordona.zooonline.domain.taxonomy.enums.Kingdom
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService


@ChangeUnit(order = "1", id = "db-initialization", author = "Cordona")
class DatabaseInitializerChangeLog(
	private val mongoTemplate: MongoTemplate,
	private val taxonomyUnitService: TaxonomyUnitService,
	private val animalService: AnimalService,
	private val cellService: CellService,
	private val areaService: AreaService
) {

	private val taxonomyUnits = "TaxonomyUnits"
	private val animals = "Animals"
	private val cells = "Cells"
	private val areas = "Areas"

	@BeforeExecution
	fun beforeExecution() {
		mongoTemplate.createCollection(taxonomyUnits)
		mongoTemplate.createCollection(animals)
		mongoTemplate.createCollection(cells)
		mongoTemplate.createCollection(areas)
	}

	@RollbackBeforeExecution
	fun rollbackBeforeExecution() {
		mongoTemplate.dropCollection(taxonomyUnits)
		mongoTemplate.dropCollection(animals)
		mongoTemplate.dropCollection(cells)
		mongoTemplate.dropCollection(areas)
	}

	@RollbackExecution
	fun rollbackExecution() {
		taxonomyUnitService.deleteAll()
		animalService.deleteAll()
		animalService.deleteAll()
		cellService.deleteAll()
		areaService.deleteAll()
	}

	@Execution
	fun execution() {
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
					name = Phylum.ANIMAL.asString,
					parent = Kingdom.ANIMALIA.asString,
					children = mutableSetOf(
						MammalBuilder.mammalTaxonomyUnit.name,
						BirdBuilder.birdTaxonomyUnit.name,
						ReptileBuilder.reptileTaxonomyUnit.name,
						InsectBuilder.insectTaxonomyUnit.name,
						AmphibianBuilder.amphibianTaxonomyUnit.name
					)
				),
				TaxonomyUnit(
					name = Kingdom.ANIMALIA.asString,
					parent = Domain.EUKARYOTE.asString,
					children = mutableSetOf(Phylum.ANIMAL.asString)
				),
				TaxonomyUnit(
					name = Domain.EUKARYOTE.asString,
					parent = "Life",
					children = mutableSetOf(Kingdom.ANIMALIA.asString)
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
}

package tech.cordona.zooonline.bootstrap.builders.cell

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService

object CellBuilder {

	fun buildCells(animals: List<Animal>, taxonomyUnitService: TaxonomyUnitService) = animals
		.associate {
			it.taxonomyDetails.name to
					animals.filter { animal -> animal.taxonomyDetails.name == it.taxonomyDetails.name }
		}
		.toMap()
		.map { specie -> buildCell(specie, taxonomyUnitService) }

	private fun buildCell(
		entry: Map.Entry<String, List<Animal>>,
		taxonomyUnitService: TaxonomyUnitService
	): Cell {
		val parent = entry.value.first().taxonomyDetails.parent
		return Cell(
			animalGroup = taxonomyUnitService.findParentOf(parent).name,
			animalType = parent,
			specie = entry.key,
			animals = entry.value.map { animal -> animal.id }.toMutableSet()
		)
	}
}
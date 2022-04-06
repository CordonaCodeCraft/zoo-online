package tech.cordona.zooonline.bootstrap.cell

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitServiceImpl

object CellBuilder {

	private var animalsMap = LinkedHashMap<String, MutableSet<Animal>>()

	fun buildCells(animals: List<Animal>, taxonomyUnitService: TaxonomyUnitServiceImpl): List<Cell> {

		animals.forEach { animal ->
			animalsMap.putIfAbsent(animal.taxonomyDetails.name, mutableSetOf())
			animalsMap[animal.taxonomyDetails.name]?.add(animal)
		}

		return animalsMap
			.map { specie -> buildCell(specie, taxonomyUnitService) }
			.toList()
	}

	private fun buildCell(
		entry: Map.Entry<String, MutableSet<Animal>>,
		taxonomyUnitService: TaxonomyUnitServiceImpl
	): Cell {
		val animalType = entry.value.first().taxonomyDetails.parent
		val animalGroup = taxonomyUnitService.findParentOf(animalType).name
		val specie = entry.key
		val animalsIds = entry.value.map { animal -> animal.id }.toMutableSet()

		return Cell(animalGroup, animalType, specie, animalsIds)
	}
}
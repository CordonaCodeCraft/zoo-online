package tech.cordona.zooonline.bootstrap.builders.area

import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.cell.entity.Cell

object AreaBuilder {

	private var cellsMap = LinkedHashMap<String, MutableSet<Cell>>()

	fun buildAreas(cells: List<Cell>): List<Area> {
		cells.forEach { cell ->
			cellsMap.putIfAbsent(cell.animalType, mutableSetOf())
			cellsMap[cell.animalType]?.add(cell)
		}

		return cellsMap.map { type -> buildArea(type) }.toList()
	}

	private fun buildArea(entry: Map.Entry<String, MutableSet<Cell>>): Area {
		val cellIds = entry.value.map { cell -> cell.id }.toMutableSet()
		return Area(entry.key, cellIds)
	}
}
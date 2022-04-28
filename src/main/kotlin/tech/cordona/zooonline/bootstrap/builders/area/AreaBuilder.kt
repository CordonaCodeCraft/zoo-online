package tech.cordona.zooonline.bootstrap.builders.area

import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.AreaStaff
import tech.cordona.zooonline.domain.cell.entity.Cell

object AreaBuilder {

	fun buildAreas(cells: List<Cell>) = cells
		.associate { it.animalType to cells.filter { cell -> cell.animalType == it.animalType } }
		.toMap()
		.map { entry -> buildArea(entry) }

	private fun buildArea(entry: Map.Entry<String, List<Cell>>) = Area(
		name = entry.key,
		cells = entry.value.map { cell -> cell.id!! }.toSet(),
		staff = AreaStaff(mutableSetOf(), mutableSetOf(), mutableSetOf())
	)
}
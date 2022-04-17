package tech.cordona.zooonline.domain.area.models

import org.springframework.stereotype.Component
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService

@Component
class AreaConverter(
	private val areaService: AreaService,
	private val cellService: CellService
) {

	fun areaToVisitor(animalType: String) = convertAreaForVisitor(areaService.findAreaByAnimalType(animalType))

	fun areasToVisitor() = areaService.findAllAreas().map { area -> convertAreaForVisitor(area) }

	fun areasToVisitor(animalTypes: List<String>) =
		areaService.findAreasByAnimalTypes(animalTypes).map { area -> convertAreaForVisitor(area) }

	private fun convertAreaForVisitor(area: Area) = AreaToVisitor(
		area = area.animalType,
		species = cellService.findCellsByIds(area.cells).map { cell -> cell.specie }
	)
}



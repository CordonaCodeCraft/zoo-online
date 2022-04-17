package tech.cordona.zooonline.domain.cell.models

import org.springframework.stereotype.Component
import tech.cordona.zooonline.domain.animal.models.AnimalToVisitor
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.service.CellService
import kotlin.math.roundToInt

@Component
class CellConverter(
	private val cellService: CellService,
	private val animalService: AnimalService
) {

	fun cellToVisitor(specie: String) = convertCellForVisitor(cellService.findCellBySpecie(format(specie)))

	private fun format(specie: String) = specie.replace("-", " ")

	private fun convertCellForVisitor(cell: Cell) = CellToVisitor(
		group = cell.animalGroup,
		type = cell.animalType,
		specie = cell.specie,
		animalsCount = cell.animals.size,
		animals = animalService.findAnimalsByIds(cell.animals)
			.map { animal ->
				AnimalToVisitor(
					name = animal.name,
					age = animal.age,
					weight = (animal.weight * 10.0).roundToInt() / 10.0,
					gender = animal.gender
				)
			}
	)
}
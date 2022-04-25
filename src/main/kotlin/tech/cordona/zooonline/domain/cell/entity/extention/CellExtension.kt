package tech.cordona.zooonline.domain.cell.entity.extention

import tech.cordona.zooonline.domain.animal.model.AnimalToVisitor
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.model.CellToVisitor

object CellExtension {

	fun Cell.toVisitor(animals: List<AnimalToVisitor>) = CellToVisitor(
		animalGroup = this.animalGroup,
		animalType = this.animalType,
		specie = this.specie,
		animals = animals
	)
}
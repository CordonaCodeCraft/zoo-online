package tech.cordona.zooonline.domain.cell.entity.extension

import tech.cordona.zooonline.domain.animal.model.AnimalToVisitor
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.model.CellToGuard
import tech.cordona.zooonline.domain.cell.model.CellToVisitor

fun Cell.toVisitor(animals: List<AnimalToVisitor>) = CellToVisitor(
	animalGroup = this.animalGroup,
	animalType = this.animalType,
	specie = this.specie,
	animals = animals
)

fun Cell.toGuard() = CellToGuard(
	specie = this.specie,
	animalCount = this.species.size
)
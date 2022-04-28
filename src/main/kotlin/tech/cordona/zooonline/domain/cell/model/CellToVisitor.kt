package tech.cordona.zooonline.domain.cell.model

import tech.cordona.zooonline.domain.animal.model.AnimalToVisitor

data class CellToVisitor(
	val animalGroup: String,
	val animalType: String,
	val specie: String,
	val animals: List<AnimalToVisitor>
)
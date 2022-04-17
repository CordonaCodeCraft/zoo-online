package tech.cordona.zooonline.domain.cell.models

import tech.cordona.zooonline.domain.animal.models.AnimalToVisitor

data class CellToVisitor(
	val group: String,
	val type: String,
	val specie: String,
	val animalsCount: Int,
	val animals: List<AnimalToVisitor>
)
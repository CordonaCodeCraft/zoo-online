package tech.cordona.zooonline.domain.cell.entity

import tech.cordona.zooonline.domain.BaseEntity
import tech.cordona.zooonline.domain.animals.entity.Animal

data class Cell(
	val capacity: Int,
	val animalGroup: String,
	val animalType: String,
	val animalSpecie: String,
	val occupants: MutableSet<Animal>
) : BaseEntity()
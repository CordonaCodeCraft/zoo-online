package tech.cordona.zooonline.domain.cell.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.BaseEntity

@Document(collection = "Cells")
@TypeAlias("Cell")
data class Cell(
	val animalGroup: String,
	val animalType: String,
	val specie: String,
	val species: MutableSet<ObjectId>
) : BaseEntity()
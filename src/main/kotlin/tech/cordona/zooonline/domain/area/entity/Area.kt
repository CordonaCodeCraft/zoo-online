package tech.cordona.zooonline.domain.area.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.BaseEntity

@Document(collection = "Areas")
@TypeAlias("Area")
data class Area(
	val animalType: String,
	val cells: MutableSet<ObjectId>
) : BaseEntity()
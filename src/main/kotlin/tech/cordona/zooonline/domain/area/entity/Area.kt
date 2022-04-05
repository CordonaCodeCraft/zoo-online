package tech.cordona.zooonline.domain.area.entity

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.BaseEntity

data class Area(
	val animalGroup: String,
	val cells: MutableSet<ObjectId>
) : BaseEntity()
package tech.cordona.zooonline.domain.area.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.BaseEntity
import java.time.LocalDateTime

@Document(collection = "Areas")
@TypeAlias("Area")
data class Area(
	val animalType: String,
	val cells: MutableSet<ObjectId>,
	override val id: ObjectId = ObjectId.get(),
	override val createdDate: LocalDateTime = LocalDateTime.now(),
	override var modifiedDate: LocalDateTime = LocalDateTime.now()
) : BaseEntity
package tech.cordona.zooonline.domain.cell.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.BaseEntity
import java.time.LocalDateTime

@Document(collection = "Cells")
@TypeAlias("Cell")
data class Cell(
	val animalGroup: String,
	val animalType: String,
	val specie: String,
	val species: MutableSet<ObjectId>,
	override val id: ObjectId = ObjectId.get(),
	override val createdDate: LocalDateTime = LocalDateTime.now(),
	override var modifiedDate: LocalDateTime = LocalDateTime.now()
) : BaseEntity
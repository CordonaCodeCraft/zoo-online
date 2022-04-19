package tech.cordona.zooonline.domain.cell.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.DatabaseInitializer.Companion.CELLS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.BaseEntity
import java.time.LocalDateTime

@Document(collection = CELLS_COLLECTION)
@TypeAlias("Cell")
data class Cell(
	val animalGroup: String,
	val animalType: String,
	@Indexed(unique = true)
	val specie: String,
	val species: MutableSet<ObjectId>,
	override val id: ObjectId = ObjectId.get(),
	override val createdDate: LocalDateTime = LocalDateTime.now(),
	override var modifiedDate: LocalDateTime = LocalDateTime.now()
) : BaseEntity
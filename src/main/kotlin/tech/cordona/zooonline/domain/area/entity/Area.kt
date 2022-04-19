package tech.cordona.zooonline.domain.area.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.DatabaseInitializer.Companion.AREAS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.BaseEntity
import java.time.LocalDateTime

@Document(collection = AREAS_COLLECTION)
@TypeAlias("Area")
data class Area(
	@Indexed(unique = true)
	val animalType: String,
	val cells: MutableSet<ObjectId>,
	override val id: ObjectId = ObjectId.get(),
	override val createdOn: LocalDateTime = LocalDateTime.now(),
	override var modifiedOn: LocalDateTime = LocalDateTime.now()
) : BaseEntity
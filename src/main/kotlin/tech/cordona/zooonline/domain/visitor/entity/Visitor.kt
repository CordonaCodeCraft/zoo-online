package tech.cordona.zooonline.domain.visitor.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.common.entity.BaseEntity
import java.time.LocalDateTime

@Document("Visitors")
@TypeAlias("Visitor")
data class Visitor(
	val userId: ObjectId,
	val firstName: String,
	val lastName: String,
	val favorites: List<String>,
	override val id: ObjectId = ObjectId.get(),
	override val createdOn: LocalDateTime = LocalDateTime.now(),
	override var modifiedOn: LocalDateTime = LocalDateTime.now()
) : BaseEntity

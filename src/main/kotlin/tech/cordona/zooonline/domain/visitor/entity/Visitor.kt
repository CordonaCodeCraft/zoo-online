package tech.cordona.zooonline.domain.visitor.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.validation.annotation.validname.ValidName

@Document("Visitors")
@TypeAlias("Visitor")
data class Visitor(
	val userId: ObjectId,
	@get:ValidName
	val firstName: String,
	@get:ValidName
	val lastName: String,
	val favorites: MutableSet<String>,
	override val id: ObjectId? = null,
) : Identifiable, AuditMetadata()

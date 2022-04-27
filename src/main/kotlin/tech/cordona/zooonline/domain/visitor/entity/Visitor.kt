package tech.cordona.zooonline.domain.visitor.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable

@Document("Visitors")
@TypeAlias("Visitor")
data class Visitor(
	val userId: ObjectId,
	val firstName: String,
	val lastName: String,
	val favorites: List<String>,
	override val id: ObjectId? = null,
) : Identifiable, AuditMetadata()

package tech.cordona.zooonline.domain.trainer.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.StaffDbInitializer.Companion.TRAINERS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable

@Document(collection = TRAINERS_COLLECTION)
@TypeAlias("Trainer")
data class Trainer(
	val userId: ObjectId,
	val firstName: String,
	val lastName: String,
	val area: String,
	val animals: MutableSet<ObjectId>,
	override val id: ObjectId? = null,
) : Identifiable, AuditMetadata()
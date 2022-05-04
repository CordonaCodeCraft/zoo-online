package tech.cordona.zooonline.domain.trainer.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.StaffDbInitializer.Companion.TRAINERS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Employee
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.domain.user.entity.Authority.TRAINER
import tech.cordona.zooonline.extension.asTitlecase

@Document(collection = TRAINERS_COLLECTION)
@TypeAlias("Trainer")
data class Trainer(
	override val id: ObjectId? = null,
	override val userId: ObjectId,
	override val firstName: String,
	override val middleName: String,
	override val lastName: String,
	override val area: String,
	override val position: String = TRAINER.name.asTitlecase(),
	val animals: MutableSet<ObjectId>
) : Identifiable, Employee, AuditMetadata()
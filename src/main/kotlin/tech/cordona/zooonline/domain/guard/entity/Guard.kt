package tech.cordona.zooonline.domain.guard.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.StaffDbInitializer.Companion.GUARDS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Employee
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.extension.StringExtension.asTitlecase
import tech.cordona.zooonline.security.user.entity.Authority.GUARD

@Document(collection = GUARDS_COLLECTION)
@TypeAlias("Guard")
data class Guard(
	override val id: ObjectId? = null,
	override val userId: ObjectId,
	override val firstName: String,
	override val middleName: String,
	override val lastName: String,
	override val area: String,
	override val position: String = GUARD.name.asTitlecase(),
	val cells: MutableSet<ObjectId>,
) : Identifiable, Employee, AuditMetadata()

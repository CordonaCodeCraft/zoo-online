package tech.cordona.zooonline.domain.doctor.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.StaffDbInitializer.Companion.DOCTORS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Employee
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.domain.user.entity.Authority.DOCTOR

@Document(collection = DOCTORS_COLLECTION)
@TypeAlias("Doctor")
data class Doctor(
	override val id: ObjectId? = null,
	override val userId: ObjectId,
	override val firstName: String,
	override val middleName: String,
	override val lastName: String,
	override val area: String,
	override val position: String = DOCTOR.name.asTitlecase(),
	val animals: MutableSet<ObjectId>,
) : Identifiable, Employee, AuditMetadata()
package tech.cordona.zooonline.security.user.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.security.user.entity.Authority.USER

@Document("Users")
@TypeAlias("User")
data class User(
	val firstName: String,
	val lastName: String,
	val email: String,
	val password: String,
	val authority: Authority = USER,
	val locked: Boolean = false,
	val confirmed: Boolean = false,
	override val id: ObjectId? = null
) : Identifiable, AuditMetadata()


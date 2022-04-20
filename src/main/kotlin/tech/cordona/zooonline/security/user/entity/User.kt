package tech.cordona.zooonline.security.user.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.common.entity.BaseEntity
import tech.cordona.zooonline.security.user.entity.Authority.USER
import java.time.LocalDateTime

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
	override val id: ObjectId = ObjectId.get(),
	override val createdOn: LocalDateTime = LocalDateTime.now(),
	override var modifiedOn: LocalDateTime = LocalDateTime.now()
) : BaseEntity

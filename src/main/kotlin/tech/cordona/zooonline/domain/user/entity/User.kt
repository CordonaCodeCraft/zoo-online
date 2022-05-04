package tech.cordona.zooonline.domain.user.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.UsersDbInitializer.Companion.USERS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.domain.user.entity.Authority.USER
import tech.cordona.zooonline.validation.annotation.validname.ValidName
import javax.validation.constraints.Email

@Document(USERS_COLLECTION)
@TypeAlias("User")
data class User(
	@get:ValidName
	val firstName: String,
	@get:ValidName
	val middleName: String,
	@get:ValidName
	val lastName: String,
	@get:Email
	val email: String,
	val password: String,
	val authority: Authority = USER,
	val locked: Boolean = false,
	val confirmed: Boolean = false,
	override val id: ObjectId? = null
) : Identifiable, AuditMetadata()


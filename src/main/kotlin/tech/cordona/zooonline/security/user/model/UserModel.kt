package tech.cordona.zooonline.security.user.model

import tech.cordona.zooonline.validation.annotation.validname.ValidName
import javax.validation.constraints.Email

data class UserModel(
	@get:ValidName
	val firstName: String,
	@get:ValidName
	val middleName: String,
	@get:ValidName
	val lastName: String,
	@get:Email
	val email: String,
	val password: String
)
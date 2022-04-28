package tech.cordona.zooonline.security.user.model

data class UserModel(
	val firstName: String,
	val middleName: String,
	val lastName: String,
	val email: String,
	val password: String
)
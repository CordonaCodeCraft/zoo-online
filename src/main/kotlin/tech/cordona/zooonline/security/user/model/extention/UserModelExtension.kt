package tech.cordona.zooonline.security.user.model.extention

import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.model.UserModel

object UserModelExtension {

	fun UserModel.asEntity() = User(
		firstName = this.firstName,
		lastName = this.lastName,
		email = this.email,
		password = this.password,
	)
}
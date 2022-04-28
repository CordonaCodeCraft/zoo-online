package tech.cordona.zooonline.security.user.model.extension

import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.model.UserModel

fun UserModel.asEntity() = User(
	firstName = this.firstName,
	middleName = this.middleName,
	lastName = this.lastName,
	email = this.email,
	password = this.password,
)
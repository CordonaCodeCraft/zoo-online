package tech.cordona.zooonline.domain.user.model.extension

import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.model.UserModel

fun UserModel.asEntity() = User(
	firstName = this.firstName,
	middleName = this.middleName,
	lastName = this.lastName,
	email = this.email,
	password = this.password,
)
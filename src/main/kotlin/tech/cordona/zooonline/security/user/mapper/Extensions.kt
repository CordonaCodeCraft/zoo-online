package tech.cordona.zooonline.security.user.mapper

import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.model.AuthenticatedUserDetails
import tech.cordona.zooonline.security.user.model.UserModel

object Extensions {

	fun UserModel.asEntity() = User(
		firstName = this.firstName,
		lastName = this.lastName,
		email = this.email,
		password = this.password,
	)

	fun User.asModel() = UserModel(
		firstName =this.firstName,
		lastName = this.lastName,
		email = this.email,
		password = this.password
	)

	fun User.asVisitor() = Visitor(
		userId = this.id!!,
		firstName = this.firstName,
		lastName = this.lastName,
		favorites = listOf()
	)

	fun User.asAuthenticatedUser() = AuthenticatedUserDetails(
		id = this.id!!,
		email = this.email,
		userPassword = this.password,
		authority = this.authority
	)
}
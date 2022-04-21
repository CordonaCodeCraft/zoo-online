package tech.cordona.zooonline.security.user.mapper

import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.model.AuthenticatedUserDetails
import tech.cordona.zooonline.security.user.model.UserModel

object UserMapper {

	fun modelToEntity(model: UserModel) = User(
		firstName = model.firstName,
		lastName = model.lastName,
		email = model.email,
		password = model.password,
	)

	fun entityToModel(entity: User) = UserModel(
		firstName = entity.firstName,
		lastName = entity.lastName,
		email = entity.email,
		password = entity.password
	)

	fun entityToVisitor(entity: User) = Visitor(
		userId = entity.id,
		firstName = entity.firstName,
		lastName = entity.lastName,
		favorites = listOf()
	)

	fun entityToAuthenticatedUser(entity: User) = AuthenticatedUserDetails(
		id = entity.id,
		email = entity.email,
		userPassword = entity.password,
		authority = entity.authority
	)
}
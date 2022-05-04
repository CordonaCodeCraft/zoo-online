package tech.cordona.zooonline.security.authentication.service

import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.model.UserModel

interface AuthenticationService {
	fun register(newUser: UserModel): User
	fun verifyEmail(token: String)
}
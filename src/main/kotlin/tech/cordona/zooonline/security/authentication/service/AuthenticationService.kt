package tech.cordona.zooonline.security.authentication.service

import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.model.UserModel

interface AuthenticationService {
	fun register(newUser: UserModel): User
	fun verifyEmail(token: String)
}
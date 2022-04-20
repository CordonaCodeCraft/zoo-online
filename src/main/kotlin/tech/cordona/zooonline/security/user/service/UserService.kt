package tech.cordona.zooonline.security.user.service

import org.springframework.security.core.userdetails.UserDetailsService
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.model.UserModel

interface UserService : UserDetailsService {
	fun createUser(model: UserModel): User
	fun confirmUser(id: String)
}
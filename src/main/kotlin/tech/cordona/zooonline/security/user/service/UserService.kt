package tech.cordona.zooonline.security.user.service

import org.springframework.security.core.userdetails.UserDetailsService
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.model.UserModel

interface UserService : UserDetailsService {
	fun createUser(model: UserModel): User
	fun createUser(user: User): User
	fun initUser(userId: String): User
	fun findByUserName(username: String): User
	fun findById(userId: String): User
	fun deleteAll()
}
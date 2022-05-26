package tech.cordona.zooonline.domain.user.service

import org.springframework.security.core.userdetails.UserDetailsService
import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.model.UserModel

interface UserService : UserDetailsService {
	fun createUser(model: UserModel): User
	fun createUser(user: User): User
	fun createUsers(users: List<User>): List<User>
	fun initUser(userId: String): User
	fun findByUserName(username: String): User
	fun findById(userId: String): User
	fun deleteAll()
}
package tech.cordona.zooonline.security.user.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.entity.extension.asAuthenticatedUser
import tech.cordona.zooonline.security.user.model.UserModel
import tech.cordona.zooonline.security.user.model.extension.asEntity
import tech.cordona.zooonline.security.user.repository.UserRepository

@Service
class UserServiceImpl(
	private val repository: UserRepository,
	private val passwordEncoder: BCryptPasswordEncoder
) : UserService {

	private val logger = KotlinLogging.logger {}

	override fun createUser(model: UserModel) = repository.save(model.asEntity().withEncodedPassword())
		.also { logger.info { "Saving user with username: ${it.email}" } }

	override fun createUser(user: User): User = repository.save(user.withEncodedPassword())

	override fun createUsers(users: List<User>): List<User> = repository.saveAll(users)

	override fun initUser(userId: String) = findById(userId).copy(confirmed = true).also { repository.save(it) }

	override fun findByUserName(username: String) = repository.findByEmail(username)
		?: run {
			logger.error { "User with username: $username not found" }
			throw UsernameNotFoundException("User with username: $username not found")
		}

	override fun findById(userId: String): User = repository.findById(ObjectId(userId))
		?: run {
			logger.error { "User with ID: $userId not found" }
			throw UsernameNotFoundException("User with ID: $userId not found")
		}

	override fun loadUserByUsername(username: String) = findByUserName(username).asAuthenticatedUser()

	override fun deleteAll() = repository.deleteAll()

	private fun User.withEncodedPassword() = this.copy(password = passwordEncoder.encode(this.password))
}


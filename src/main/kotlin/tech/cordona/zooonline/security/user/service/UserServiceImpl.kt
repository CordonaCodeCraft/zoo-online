package tech.cordona.zooonline.security.user.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.mapper.Extensions.asAuthenticatedUser
import tech.cordona.zooonline.security.user.mapper.Extensions.asEntity
import tech.cordona.zooonline.security.user.model.UserModel
import tech.cordona.zooonline.security.user.repository.UsersRepository

@Service
class UserServiceImpl(
	private val repository: UsersRepository,
	private val passwordEncoder: BCryptPasswordEncoder
) : UserService {

	private val logger = KotlinLogging.logger {}

	override fun createUser(model: UserModel) = repository.save(model.asEntity().withEncodedPassword())
		.also { logger.info { "Saving user with username: ${it.email}" } }

	override fun createUser(user: User): User = repository.save(user.withEncodedPassword())

	override fun initUser(id: String) = repository.findById(ObjectId(id))
		?.let { user ->
			user.copy(confirmed = true).also { repository.save(it) }
		}
		?: run {
			logger.error { "User with id: $id not found" }
			throw UsernameNotFoundException("User with id: $id not found")
		}

	override fun findByUserName(username: String) = repository.findByEmail(username)
		?: run {
			logger.error { "User with $username not found" }
			throw UsernameNotFoundException("User with $username not found")
		}

	override fun loadUserByUsername(username: String) = findByUserName(username).asAuthenticatedUser()

	override fun deleteAll() = repository.deleteAll()

	private fun User.withEncodedPassword() = this.copy(password = passwordEncoder.encode(this.password))
}




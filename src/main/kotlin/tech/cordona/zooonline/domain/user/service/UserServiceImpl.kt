package tech.cordona.zooonline.domain.user.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.entity.extension.asAuthenticatedUser
import tech.cordona.zooonline.domain.user.entity.extension.withEncodedPassword
import tech.cordona.zooonline.domain.user.model.UserModel
import tech.cordona.zooonline.domain.user.model.extension.asEntity
import tech.cordona.zooonline.domain.user.repository.UserRepository
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.validation.EntityValidator
import tech.cordona.zooonline.validation.FailReport.entityNotFound

@Service
class UserServiceImpl(
	private val repository: UserRepository,
	private val encoder: BCryptPasswordEncoder
) : UserService, EntityValidator() {

	private val logger = KotlinLogging.logger {}

	override fun createUser(model: UserModel) =
		validate(model)
			.let { repository.save(model.asEntity().withEncodedPassword(encoder)) }
			.also { logger.info { "Saving user with username: ${it.email}" } }

	override fun createUser(user: User): User =
		validate(user)
			.let { repository.save(user.withEncodedPassword(encoder)) }
			.also { logger.info { "Saving user with username: ${it.email}" } }


	override fun createUsers(users: List<User>): List<User> = users.map { newUSer -> createUser(newUSer) }

	override fun initUser(userId: String) = findById(userId).copy(confirmed = true).also { repository.save(it) }

	override fun findByUserName(username: String) = repository.findByEmail(username)
		?: run {
			logger.error { entityNotFound(entity = "User", idType = "username", id = username) }
			throw EntityNotFoundException(entityNotFound(entity = "User", idType = "username", id = username))
		}

	override fun findById(userId: String): User = repository.findById(ObjectId(userId))
		?: run {
			logger.error { entityNotFound(entity = "User", idType = "username", id = userId) }
			throw EntityNotFoundException(entityNotFound(entity = "User", idType = "username", id = userId))
		}

	override fun loadUserByUsername(username: String) = findByUserName(username).asAuthenticatedUser()

	override fun deleteAll() = repository.deleteAll()

	private fun validate(model: UserModel) =
		model
			.withValidProperties()
			.withUniqueUsername()

	private fun validate(user: User) =
		user
			.withValidProperties()
			.withUniqueUsername()
}


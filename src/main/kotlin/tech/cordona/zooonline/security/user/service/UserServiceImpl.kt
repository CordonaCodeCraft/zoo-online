package tech.cordona.zooonline.security.user.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tech.cordona.zooonline.security.user.mapper.UserMapper
import tech.cordona.zooonline.security.user.model.UserModel
import tech.cordona.zooonline.security.user.repository.UserRepository

@Service
class UserServiceImpl(
	private val repository: UserRepository,
	private val passwordEncoder: BCryptPasswordEncoder
) : UserService {

	private val logger = KotlinLogging.logger {}

	override fun createUser(model: UserModel) =
		UserMapper.modelToEntity(withEncodedPassword(model))
			.also {
				logger.info { "Saving user with username: ${it.email}" }
			}
			.also { entity ->
				repository.save(entity)
			}

	override fun confirmUser(id: String) {
		repository.findById(ObjectId(id))
			?.let { user ->
				user.copy(confirmed = true)
					.also { confirmed ->
						repository.save(confirmed)
					}
			}
			?: run {
				logger.error { "User with id: $id not found" }
				throw UsernameNotFoundException("User with id: $id not found")
			}
	}

	override fun loadUserByUsername(username: String) =
		repository.findByEmail(username)
			?.let { retrievedEntity ->
				logger.info { "User with $username found" }
				UserMapper.entityToAuthenticatedUser(retrievedEntity)
			}
			?: run {
				logger.error { "User with $username not found" }
				throw UsernameNotFoundException("User with $username not found")
			}

	private fun withEncodedPassword(model: UserModel) =
		model.copy(password = passwordEncoder.encode(model.password))

}




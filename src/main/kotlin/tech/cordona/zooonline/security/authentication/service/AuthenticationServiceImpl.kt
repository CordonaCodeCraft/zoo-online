package tech.cordona.zooonline.security.authentication.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.visitor.service.VisitorService
import tech.cordona.zooonline.security.jwt.service.JwtTokenService
import tech.cordona.zooonline.security.mail.service.EmailService
import tech.cordona.zooonline.security.user.entity.Authority
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.mapper.UserMapper
import tech.cordona.zooonline.security.user.model.UserModel
import tech.cordona.zooonline.security.user.service.UserService

@Service
class AuthenticationServiceImpl(
	private val userService: UserService,
	private val visitorService: VisitorService,
	private val jwtTokenService: JwtTokenService,
	private val emailService: EmailService
) : AuthenticationService {

	private val logger = KotlinLogging.logger {}

	override fun register(newUser: UserModel): UserModel {

		logger.info("Register user BEGIN: $newUser")

		val created = userService.createUser(newUser)

		jwtTokenService.createEmailVerificationToken(
			email = created.email,
			id = created.id.toString()
		)
			.also { token ->
				emailService.sendVerifyRegistrationEmail(created.email, withFullName(created), token)
			}
			.also { logger.info("Register user END: $created") }

		return UserMapper.entityToModel(created)
	}

	override fun verifyEmail(token: String) {

		val tokenInfo = jwtTokenService.decodeToken(token)

		if (tokenInfo.authority != Authority.EMAIL_VERIFY.name) {
			throw RuntimeException("Invalid verify token")
		}

		userService.initUser(tokenInfo.id)
			.also { logger.info("User ${it.firstName} ${it.lastName} with ID: ${it.id} initialized") }
			.also { emailService.sendSuccessfulRegistrationEmail(it) }
			.let { user -> UserMapper.entityToVisitor(user) }
			.also { visitorService.create(it) }
			.also { logger.info("Visitor ${it.firstName} ${it.lastName} with ID: ${it.id} created") }
	}

	fun withFullName(createdUser: User) = "${createdUser.firstName} ${createdUser.lastName}"
}
package tech.cordona.zooonline.security.mail.service

import mu.KotlinLogging
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import tech.cordona.zooonline.security.authentication.controller.AuthenticationController.Companion.VERIFY_EMAIL_URL
import tech.cordona.zooonline.domain.user.entity.User

@Service
class EmailServiceImpl(private val emailSender: JavaMailSender) : EmailService {

	val logger = KotlinLogging.logger {}

	override fun sendVerifyRegistrationEmail(email: String, recipient: String, jwtToken: String) =
		try {
			sendVerificationEmail(email, generateEmailBody(recipient, jwtToken))
		} catch (e: Exception) {
			logger.error { "Error sending email for registration verification to: $email ${e.message}" }
		}


	override fun sendSuccessfulRegistrationEmail(user: User) =
		try {
			sendRegistrationSuccessfulEmail(user, generateEmailBody(user))
		} catch (e: Exception) {
			logger.error { "Error sending email for successful registration to: ${user.email} ${e.message}" }
		}


	private fun sendVerificationEmail(to: String, text: String) =
		emailSender.send(SimpleMailMessage()
			.apply {
				setTo(to)
				setFrom(SENDER_EMAIL)
				setSubject(VERIFICATION_MAIL_SUBJECT)
				setText(text)
			}
			.also {
				logger.info { "Sent registration verification email: $it" }
			}
		)

	private fun sendRegistrationSuccessfulEmail(user: User, text: String) {
		emailSender.send(SimpleMailMessage()
			.apply {
				setTo(user.email)
				setFrom(SENDER_EMAIL)
				setSubject(REGISTRATION_SUCCESSFUL_MAIL_SUBJECT)
				setText(text)
			}
			.also {
				logger.info { "Sent registration successful email: $it" }
			}
		)
	}

	private fun generateEmailBody(fullName: String, jwtToken: String) = """"Hello, $fullName, 
					|welcome to the zoo-online.com. 
					|Please click here to confirm your registration: $VERIFICATION_URL$jwtToken
					|The zoo-online.com team
				""".trimMargin()

	private fun generateEmailBody(user: User) = """"Hello, ${user.firstName} ${user.lastName}, 
					|your account has been successfully registered. 
					|Your username is ${user.email}.
					|The zoo-online.com team
				""".trimMargin()

	companion object {
		const val VERIFICATION_URL = "http://localhost:8080$VERIFY_EMAIL_URL?token="
		const val VERIFICATION_MAIL_SUBJECT = "[zoo-online.com] Verify registration"
		const val REGISTRATION_SUCCESSFUL_MAIL_SUBJECT = "[zoo-online.com] Successful registration"
		const val SENDER_EMAIL = "no_reply@zoo-online.com"
	}
}


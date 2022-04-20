package tech.cordona.zooonline.security.mail.service

import mu.KotlinLogging
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import tech.cordona.zooonline.security.authentication.controller.AuthenticationController.Companion.VERIFY_EMAIL_URL

@Service
class EmailServiceImpl(private val emailSender: JavaMailSender) : EmailService {

	val logger = KotlinLogging.logger {}

	override fun sendVerifyRegistrationEmail(email: String, recipient: String, jwtToken: String) {
		try {
			sendEmail(email, generateEmailBody(recipient, jwtToken))
		} catch (e: Exception) {
			logger.error { "Error sending verify registration email to: $email ${e.message}" }
		}
	}

	private fun sendEmail(to: String, text: String) =
		emailSender.send(SimpleMailMessage()
			.apply {
				setTo(to)
				setFrom(SENDER_EMAIL)
				setSubject(VERIFICATION_MAIL_SUBJECT)
				setText(text)
			}
			.also {
				logger.info { "Sent: $it" }
			}
		)

	private fun generateEmailBody(fullName: String, jwtToken: String) = """"Hello, $fullName, 
					|welcome to the zoo-online.com. 
					|Please click here to confirm your registration: $VERIFICATION_URL$jwtToken
					|The zoo-online.com team
				""".trimMargin()

	companion object {
		const val VERIFICATION_URL = "http://localhost:8080$VERIFY_EMAIL_URL?token="
		const val VERIFICATION_MAIL_SUBJECT = "[zoo-online.com] Verify registration"
		const val SENDER_EMAIL = "no_reply@zoo-online.com"
	}
}


package tech.cordona.zooonline.security.mail.service

interface EmailService {
	fun sendVerifyRegistrationEmail(email: String, recipient: String, jwtToken: String)
}
package tech.cordona.zooonline.security.mail.service

import tech.cordona.zooonline.security.user.entity.User

interface EmailService {
	fun sendVerifyRegistrationEmail(email: String, recipient: String, jwtToken: String)
	fun sendSuccessfulRegistrationEmail(user: User)
}
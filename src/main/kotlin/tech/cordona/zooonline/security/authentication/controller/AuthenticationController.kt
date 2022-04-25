package tech.cordona.zooonline.security.authentication.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.security.authentication.service.AuthenticationService
import tech.cordona.zooonline.security.user.entity.extention.UserExtension.asModel
import tech.cordona.zooonline.security.user.model.UserModel
import javax.validation.Valid

@RestController
class AuthenticationController(
	private val authenticationService: AuthenticationService
) {

	@PostMapping(REGISTER_URL)
	fun register(@Valid @RequestBody newUser: UserModel) = authenticationService.register(newUser).asModel()

	@PostMapping(VERIFY_EMAIL_URL)
	fun verifyEmail(@RequestParam token: String) = authenticationService.verifyEmail(token)

	companion object {
		const val REGISTER_URL = "/register"
		const val VERIFY_EMAIL_URL = "/verify-email"
		const val LOGIN_URL = "/login"
	}
}


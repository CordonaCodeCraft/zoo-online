package tech.cordona.zooonline.domain.admin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.domain.admin.controller.AdminController.Companion.ADMIN_BASE_URL
import tech.cordona.zooonline.security.annotation.IsAdmin

@IsAdmin
@RestController
@RequestMapping(ADMIN_BASE_URL)
class AdminController {

	@GetMapping("/users")
	fun getAllUsers() = "Works"

	companion object {
		const val ADMIN_BASE_URL = "/admin"
	}
}

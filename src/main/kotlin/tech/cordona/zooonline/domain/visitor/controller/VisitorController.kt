package tech.cordona.zooonline.domain.visitor.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.security.annotation.IsUser

@RestController
class VisitorController {

	@IsUser
	@GetMapping(AREAS_URL)
	fun getAreas() = "Works"

	companion object {
		const val AREAS_URL = "/visitor/areas"
	}
}
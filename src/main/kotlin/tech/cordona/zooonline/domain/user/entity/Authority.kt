package tech.cordona.zooonline.domain.user.entity

import org.springframework.security.core.GrantedAuthority

enum class Authority : GrantedAuthority {
	USER,
	MANAGER,
	ADMIN,
	TRAINER,
	DOCTOR,
	GUARD,
	EMAIL_VERIFY;

	override fun getAuthority() = this.name
}
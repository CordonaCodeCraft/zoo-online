package tech.cordona.zooonline.security.user.entity

import org.springframework.security.core.GrantedAuthority

enum class Authority : GrantedAuthority {
	USER,
	MANAGER,
	ADMIN,
	TRAINER,
	DOCTOR,
	EMAIL_VERIFY;

	override fun getAuthority() = this.name
}
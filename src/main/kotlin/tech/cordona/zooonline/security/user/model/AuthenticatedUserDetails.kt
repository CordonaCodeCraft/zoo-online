package tech.cordona.zooonline.security.user.model

import org.bson.types.ObjectId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import tech.cordona.zooonline.security.user.entity.Authority

data class AuthenticatedUserDetails(
	val id: ObjectId,
	val email: String,
	val userPassword: String,
	val authority: Authority
) : UserDetails {
	override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(authority)
	override fun getPassword() = userPassword
	override fun getUsername() = email
	override fun isAccountNonLocked() = true
	override fun isAccountNonExpired() = true
	override fun isCredentialsNonExpired() = true
	override fun isEnabled() = true
}

package tech.cordona.zooonline.security.jwt.service

import tech.cordona.zooonline.security.dto.JwtTokenInfo
import tech.cordona.zooonline.domain.user.model.AuthenticatedUserDetails

interface JwtTokenService {
	fun createEmailVerificationToken(email: String, id: String): String
	fun createLoginToken(principal: AuthenticatedUserDetails): String
	fun decodeToken(token: String): JwtTokenInfo
}
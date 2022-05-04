package tech.cordona.zooonline.security.jwt.service

import tech.cordona.zooonline.domain.user.model.AuthenticatedUserDetails
import tech.cordona.zooonline.security.dto.JwtTokenInfo

interface JwtTokenService {
	fun createEmailVerificationToken(email: String, id: String): String
	fun createLoginToken(principal: AuthenticatedUserDetails): String
	fun decodeToken(token: String): JwtTokenInfo
}
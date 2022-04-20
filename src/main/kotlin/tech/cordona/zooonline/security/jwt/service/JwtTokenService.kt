package tech.cordona.zooonline.security.jwt.service

import tech.cordona.zooonline.security.jwt.dto.JwtTokenInfo

interface JwtTokenService {
	fun createEmailVerificationToken(email: String, id: String) : String
	fun decodeToken(token: String): JwtTokenInfo
}
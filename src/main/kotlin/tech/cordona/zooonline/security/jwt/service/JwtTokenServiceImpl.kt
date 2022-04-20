package tech.cordona.zooonline.security.jwt.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.stereotype.Component
import tech.cordona.zooonline.security.jwt.dto.JwtTokenInfo
import tech.cordona.zooonline.security.user.entity.Authority.EMAIL_VERIFY
import java.util.*

@Component
class JwtTokenServiceImpl : JwtTokenService {

	private val algorithm = Algorithm.HMAC256(SECRET.toByteArray())

	override fun createEmailVerificationToken(email: String, id: String) = createJwtToken(email = email, id = id)

	override fun decodeToken(token: String): JwtTokenInfo {
		try {
			val jwt = JWT
				.require(Algorithm.HMAC256(SECRET))
				.build()
				.verify(token)
			return JwtTokenInfo(
				id = jwt.getClaim(ID).asString(),
				authority = jwt.getClaim(ROLE).asString(),
				email = jwt.subject
			)
		} catch (e: Exception) {
			throw JWTVerificationException("Failed to parse JWT", e)
		}
	}

	private fun createJwtToken(
		email: String,
		id: String,
	): String = JWT.create()
		.withSubject(email)
		.withClaim(ID, id)
		.withClaim(ROLE, EMAIL_VERIFY.name)
		.withExpiresAt(Date(System.currentTimeMillis() + MAIL_VERIFY_EXPIRATION_PERIOD))
		.sign(algorithm)

	companion object {
		const val ID = "id"
		const val ROLE = "role"
		const val MAIL_VERIFY_EXPIRATION_PERIOD = 172800000
		const val SECRET = "TOP-SECRET"
	}
}
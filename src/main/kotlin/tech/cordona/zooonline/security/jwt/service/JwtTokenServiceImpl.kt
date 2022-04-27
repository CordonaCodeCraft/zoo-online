package tech.cordona.zooonline.security.jwt.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import org.springframework.stereotype.Component
import tech.cordona.zooonline.security.dto.JwtTokenInfo
import tech.cordona.zooonline.security.user.entity.Authority
import tech.cordona.zooonline.security.user.entity.Authority.EMAIL_VERIFY
import tech.cordona.zooonline.security.user.model.AuthenticatedUserDetails
import java.util.Date

@Component
class JwtTokenServiceImpl : JwtTokenService {

	private val algorithm = Algorithm.HMAC256(SECRET.toByteArray())

	override fun createEmailVerificationToken(email: String, id: String) = createJwtToken(
		email = email,
		id = id,
		authority = EMAIL_VERIFY
	)

	override fun createLoginToken(principal: AuthenticatedUserDetails) = createJwtToken(
		email = principal.email,
		id = principal.id.toString(),
		authority = principal.authority
	)

	override fun decodeToken(token: String): JwtTokenInfo =
		try {
			val jwt = JWT
				.require(Algorithm.HMAC256(SECRET))
				.build()
				.verify(token)

			JwtTokenInfo(
				id = jwt.getClaim(ID).asString(),
				authority = getAuthority(jwt.getClaim(ROLE)),
				email = jwt.subject
			)
		} catch (e: Exception) {
			throw JWTVerificationException("Failed to parse JWT", e)
		}

	private fun createJwtToken(
		email: String,
		id: String,
		authority: Authority
	): String = JWT.create()
		.withSubject(email)
		.withClaim(ID, id)
		.withClaim(ROLE, authority.name)
		.withExpiresAt(Date(System.currentTimeMillis() + MAIL_VERIFY_EXPIRATION_PERIOD))
		.sign(algorithm)

	private fun getAuthority(claim: Claim) = Authority.values().first { a -> a.name == claim.asString() }

	companion object {
		const val ID = "id"
		const val ROLE = "role"
		const val MAIL_VERIFY_EXPIRATION_PERIOD = 172800000
		const val SECRET = "TOP-SECRET"
		const val TOKEN_PREFIX = "Bearer "
	}
}
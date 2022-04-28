package tech.cordona.zooonline.security.jwt.filters

import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import tech.cordona.zooonline.security.dto.LoginRequest
import tech.cordona.zooonline.security.jwt.service.JwtTokenService
import tech.cordona.zooonline.security.jwt.service.JwtTokenServiceImpl.Companion.TOKEN_PREFIX
import tech.cordona.zooonline.security.user.model.AuthenticatedUserDetails
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
	private val authManager: AuthenticationManager,
	private val jwtTokenService: JwtTokenService
) : UsernamePasswordAuthenticationFilter() {

	private val logging = KotlinLogging.logger {}

	override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse): Authentication =
		getCredentialsFromRequest(req)
			.also { logging.info("Attempt authentication: ${it.username}") }
			.let { request -> UsernamePasswordAuthenticationToken(request.username, request.password) }
			.let { authManager.authenticate(it) }

	override fun successfulAuthentication(
		request: HttpServletRequest,
		response: HttpServletResponse,
		chain: FilterChain,
		authentication: Authentication
	) {
		authentication.principal
			.let { principal -> principal as AuthenticatedUserDetails }
			.let { authenticatedUser -> jwtTokenService.createLoginToken(authenticatedUser) }
			.also { response.addHeader(HttpHeaders.AUTHORIZATION, "$TOKEN_PREFIX$it") }
			.also { logging.info { "Successful authentication. Authorization token created: $it" } }
	}

	private fun getCredentialsFromRequest(request: HttpServletRequest) =
		LoginRequest(
			username = request.getParameter("username"),
			password = request.getParameter("password")
		)
}
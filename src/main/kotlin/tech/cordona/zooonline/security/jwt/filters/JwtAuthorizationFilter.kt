package tech.cordona.zooonline.security.jwt.filters

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import tech.cordona.zooonline.security.jwt.service.JwtTokenService
import tech.cordona.zooonline.security.user.mapper.Extensions.extractJwtToken
import tech.cordona.zooonline.security.user.mapper.Extensions.isAuthorizationHeader
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(private val jwtTokenService: JwtTokenService) : OncePerRequestFilter() {

	private val logging = KotlinLogging.logger {}

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		request.getHeader(HttpHeaders.AUTHORIZATION).takeIf { it.isAuthorizationHeader() }
			?.also { header -> attemptAuthorization(header, request, response, filterChain) }
			?: run { filterChain.doFilter(request, response) }
	}

	private fun attemptAuthorization(
		header: String,
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		try {
			header.extractJwtToken()
				.let { token -> jwtTokenService.decodeToken(token) }
				.let { tokenInfo ->
					UsernamePasswordAuthenticationToken(tokenInfo.email, null, listOf(tokenInfo.authority))
				}
				.also { authToken -> SecurityContextHolder.getContext().authentication = authToken }
				.also {
					logging.info { "${it.principal} successfully authorized" }
					filterChain.doFilter(request, response)
				}
		} catch (e: Exception) {
			response
				.apply {
					setHeader("error", e.message)
					status = HttpStatus.FORBIDDEN.value()
					contentType = MediaType.APPLICATION_JSON_VALUE
				}
				.also {
					ObjectMapper().writeValue(it.outputStream, mapOf("error_message" to e.message))
					logging.error("Authorization failed: ${e.message}")
				}
		}
	}
}
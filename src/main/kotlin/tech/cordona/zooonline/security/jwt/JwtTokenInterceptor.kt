package tech.cordona.zooonline.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.web.servlet.HandlerInterceptor
import tech.cordona.zooonline.extension.StringExtension.extractJwtToken
import tech.cordona.zooonline.extension.StringExtension.isAuthorizationHeader
import tech.cordona.zooonline.extension.StringExtension.isGoodFor
import tech.cordona.zooonline.security.dto.TokenWrapper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenInterceptor(private val tokenWrapper: TokenWrapper) : HandlerInterceptor {

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any) =
		request.getHeader(HttpHeaders.AUTHORIZATION).takeIf { it.isAuthorizationHeader() }
			?.run {
				this.extractJwtToken().takeIf { it.isGoodFor(tokenWrapper) }?.run { tokenWrapper.token = this }
			}.let { true }
}
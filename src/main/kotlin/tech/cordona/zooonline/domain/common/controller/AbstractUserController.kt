package tech.cordona.zooonline.domain.common.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tech.cordona.zooonline.security.dto.TokenWrapper
import tech.cordona.zooonline.security.jwt.service.JwtTokenServiceImpl

@Component
abstract class AbstractUserController {

	@Autowired
	private lateinit var jwtTokenService: JwtTokenServiceImpl

	@Autowired
	private lateinit var tokenWrapper: TokenWrapper

	protected fun getUserId() = jwtTokenService.decodeToken(tokenWrapper.token).id
}
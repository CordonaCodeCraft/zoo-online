package tech.cordona.zooonline.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import tech.cordona.zooonline.security.dto.TokenWrapper
import tech.cordona.zooonline.security.jwt.JwtTokenInterceptor

@Configuration
class WebMvcConfig : WebMvcConfigurer {

	@Bean
	fun tokenWrapper() = TokenWrapper()

	@Bean
	fun jwtTokenInterceptor() = JwtTokenInterceptor(tokenWrapper())

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(jwtTokenInterceptor())
	}
}
package tech.cordona.zooonline.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class ApplicationPasswordEncoder {
	@Bean
	fun passwordEncoder() = BCryptPasswordEncoder(12)
}
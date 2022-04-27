package tech.cordona.zooonline.security.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import tech.cordona.zooonline.security.authentication.controller.AuthenticationController.Companion.LOGIN_URL
import tech.cordona.zooonline.security.authentication.controller.AuthenticationController.Companion.REGISTER_URL
import tech.cordona.zooonline.security.authentication.controller.AuthenticationController.Companion.VERIFY_EMAIL_URL

@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

	override fun configure(http: HttpSecurity) {
		http.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(
				HttpMethod.POST, LOGIN_URL,
				REGISTER_URL,
				VERIFY_EMAIL_URL,
			)
			.permitAll()
	}
}
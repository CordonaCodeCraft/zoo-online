package tech.cordona.zooonline.security.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import tech.cordona.zooonline.domain.doctor.controller.DoctorController.Companion.DOCTOR_BASE_URL
import tech.cordona.zooonline.domain.manager.controller.ManagerController.Companion.MANAGER_BASE_URL
import tech.cordona.zooonline.domain.trainer.controller.TrainerController.Companion.TRAINER_BASE_URL
import tech.cordona.zooonline.domain.user.service.UserService
import tech.cordona.zooonline.domain.visitor.controller.VisitorController.Companion.VISITOR_BASE_URL
import tech.cordona.zooonline.security.authentication.controller.AuthenticationController.Companion.LOGIN_URL
import tech.cordona.zooonline.security.authentication.controller.AuthenticationController.Companion.REGISTER_URL
import tech.cordona.zooonline.security.authentication.controller.AuthenticationController.Companion.VERIFY_EMAIL_URL
import tech.cordona.zooonline.security.jwt.filters.JwtAuthenticationFilter
import tech.cordona.zooonline.security.jwt.filters.JwtAuthorizationFilter
import tech.cordona.zooonline.security.jwt.service.JwtTokenService


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
	securedEnabled = true,
	jsr250Enabled = true,
	prePostEnabled = true
)
class WebSecurityConfig(
	private val userService: UserService,
	private val passwordEncoder: BCryptPasswordEncoder,
	private val jwtTokenService: JwtTokenService
) : WebSecurityConfigurerAdapter() {

	override fun configure(auth: AuthenticationManagerBuilder) {
		auth
			.userDetailsService(userService)
			.passwordEncoder(passwordEncoder)
	}

	override fun configure(http: HttpSecurity) {
		val jwtAuthenticationFilter =
			JwtAuthenticationFilter(authenticationManagerBean(), jwtTokenService).apply {
				setFilterProcessesUrl(LOGIN_URL)
			}
		http.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(
				HttpMethod.POST,
				REGISTER_URL,
				VERIFY_EMAIL_URL,
				LOGIN_URL
			).permitAll()
			.antMatchers(
				HttpMethod.POST,
				"$MANAGER_BASE_URL/**",
				"$VISITOR_BASE_URL/**",
				"$TRAINER_BASE_URL/**",
				"$DOCTOR_BASE_URL/**",
			).permitAll()
			.antMatchers(
				HttpMethod.GET,
				"$MANAGER_BASE_URL/**",
				"$VISITOR_BASE_URL/**",
				"$TRAINER_BASE_URL/**",
				"$DOCTOR_BASE_URL/**",
			).permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(jwtAuthenticationFilter)
			.addFilterBefore(JwtAuthorizationFilter(jwtTokenService), UsernamePasswordAuthenticationFilter::class.java)
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	}

	@Bean
	override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()
}
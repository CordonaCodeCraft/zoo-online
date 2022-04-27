package tech.cordona.zooonline.config.mongo

import org.springframework.data.domain.AuditorAware
import java.util.*

class AuditorAwareImpl : AuditorAware<String> {
	override fun getCurrentAuditor(): Optional<String> = Optional.of("Cordona")
}
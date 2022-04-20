package tech.cordona.zooonline

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class Runner(private val mongoTemplate: MongoTemplate) : ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		resetUsers(true)
	}

	private fun resetUsers(reset: Boolean) {
		if (reset) {
			mongoTemplate.dropCollection("Users")
		}
	}
}
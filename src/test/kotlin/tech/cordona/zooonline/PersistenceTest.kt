package tech.cordona.zooonline

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container

@SpringBootTest
@TestPropertySource(properties = ["mongock.enabled=false"])

abstract class PersistenceTest {
	companion object {
		@Container
		val container: MongoDBContainer = MongoDBContainer("mongo:latest")
			.apply {
				start()
				withReuse(true)
			}

		@JvmStatic
		@DynamicPropertySource
		fun dataSourceConfig(registry: DynamicPropertyRegistry) =
			registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl)
	}
}
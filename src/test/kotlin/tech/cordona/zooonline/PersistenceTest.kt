package tech.cordona.zooonline

import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = ["mongock.enabled=false"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

abstract class PersistenceTest {
	companion object {
		@Container
		val container: MongoDBContainer = MongoDBContainer("mongo:latest").also { it.start() }

		@JvmStatic
		@DynamicPropertySource
		fun dataSourceConfig(registry: DynamicPropertyRegistry) {
			registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl)
		}
	}
}
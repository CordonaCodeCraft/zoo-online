package tech.cordona.zooonline

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Testcontainers
import tech.cordona.zooonline.testcontainers.MongoTestContainer

@SpringBootTest
@Testcontainers
abstract class AbstractTest {

	companion object {
		@JvmStatic
		@DynamicPropertySource
		fun dataSourceConfig(registry: DynamicPropertyRegistry) {
			registry.add("spring.data.mongodb.uri", MongoTestContainer.container::getReplicaSetUrl)
		}
	}

}
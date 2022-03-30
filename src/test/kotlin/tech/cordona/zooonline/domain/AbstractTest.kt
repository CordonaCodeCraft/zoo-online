package tech.cordona.zooonline.domain

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Testcontainers
import tech.cordona.zooonline.domain.tiger.service.objects.MongoTestContainer

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
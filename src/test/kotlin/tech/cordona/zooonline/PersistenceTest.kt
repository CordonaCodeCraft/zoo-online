package tech.cordona.zooonline

import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import tech.cordona.zooonline.common.TestAssets.validGraphOfTaxonomyUnits
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService

@SpringBootTest
@TestPropertySource(properties = ["mongock.enabled=false"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class PersistenceTest {

	@Autowired lateinit var taxonomyUnitService: TaxonomyUnitService

	fun givenPersistedTaxonomyUnits(vararg units: TaxonomyUnit) =
		mutableListOf(validGraphOfTaxonomyUnits, units.toList())
			.flatten()
			.let { newUnits -> taxonomyUnitService.createMany(newUnits) }

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
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
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.doctor.service.DoctorService
import tech.cordona.zooonline.domain.guard.service.GuardService
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.domain.trainer.service.TrainerService
import tech.cordona.zooonline.domain.user.service.UserService

@SpringBootTest
@TestPropertySource(properties = ["mongock.enabled=false"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class PersistenceTest {

	@Autowired lateinit var taxonomyUnitService: TaxonomyUnitService
	@Autowired lateinit var animalService: AnimalService
	@Autowired lateinit var cellService: CellService
	@Autowired lateinit var areaService: AreaService
	@Autowired lateinit var trainerService: TrainerService
	@Autowired lateinit var doctorService: DoctorService
	@Autowired lateinit var guardService: GuardService
	@Autowired lateinit var userService: UserService

	fun persistTaxonomyUnits(vararg units: TaxonomyUnit) =
		mutableListOf(validGraphOfTaxonomyUnits, units.toList())
			.flatten()
			.let { newUnits -> taxonomyUnitService.createMany(newUnits)  }


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
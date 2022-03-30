package tech.cordona.zooonline.domain.tiger.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.domain.AbstractTest
import tech.cordona.zooonline.domain.tiger.entity.Tiger
import tech.cordona.zooonline.domain.tiger.service.objects.MongoTestContainer.container


internal class TigerServiceMongoImplTest @Autowired constructor(val service: TigerServiceMongoImpl) : AbstractTest() {

	@Test
	@DisplayName("Container is up and running")
	fun `container is up and running`() {
		assertThat(container.isCreated)
		assertThat(container.isRunning)
	}

	@Test
	@DisplayName("Service properly persists data")
	fun `service properly persists data`() {
		// given
		val first = Tiger("Joe", "Cat")
		val second = Tiger("Buck", "Big cat")

		// when
		service.saveAll(listOf(first, second))
		val retrieved = service.getAll()

		// then
		assertThat(retrieved.size).isEqualTo(2)
	}

}
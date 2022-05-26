package tech.cordona.zooonline.testcontainers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tech.cordona.zooonline.PersistenceTest.Companion.container

class TestContainersTest {
	@Test
	@DisplayName("Container is up and running")
	fun `container is up and running`() {
		assertThat(container.isCreated)
		assertThat(container.isRunning)
	}
}
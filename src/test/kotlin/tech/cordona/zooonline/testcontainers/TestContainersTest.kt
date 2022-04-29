package tech.cordona.zooonline.testcontainers

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tech.cordona.zooonline.PersistenceTest

class TestContainersTest : PersistenceTest() {

	@Test
	@DisplayName("Container is up and running")
	fun `container is up and running`() {
		Assertions.assertThat(container.isCreated)
		Assertions.assertThat(container.isRunning)
	}
}
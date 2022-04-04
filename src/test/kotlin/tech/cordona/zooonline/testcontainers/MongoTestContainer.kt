package tech.cordona.zooonline.testcontainers

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container

object MongoTestContainer {
	@Container
	val container: MongoDBContainer = MongoDBContainer("mongo:latest")

	init {
		container.start()
	}
}
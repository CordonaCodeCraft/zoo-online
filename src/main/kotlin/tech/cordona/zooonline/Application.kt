package tech.cordona.zooonline

import io.mongock.runner.springboot.EnableMongock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@SpringBootApplication
@EnableMongock
@EnableMongoRepositories(
	basePackages = [
		"tech.cordona.zooonline.domain.animal.repository",
		"tech.cordona.zooonline.domain.area.repository",
		"tech.cordona.zooonline.domain.cell.repository",
		"tech.cordona.zooonline.domain.taxonomy.repository",
		"tech.cordona.zooonline.security.user.repository",
		"tech.cordona.zooonline.domain.visitor.repository",
		"tech.cordona.zooonline.domain.trainer.repository",
		"tech.cordona.zooonline.domain.doctor.repository",
		"tech.cordona.zooonline.domain.guard.repository"
	]
)
class ZooOnlineApplication

fun main(args: Array<String>) {
	runApplication<ZooOnlineApplication>(*args)
}



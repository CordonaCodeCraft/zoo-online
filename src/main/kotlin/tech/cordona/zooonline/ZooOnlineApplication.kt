package tech.cordona.zooonline

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZooOnlineApplication

fun main(args: Array<String>) {
	runApplication<ZooOnlineApplication>(*args)
}

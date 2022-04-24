package tech.cordona.zooonline.domain.area.service

import tech.cordona.zooonline.domain.area.entity.Area

interface AreaService {
	fun saveAll(areas: List<Area>): List<Area>
	fun findAll(): List<Area>
	fun findAreaByAnimalType(animalType: String): Area
	fun deleteAll()
}
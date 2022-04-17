package tech.cordona.zooonline.domain.area.service

import tech.cordona.zooonline.domain.area.entity.Area

interface AreaService {
	fun saveAllAreas(areas: List<Area>): List<Area>
	fun findAreaByAnimalType(type: String): Area
	fun findAreasByAnimalTypes(types: List<String>): List<Area>
	fun findAllAreas(): List<Area>
	fun deleteAllAreas()
}
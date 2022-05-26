package tech.cordona.zooonline.domain.area.service

import tech.cordona.zooonline.domain.area.entity.Area

interface AreaService {
	fun create(newArea: Area): Area
	fun update(updatedArea: Area): Area
	fun createMany(newAreas: List<Area>): List<Area>
	fun findAll(): List<Area>
	fun findAreaByName(name: String): Area
	fun findAllByNames(names: List<String>): List<Area>
	fun deleteAll()
}
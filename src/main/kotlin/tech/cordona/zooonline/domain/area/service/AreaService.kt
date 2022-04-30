package tech.cordona.zooonline.domain.area.service

import tech.cordona.zooonline.domain.area.entity.Area

interface AreaService {
	fun create(area: Area): Area
	fun createMany(areas: List<Area>): List<Area>
	fun findAll(): List<Area>
	fun findAreaByName(name: String): Area
	fun findAllByNames(names: List<String>): List<Area>
	fun deleteAll()
}
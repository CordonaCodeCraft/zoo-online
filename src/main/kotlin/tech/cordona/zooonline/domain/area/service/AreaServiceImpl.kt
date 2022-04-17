package tech.cordona.zooonline.domain.area.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.repository.AreaRepository

@Service
class AreaServiceImpl(private val repository: AreaRepository) : AreaService {

	override fun saveAllAreas(areas: List<Area>): List<Area> = repository.saveAll(areas)

	override fun findAreaByAnimalType(type: String): Area = repository.findByAnimalType(type)

	override fun findAreasByAnimalTypes(types: List<String>): List<Area> =
		types.map { type -> repository.findByAnimalType(type) }

	override fun findAllAreas(): List<Area> = repository.findAll()

	override fun deleteAllAreas() = repository.deleteAll()
}
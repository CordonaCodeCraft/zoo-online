package tech.cordona.zooonline.domain.area.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.repository.AreasRepository

@Service
class AreaServiceImpl(private val repository: AreasRepository) : AreaService {

	override fun saveAll(areas: List<Area>): List<Area> = repository.saveAll(areas)

	override fun findAll(): List<Area> = repository.findAll()

	override fun findAreaByAnimalType(name: String) = repository.findByName(name)
		?: throw IllegalArgumentException("Animal type $name does not exist")

	override fun deleteAll() = repository.deleteAll()
}
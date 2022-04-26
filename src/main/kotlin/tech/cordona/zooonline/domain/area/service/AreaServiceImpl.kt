package tech.cordona.zooonline.domain.area.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.repository.AreasRepository

@Service
class AreaServiceImpl(private val repository: AreasRepository) : AreaService {

	override fun save(area: Area): Area = repository.save(area)

	override fun saveAll(areas: List<Area>): List<Area> = repository.saveAll(areas)

	override fun findAll(): List<Area> = repository.findAll()

	override fun findAreaByName(name: String) = repository.findByName(name)
		?: throw IllegalArgumentException("Animal type $name does not exist")

	override fun findAllByNames(names: List<String>): List<Area> = repository.findAllByNameIn(names)

	override fun deleteAll() = repository.deleteAll()
}
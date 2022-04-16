package tech.cordona.zooonline.domain.area.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.repository.AreaRepository

@Service
class AreaServiceImpl(private val repository: AreaRepository) : AreaService {
	override fun saveAll(areas: List<Area>): List<Area> = repository.saveAll(areas)
	override fun deleteAll() = repository.deleteAll()
}
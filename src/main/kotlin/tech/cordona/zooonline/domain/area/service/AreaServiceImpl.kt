package tech.cordona.zooonline.domain.area.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.repository.AreaRepository

@Service
class AreaServiceImpl @Autowired constructor(val repository: AreaRepository) : AreaService {
	override fun saveAll(areas: List<Area>): List<Area> {
		return repository.saveAll(areas)
	}
}
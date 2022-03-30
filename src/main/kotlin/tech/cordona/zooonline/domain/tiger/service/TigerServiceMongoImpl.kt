package tech.cordona.zooonline.domain.tiger.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.tiger.entity.Tiger
import tech.cordona.zooonline.domain.tiger.repositories.TigerMongoRepository

@Service
class TigerServiceMongoImpl @Autowired constructor(val repository: TigerMongoRepository): TigerService {
	override fun saveAll(entities: List<Tiger>): List<Tiger> {
		return repository.saveAll(entities)
	}

	override fun getAll(): List<Tiger> {
		return repository.findAll()
	}
}
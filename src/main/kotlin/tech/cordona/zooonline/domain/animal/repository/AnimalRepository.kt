package tech.cordona.zooonline.domain.animal.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.animal.entity.Animal

interface AnimalRepository : MongoRepository<Animal, String> {
	fun findById(id: ObjectId): Animal?
}
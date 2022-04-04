package tech.cordona.zooonline.domain.animals.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import tech.cordona.zooonline.domain.animals.entity.Animal

@Repository
interface AnimalRepository : MongoRepository<Animal, String> {
}
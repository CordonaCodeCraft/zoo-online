package tech.cordona.zooonline.domain.animal.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import tech.cordona.zooonline.domain.animal.entity.Animal

@Repository
interface AnimalRepository : MongoRepository<Animal, String> {
}
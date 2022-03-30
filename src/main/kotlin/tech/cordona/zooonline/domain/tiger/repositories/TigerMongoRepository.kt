package tech.cordona.zooonline.domain.tiger.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import tech.cordona.zooonline.domain.tiger.entity.Tiger

@Repository
interface TigerMongoRepository : MongoRepository<Tiger, String> {
}
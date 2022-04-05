package tech.cordona.zooonline.domain.area.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import tech.cordona.zooonline.domain.area.entity.Area

@Repository
interface AreaRepository : MongoRepository<Area, String>
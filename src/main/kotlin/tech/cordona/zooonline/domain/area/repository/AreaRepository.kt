package tech.cordona.zooonline.domain.area.repository

import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.area.entity.Area

interface AreaRepository : MongoRepository<Area, String>
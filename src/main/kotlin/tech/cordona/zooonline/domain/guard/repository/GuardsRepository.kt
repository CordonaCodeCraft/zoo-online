package tech.cordona.zooonline.domain.guard.repository

import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.guard.entity.Guard

interface GuardsRepository : MongoRepository<Guard, String>
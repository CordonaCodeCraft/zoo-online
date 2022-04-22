package tech.cordona.zooonline.domain.visitor.repository

import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.visitor.entity.Visitor

interface VisitorRepository : MongoRepository<Visitor, String>
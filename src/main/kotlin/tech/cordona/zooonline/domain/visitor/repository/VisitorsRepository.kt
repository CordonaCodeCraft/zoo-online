package tech.cordona.zooonline.domain.visitor.repository

import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.visitor.entity.Visitor

interface VisitorsRepository : MongoRepository<Visitor, String>
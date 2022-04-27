package tech.cordona.zooonline.domain.guard.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.guard.entity.Guard

interface GuardRepository : MongoRepository<Guard, String> {
	fun findGuardByUserId(userId: ObjectId): Guard?
	fun findById(guardId: ObjectId): Guard?
}
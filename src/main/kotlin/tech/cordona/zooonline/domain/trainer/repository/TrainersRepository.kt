package tech.cordona.zooonline.domain.trainer.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.trainer.entity.Trainer

interface TrainersRepository : MongoRepository<Trainer, String> {
	fun findByUserId(userId: ObjectId): Trainer?
}
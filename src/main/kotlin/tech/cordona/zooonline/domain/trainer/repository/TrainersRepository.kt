package tech.cordona.zooonline.domain.trainer.repository

import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.trainer.entity.Trainer

interface TrainersRepository : MongoRepository<Trainer, String>
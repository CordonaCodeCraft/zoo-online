package tech.cordona.zooonline.domain.trainer.service

import tech.cordona.zooonline.domain.trainer.entity.Trainer

interface TrainerService {
	fun deleteAll()
	fun create(newTrainer: Trainer): Trainer
}
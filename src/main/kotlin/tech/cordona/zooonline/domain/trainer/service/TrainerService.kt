package tech.cordona.zooonline.domain.trainer.service

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest
import tech.cordona.zooonline.domain.trainer.entity.Trainer

interface TrainerService {
	fun deleteAll()
	fun create(newTrainer: Trainer): Trainer
	fun findByTrainerId(trainerId: String): Trainer
	fun findByUserId(userId: String): Trainer
	fun trainAnimals(userId: String, animals: List<String>): List<Animal>
	fun reassignTrainer(request: ReassignEmployeeRequest): Trainer
}
package tech.cordona.zooonline.domain.trainer.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.Extensions.stringify
import tech.cordona.zooonline.domain.animal.entity.extention.AnimalExtension.train
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.trainer.entity.Trainer
import tech.cordona.zooonline.domain.trainer.repository.TrainersRepository

@Service
class TrainerServiceImpl(
	private val repository: TrainersRepository,
	private val animalService: AnimalService
) : TrainerService {

	val logging = KotlinLogging.logger {}

	override fun deleteAll() = repository.deleteAll()

	override fun create(newTrainer: Trainer) = repository.save(newTrainer)

	override fun findByUserId(userId: String): Trainer =
		repository.findByUserId(ObjectId(userId))
			?: run {
				logging.error { "Trainer with ID: $userId not found" }
				throw IllegalArgumentException("Trainer with ID: $userId not found")
			}

	override fun trainAnimals(userId: String, animals: List<String>) =
		animals
			.filter { findByUserId(userId).animals.stringify().contains(it) }
			.let { animalsIds -> animalService.findAllByIds(animalsIds) }
			.map { animal -> animal.train() }
			.also { trained -> animalService.saveAll(trained) }
}
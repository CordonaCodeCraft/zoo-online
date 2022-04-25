package tech.cordona.zooonline.domain.trainer.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.trainer.entity.Trainer
import tech.cordona.zooonline.domain.trainer.repository.TrainersRepository

@Service
class TrainerServiceImpl(private val repository: TrainersRepository) : TrainerService {

	override fun deleteAll() = repository.deleteAll()
	override fun create(newTrainer: Trainer) = repository.save(newTrainer)
}
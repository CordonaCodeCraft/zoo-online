package tech.cordona.zooonline.domain.trainer.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.Extensions.stringify
import tech.cordona.zooonline.domain.animal.entity.extention.AnimalExtension.toTrainer
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.common.controller.AbstractUserController
import tech.cordona.zooonline.domain.trainer.controller.TrainerController.Companion.TRAINER_BASE_URL
import tech.cordona.zooonline.domain.trainer.dto.TrainAnimalsRequest
import tech.cordona.zooonline.domain.trainer.service.TrainerService
import tech.cordona.zooonline.security.annotation.IsTrainer

@IsTrainer
@RestController
@RequestMapping(TRAINER_BASE_URL)
class TrainerController(
	private val trainerService: TrainerService,
	private val animalService: AnimalService
) : AbstractUserController() {

	@GetMapping("/my-animals")
	fun getAnimals() =
		trainerService.findByUserId(getUserId())
			.let { animalService.findAllByIds(it.animals.stringify()) }
			.map { it.toTrainer() }

	@GetMapping("/report-untrained")
	fun reportUntrainedAnimals() =
		trainerService.findByUserId(getUserId())
			.let { animalService.findAllByIds(it.animals.stringify()) }
			.filter { it.healthStatistics.trainingStatus == "Untrained" }
			.map { it.toTrainer() }

	@PostMapping("/train")
	fun trainAnimals(@RequestBody request: TrainAnimalsRequest) =
		trainerService.trainAnimals(getUserId(), request.animalsToBeTrained)
			.map { trained -> trained.toTrainer() }

	companion object {
		const val TRAINER_BASE_URL = "/trainer"
	}
}
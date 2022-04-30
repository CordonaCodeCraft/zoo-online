package tech.cordona.zooonline.domain.trainer.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.extension.train
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.extension.assignEmployee
import tech.cordona.zooonline.domain.area.entity.extension.removeEmployee
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest
import tech.cordona.zooonline.domain.trainer.entity.Trainer
import tech.cordona.zooonline.domain.trainer.entity.extension.reassigned
import tech.cordona.zooonline.domain.trainer.repository.TrainerRepository
import tech.cordona.zooonline.extension.stringify

@Service
class TrainerServiceImpl(
	private val repository: TrainerRepository,
	private val animalService: AnimalService,
	private val areaService: AreaService,
	private val cellService: CellService
) : TrainerService {

	val logging = KotlinLogging.logger {}

	override fun deleteAll() = repository.deleteAll()

	override fun create(newTrainer: Trainer) = repository.save(newTrainer)

	override fun findByTrainerId(trainerId: String): Trainer =
		repository.findById(ObjectId(trainerId))
			?: run {
				logging.error { "Trainer with ID: $trainerId not found" }
				throw IllegalArgumentException("Trainer with ID: $trainerId not found")
			}

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
			.also { trained -> animalService.createMany(trained) }

	override fun reassignTrainer(request: ReassignEmployeeRequest) =
		findByTrainerId(request.employeeId)
			.also { trainer ->
				areaService.findAreaByName(request.fromArea)
					.removeEmployee(request.position, trainer.id!!)
					.also { fromArea -> areaService.create(fromArea) }
			}
			.also { trainer ->
				areaService.findAreaByName(request.toArea)
					.assignEmployee(request.position, trainer.id!!)
					.also { toArea -> areaService.create(toArea) }
					.let { toArea ->
						repository.save(trainer.reassigned(toArea, getAnimals(toArea)))
					}
			}

	fun getAnimals(toArea: Area) =
		cellService.findAllById(toArea.cells.stringify())
			.map { cell -> cell.species }
			.flatten()
			.toMutableSet()
}
package tech.cordona.zooonline.domain.trainer.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.extension.train
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.extension.assignEmployee
import tech.cordona.zooonline.domain.area.entity.extension.removeEmployee
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.manager.dto.AssignEmployeeRequest
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest
import tech.cordona.zooonline.domain.trainer.entity.Trainer
import tech.cordona.zooonline.domain.trainer.entity.extension.assignTo
import tech.cordona.zooonline.domain.trainer.repository.TrainerRepository
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.extension.stringify
import tech.cordona.zooonline.validation.EntityValidator
import tech.cordona.zooonline.validation.FailReport.entityNotFound

@Service
class TrainerServiceImpl(
	private val repository: TrainerRepository,
	private val areaService: AreaService,
	private val cellService: CellService
) : TrainerService, EntityValidator() {

	val logging = KotlinLogging.logger {}

	override fun create(newTrainer: Trainer) = validate(newTrainer).let { repository.save(newTrainer) }

	override fun findByTrainerId(trainerId: String): Trainer =
		repository.findById(ObjectId(trainerId))
			?: run {
				logging.error { entityNotFound(entity = "Trainer", idType = "ID", id = trainerId) }
				throw EntityNotFoundException(entityNotFound(entity = "Trainer", idType = "ID", id = trainerId))
			}

	override fun findByUserId(userId: String): Trainer =
		repository.findByUserId(ObjectId(userId))
			?: run {
				logging.error { entityNotFound(entity = "User", idType = "ID", id = userId) }
				throw EntityNotFoundException(entityNotFound(entity = "User", idType = "ID", id = userId))
			}

	override fun trainAnimals(userId: String, animals: List<String>) =
		animals
			.filter { findByUserId(userId).animals.stringify().contains(it) }
			.let { animalsIds -> animalService.findAllByIds(animalsIds) }
			.map { animal -> animal.train() }
			.also { trained -> animalService.createMany(trained) }

	override fun assignTrainer(request: AssignEmployeeRequest) =
		findByTrainerId(request.employeeId)
			.also { trainer ->
				areaService.findAreaByName(request.toArea)
					.assignEmployee(request.position, trainer.id!!)
					.also { updatedArea -> areaService.update(updatedArea) }
					.also { updatedArea -> repository.save(trainer.assignTo(updatedArea, getAnimals(updatedArea))) }
			}
			.let { findByTrainerId(request.employeeId) }

	override fun reassignTrainer(request: ReassignEmployeeRequest) =
		findByTrainerId(request.employeeId)
			.also { trainer ->
				areaService.findAreaByName(request.fromArea)
					.removeEmployee(request.position, trainer.id!!)
					.also { fromArea -> areaService.update(fromArea) }
			}
			.also { trainer ->
				areaService.findAreaByName(request.toArea)
					.assignEmployee(request.position, trainer.id!!)
					.also { toArea -> areaService.update(toArea) }
					.also { toArea -> repository.save(trainer.assignTo(toArea, getAnimals(toArea))) }
			}
			.let { findByTrainerId(request.employeeId) }

	override fun deleteAll() = repository.deleteAll()

	fun getAnimals(toArea: Area) =
		cellService.findAllById(toArea.cells.stringify())
			.map { cell -> cell.species }
			.flatten()
			.toMutableSet()

	private fun validate(newTrainer: Trainer) =
		newTrainer
			.forUniqueUser()
			.forExistingUser()
			.forExistingArea()
			.withValidProperties()
}
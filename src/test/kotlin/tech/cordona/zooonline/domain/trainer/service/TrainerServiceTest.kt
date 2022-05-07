package tech.cordona.zooonline.domain.trainer.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.bson.types.ObjectId
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import tech.cordona.zooonline.common.TestAssets.andeanBearSpecie
import tech.cordona.zooonline.common.TestAssets.wrongID
import tech.cordona.zooonline.domain.animal.entity.enums.HealthStatus.SICK
import tech.cordona.zooonline.domain.animal.entity.enums.TrainingStatus
import tech.cordona.zooonline.domain.animal.entity.enums.TrainingStatus.TRAINED
import tech.cordona.zooonline.domain.animal.entity.enums.TrainingStatus.UNTRAINED
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.Companion.UNTRAINED_THRESHOLD
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.employee.EmployeeTests
import tech.cordona.zooonline.domain.manager.dto.AssignEmployeeRequest
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.CARNIVORE
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.ELEPHANT
import tech.cordona.zooonline.domain.trainer.entity.Trainer
import tech.cordona.zooonline.domain.user.entity.Authority.TRAINER
import tech.cordona.zooonline.domain.user.service.UserService
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.extension.stringify
import tech.cordona.zooonline.validation.FailReport
import tech.cordona.zooonline.validation.FailReport.entityNotFound
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_TRAINING_POINTS

internal class TrainerServiceTest(
	@Autowired private val userService: UserService,
	@Autowired private val trainerService: TrainerService,
	@Autowired private val areaService: AreaService,
	@Autowired private val animalService: AnimalService,
	@Autowired private val cellService: CellService,
) : EmployeeTests(userService, trainerService, areaService, animalService, cellService) {

	@Nested
	@DisplayName("Trainer creation tests")
	inner class TrainerCreation {
		@Test
		@DisplayName("Throws when creating trainer for already existing user")
		fun `Throws when creating trainer for already existing user`() {
			createTrainer()
				.run {
					assertThatExceptionOfType(InvalidEntityException::class.java)
						.isThrownBy { trainerService.create(employee.copy(userId = userId)) }
						.withMessage(FailReport.existingUserId(this.userId.toString()))
				}
		}
	}

	@Nested
	@DisplayName("Trainer retrieval tests")
	inner class TrainerRetrieval {

		@Test
		@DisplayName("Successfully retrieves trainer by trainer ID")
		fun `successfully retrieves trainer by trainer ID`() {
			createTrainer()
				.let { created ->
					trainerService.findByTrainerId(created.id.toString())
				}
				.run {
					assertThat(this.firstName).isEqualTo(employee.firstName)
					assertThat(this.middleName).isEqualTo(employee.middleName)
					assertThat(this.lastName).isEqualTo(employee.lastName)
				}
		}

		@Test
		@DisplayName("Successfully retrieves trainer by user ID")
		fun `successfully retrieves trainer by user ID`() {
			createTrainer()
				.let { created ->
					trainerService.findByUserId(created.userId.toString())
				}
				.run {
					assertThat(this.firstName).isEqualTo(employee.firstName)
					assertThat(this.middleName).isEqualTo(employee.middleName)
					assertThat(this.lastName).isEqualTo(employee.lastName)
				}
		}

		@Test
		@DisplayName("Throws when retrieves trainer by wrong trainer ID")
		fun `throws when retrieves trainer by wrong trainer ID`() {
			createTrainer()
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { trainerService.findByTrainerId(wrongID.toString()) }
				.withMessageContaining(entityNotFound(entity = "Trainer", idType = "ID", id = wrongID.toString()))
		}

		@Test
		@DisplayName("Throws when retrieves trainer by wrong user ID")
		fun `throws when retrieves trainer by wrong user ID`() {
			createTrainer()
			assertThatExceptionOfType(EntityNotFoundException::class.java)
				.isThrownBy { trainerService.findByUserId(wrongID.toString()) }
				.withMessageContaining(entityNotFound(entity = "User", idType = "ID", id = wrongID.toString()))
		}
	}

	@Nested
	@DisplayName("Animals training tests")
	inner class AnimalsTraining {

		@Test
		@DisplayName("Trains untrained animal and keeps status to Untrained")
		fun `trains untrained animal and keeps status to Untrained`() {
			val untrained = createAnimal(UNTRAINED_THRESHOLD - 1, UNTRAINED)
			val trainer = createTrainer(animals = mutableSetOf(untrained.id!!))
			val trained = train(trainer.userId, listOf(untrained.id!!)).first()

			assertThat(trained.taxonomyDetails.name).isEqualTo(untrained.taxonomyDetails.name)
			assertThat(trained.healthStatistics.trainingPoints).isEqualTo(untrained.healthStatistics.trainingPoints + 1)
			assertThat(trained.healthStatistics.trainingStatus).isEqualTo(UNTRAINED.name.asTitlecase())
		}

		@Test
		@DisplayName("Trains untrained animal and changes status to Trained")
		fun `trains untrained animal and changes status to Trained`() {
			val untrained = createAnimal(UNTRAINED_THRESHOLD, UNTRAINED)
			val trainer = createTrainer(animals = mutableSetOf(untrained.id!!))
			val trained = train(trainer.userId, listOf(untrained.id!!)).first()

			assertThat(trained.taxonomyDetails.name).isEqualTo(untrained.taxonomyDetails.name)
			assertThat(trained.healthStatistics.trainingPoints).isEqualTo(untrained.healthStatistics.trainingPoints + 1)
			assertThat(trained.healthStatistics.trainingStatus).isEqualTo(TRAINED.name.asTitlecase())
		}

		@Test
		@DisplayName("Trains trained animal and keeps status to Trained")
		fun `trains trained animal and keeps status to Trained`() {
			val untrained = createAnimal(MAX_TRAINING_POINTS - 1, TRAINED)
			val trainer = createTrainer(animals = mutableSetOf(untrained.id!!))
			val trained = train(trainer.userId, listOf(untrained.id!!)).first()

			assertThat(trained.taxonomyDetails.name).isEqualTo(untrained.taxonomyDetails.name)
			assertThat(trained.healthStatistics.trainingPoints).isEqualTo(untrained.healthStatistics.trainingPoints + 1)
			assertThat(trained.healthStatistics.trainingStatus).isEqualTo(TRAINED.name.asTitlecase())
		}

		@Test
		@DisplayName("Does not exceed animal's training points threshold")
		fun `Does not exceed animal's training points threshold`() {
			val untrained = createAnimal(MAX_TRAINING_POINTS, TRAINED)
			val trainer = createTrainer(animals = mutableSetOf(untrained.id!!))
			val trained = train(trainer.userId, listOf(untrained.id!!)).first()

			assertThat(trained.taxonomyDetails.name).isEqualTo(untrained.taxonomyDetails.name)
			assertThat(trained.healthStatistics.trainingPoints).isEqualTo(untrained.healthStatistics.trainingPoints)
			assertThat(trained.healthStatistics.trainingStatus).isEqualTo(TRAINED.name.asTitlecase())
		}

		@Test
		@DisplayName("Trains only animals, assigned to trainer")
		fun `Trains only animals, assigned to trainer`() {
			val first = createAnimal(UNTRAINED_THRESHOLD, UNTRAINED)
			val second = createAnimal(UNTRAINED_THRESHOLD, UNTRAINED)
			val third = createAnimal(UNTRAINED_THRESHOLD, UNTRAINED)

			val trainer = createTrainer(animals = mutableSetOf(first.id!!, second.id!!))
			val trained = train(trainer.userId, listOf(first.id!!, second.id!!, third.id!!))

			assertThat(trained.size).isEqualTo(2)
		}
	}

	@Nested
	@DisplayName("Trainer assignment tests")
	inner class TrainerReassignment {

		@Test
		@DisplayName("Successfully assigns trainer")
		fun `successfully assigns trainer`() {

			val trainer = createTrainer()

			val request =
				createAssignmentRequest(
					employeeId = trainerId,
					to = ELEPHANT.name.asTitlecase()
				)
			val notUpdatedArea = areaService.findAreaByName(request.toArea)

			val assignedTrainer = trainerService.assignTrainer(request)

			val updatedArea = areaService.findAreaByName(request.toArea)

			val animals = cellService.findAllById(updatedArea.cells.stringify())
				.map { cell -> cell.species }
				.flatten()
				.toMutableSet()

			assertThat(notUpdatedArea.staff.trainers).doesNotContain(trainer.id)
			assertThat(updatedArea.staff.trainers).contains(trainer.id)
			assertThat(assignedTrainer.area).isEqualTo(ELEPHANT.name.asTitlecase())
			assertThat(assignedTrainer.animals).isEqualTo(animals)
		}

		@Test
		@DisplayName("Successfully reassigns trainer")
		fun `successfully reassigns trainer`() {

			val trainer = createTrainer()

			val assignmentRequest =
				createAssignmentRequest(
					employeeId = trainerId,
					to = CARNIVORE.name.asTitlecase()
				)

			val assignTrainer = trainerService.assignTrainer(assignmentRequest)

			val fromArea = areaService.findAreaByName(CARNIVORE.name.asTitlecase())
			val toArea = areaService.findAreaByName(ELEPHANT.name.asTitlecase())

			val reassignmentRequest =
				createReassignmentRequest(
					employeeId = trainerId,
					from = CARNIVORE.name.asTitlecase(),
					to = ELEPHANT.name.asTitlecase()
				)

			val reassignedTrainer = trainerService.reassignTrainer(reassignmentRequest)

			val fromAreaUpdated = areaService.findAreaByName(CARNIVORE.name.asTitlecase())
			val toAreaUpdated = areaService.findAreaByName(ELEPHANT.name.asTitlecase())

			val fromAnimals = cellService.findAllById(fromArea.cells.stringify())
				.map { cell -> cell.species }
				.flatten()
				.toMutableSet()

			val toAnimals = cellService.findAllById(toArea.cells.stringify())
				.map { cell -> cell.species }
				.flatten()
				.toMutableSet()

			assertThat(fromArea.staff.trainers).contains(trainer.id)
			assertThat(fromAreaUpdated.staff.trainers).doesNotContain(trainer.id)
			assertThat(toArea.staff.trainers).doesNotContain(trainer.id)
			assertThat(toAreaUpdated.staff.trainers).contains(trainer.id)
			assertThat(assignTrainer.area).isEqualTo(CARNIVORE.name.asTitlecase())
			assertThat(reassignedTrainer.area).isEqualTo(ELEPHANT.name.asTitlecase())
			assertThat(assignTrainer.animals).isEqualTo(fromAnimals)
			assertThat(reassignedTrainer.animals).isEqualTo(toAnimals)
		}
	}

	private fun createTrainer() = trainerService.create(trainer.copy(userId = userId)).also { trainerId = it.id!! }

	private fun createTrainer(animals: MutableSet<ObjectId>) =
		trainerService.create(trainer.copy(userId = userId, animals = animals))

	private fun createAnimal(points: Int, status: TrainingStatus) =
		animalService.create(
			andeanBearSpecie.copy(
				healthStatistics = HealthStatistics(
					trainingPoints = points,
					trainingStatus = status.name.asTitlecase(),
					healthPoints = 1,
					healthStatus = SICK.name.asTitlecase()
				)
			)
		)

	private fun train(userId: ObjectId, animalIds: List<ObjectId>) =
		trainerService.trainAnimals(userId.toString(), animalIds.stringify())

	companion object {

		lateinit var trainerId: ObjectId

		fun createAssignmentRequest(employeeId: ObjectId, to: String) = AssignEmployeeRequest(
			position = trainer.position,
			employeeId = employeeId.toString(),
			toArea = to
		)

		fun createReassignmentRequest(employeeId: ObjectId, from: String, to: String) =
			ReassignEmployeeRequest(
				position = trainer.position,
				employeeId = employeeId.toString(),
				fromArea = from,
				toArea = to
			)

		val trainer = Trainer(
			userId = ObjectId.get(),
			firstName = user.firstName,
			middleName = user.middleName,
			lastName = user.lastName,
			area = CARNIVORE.name.asTitlecase(),
			position = TRAINER.name.asTitlecase(),
			animals = mutableSetOf()
		)
	}
}



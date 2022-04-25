package tech.cordona.zooonline.bootstrap.mongock

import io.mongock.api.annotations.BeforeExecution
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackBeforeExecution
import io.mongock.api.annotations.RollbackExecution
import org.springframework.data.mongodb.core.MongoTemplate
import tech.cordona.zooonline.Extensions.buildEmail
import tech.cordona.zooonline.Extensions.buildPassword
import tech.cordona.zooonline.Extensions.getFirstName
import tech.cordona.zooonline.Extensions.getLastName
import tech.cordona.zooonline.Extensions.stringify
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.doctor.service.DoctorService
import tech.cordona.zooonline.domain.guard.service.GuardService
import tech.cordona.zooonline.domain.trainer.service.TrainerService
import tech.cordona.zooonline.security.user.entity.Authority
import tech.cordona.zooonline.security.user.entity.Authority.DOCTOR
import tech.cordona.zooonline.security.user.entity.Authority.GUARD
import tech.cordona.zooonline.security.user.entity.Authority.TRAINER
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.entity.extention.UserExtension.asDoctor
import tech.cordona.zooonline.security.user.entity.extention.UserExtension.asGuard
import tech.cordona.zooonline.security.user.entity.extention.UserExtension.asTrainer
import tech.cordona.zooonline.security.user.service.UserService
import java.util.*

@ChangeUnit(order = "3", id = "staff-initialization", author = "Cordona")
class StaffDbInitializer(
	private val mongoTemplate: MongoTemplate,
	private val userService: UserService,
	private val trainerService: TrainerService,
	private val doctorService: DoctorService,
	private val guardService : GuardService,
	private val areaService: AreaService,
	private val cellService: CellService,
	private val animalService: AnimalService
) {

	@BeforeExecution
	fun beforeExecution() {
		mongoTemplate.dropCollection(TRAINERS_COLLECTION)
		mongoTemplate.dropCollection(DOCTORS_COLLECTION)
		mongoTemplate.dropCollection(GUARDS_COLLECTION)
		mongoTemplate.createCollection(TRAINERS_COLLECTION)
		mongoTemplate.createCollection(DOCTORS_COLLECTION)
		mongoTemplate.createCollection(GUARDS_COLLECTION)
	}

	@RollbackBeforeExecution
	fun rollbackBeforeExecution() {
		mongoTemplate.dropCollection(TRAINERS_COLLECTION)
		mongoTemplate.dropCollection(DOCTORS_COLLECTION)
		mongoTemplate.dropCollection(GUARDS_COLLECTION)
	}

	@RollbackExecution
	fun rollbackExecution() {
		trainerService.deleteAll()
		doctorService.deleteAll()
		guardService.deleteAll()
	}

	@Execution
	fun initialize() {
		areaService.findAll()
			.forEach { area ->
				val animals = getAnimals(area)

				buildUser(TRAINER, trainersNames.peek())
					.let { userService.createUser(it) }
					.also {
						trainerService.create(it.asTrainer(area.name, animals))
						trainersNames.pop()
					}

				buildUser(DOCTOR, doctorsNames.peek())
					.let { userService.createUser(it) }
					.also {
						doctorService.create(it.asDoctor(area.name, animals))
						doctorsNames.pop()
					}

				buildUser(GUARD, guardNames.peek())
					.let { userService.createUser(it) }
					.also {
						guardService.create(it.asGuard(area.name, animals))
						guardNames.pop()
					}
			}
	}

	private fun buildUser(authority: Authority, name: String) = User(
		firstName = name.getFirstName(),
		lastName = name.getLastName(),
		email = name.buildEmail(),
		password = name.buildPassword(),
		authority = authority,
		confirmed = true
	)

	private fun getAnimals(area: Area) =
		cellService.findAllById(area.cells.stringify())
			.map { cell -> animalService.findAllByIds(cell.species.stringify()) }
			.flatten()
			.map { it.id!! }

	companion object {
		const val TRAINERS_COLLECTION = "Trainers"
		const val DOCTORS_COLLECTION = "Doctors"
		const val GUARDS_COLLECTION = "Guards"

		val trainersNames = ArrayDeque(
			listOf(
				"Christopher Alan",
				"Colin Brian",
				"Sarah Connor",
				"Lynn Glenn",
				"Richard Jackson",
				"Barry White",
				"Simon Gustafson",
				"Caroline Smith",
				"Samantha Elvinson",
				"Victoria Goldsmith",
				"William Black",
				"Darren Darrel",
				"Robin Williams",
				"Stewart Russell",
				"Dean Shone",
				"Cameron Dias",
				"Eric Ericsson",
				"Thomas Ian",
				"Kenneth Clark"
			)
		)

		val doctorsNames = ArrayDeque(
			listOf(
				"Callum Beck",
				"John Colin",
				"Alexander Smith",
				"Michael Alan",
				"Greg Shaun",
				"Lorraine Clark",
				"Sandra Bullock",
				"Morag Violin",
				"Lindsay Jason",
				"Bruce Willis",
				"Ewan Mark",
				"Norman Angus",
				"Hugh Williams",
				"Murray Cameron",
				"Deborah Gordon",
				"Jason Alistair",
				"Jamie Ian",
				"Lisa Tracey",
				"Catherine Gail"
			)
		)

		val guardNames = ArrayDeque(
			listOf(
				"Keith Lee",
				"Darren Charles",
				"Stewart Martin",
				"Edward Joseph",
				"Matthew Keith",
				"Aileen Morag",
				"Cheryl Crow",
				"Barbara Allison",
				"Donna Kelly",
				"Jonathan Jasper",
				"Ryan Edwards",
				"Alistair Russell",
				"Andrew Scott",
				"Ross Shaun",
				"Emma Wilson",
				"Elizabeth Lynn",
				"Catherine Heather",
				"Tracey Duncan",
				"Ewan Hugh"
			)
		)
	}
}
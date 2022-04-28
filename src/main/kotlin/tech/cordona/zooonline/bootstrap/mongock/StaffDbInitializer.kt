package tech.cordona.zooonline.bootstrap.mongock

import io.mongock.api.annotations.BeforeExecution
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackBeforeExecution
import io.mongock.api.annotations.RollbackExecution
import org.springframework.data.mongodb.core.MongoTemplate
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.extension.assignEmployee
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.doctor.service.DoctorService
import tech.cordona.zooonline.domain.guard.service.GuardService
import tech.cordona.zooonline.domain.trainer.service.TrainerService
import tech.cordona.zooonline.extension.buildEmail
import tech.cordona.zooonline.extension.buildPassword
import tech.cordona.zooonline.extension.getFirstName
import tech.cordona.zooonline.extension.getLastName
import tech.cordona.zooonline.extension.getMiddleName
import tech.cordona.zooonline.extension.stringify
import tech.cordona.zooonline.security.user.entity.Authority
import tech.cordona.zooonline.security.user.entity.Authority.DOCTOR
import tech.cordona.zooonline.security.user.entity.Authority.GUARD
import tech.cordona.zooonline.security.user.entity.Authority.TRAINER
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.entity.extension.asDoctor
import tech.cordona.zooonline.security.user.entity.extension.asGuard
import tech.cordona.zooonline.security.user.entity.extension.asTrainer
import tech.cordona.zooonline.security.user.service.UserService
import java.util.*

@ChangeUnit(order = "3", id = "staff-initialization", author = "Cordona")
class StaffDbInitializer(
	private val mongoTemplate: MongoTemplate,
	private val userService: UserService,
	private val trainerService: TrainerService,
	private val doctorService: DoctorService,
	private val guardService: GuardService,
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
							.also { trainer ->
								area.assignEmployee(trainer.position, trainer.id!!)
								trainersNames.pop()
							}
					}

				buildUser(DOCTOR, doctorsNames.peek())
					.let { userService.createUser(it) }
					.also {
						doctorService.create(it.asDoctor(area.name, animals))
							.also { doctor ->
								area.assignEmployee(doctor.position, doctor.id!!)
								doctorsNames.pop()
							}
					}

				buildUser(GUARD, guardNames.peek())
					.let { userService.createUser(it) }
					.also {
						guardService.create(it.asGuard(area.name, area.cells))
							.also { guard ->
								area.assignEmployee(guard.position, guard.id!!)
								guardNames.pop()
								areaService.save(area)
							}
					}
			}
	}

	private fun buildUser(authority: Authority, name: String) = User(
		firstName = name.getFirstName(),
		middleName = name.getMiddleName(),
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
				"Christopher Gordon Alan",
				"Colin Graham Brian",
				"Sarah Douglas Connor",
				"Lynn George Glenn",
				"Richard Joseph Jackson",
				"Barry Daniel White",
				"Simon Edward Gustafson",
				"Caroline Fraser Smith",
				"Samantha Duncan Elvinson",
				"Victoria Jamie Goldsmith",
				"William Malcolm Black",
				"Darren Raymond Darrel",
				"Robin Marc Williams",
				"Stewart Adam Russell",
				"Dean Russell Shone",
				"Cameron Roderick Dias",
				"Eric Murray Ericsson",
				"Thomas Dean Ian",
				"Kenneth Adrian Clark"
			)
		)

		val doctorsNames = ArrayDeque(
			listOf(
				"Callum Adrian Beck",
				"John Timothy Colin",
				"Alexander Paul Smith",
				"Michael Justin Alan",
				"Greg Roderick Shaun",
				"Lorraine Gregor Clark",
				"Sandra Gerald Bullock",
				"Morag Callum Violin",
				"Lindsay Paul Jason",
				"Bruce Steven Willis",
				"Ewan Michael Mark",
				"Norman Stuart Angus",
				"Hugh William Williams",
				"Murray Steven Cameron",
				"Deborah Craig Gordon",
				"Jason Charles Alistair",
				"Jamie Greig Ian",
				"Lisa Benjamin Tracey",
				"Catherine James Gail"
			)
		)

		val guardNames = ArrayDeque(
			listOf(
				"Keith Adrian Lee",
				"Darren Campbell Charles",
				"Stewart Frank Martin",
				"Edward Kevin Joseph",
				"Matthew Thomas Keith",
				"Aileen Gordon Morag",
				"Cheryl Kenneth Crow",
				"Barbara Simon Allison",
				"Donna Douglas Kelly",
				"Jonathan Gavin Jasper",
				"Ryan Malcolm Edwards",
				"Alistair Alasdair Russell",
				"Andrew Niall Scott",
				"Ross Christopher Shaun",
				"Emma Alan Wilson",
				"Elizabeth Colin Lynn",
				"Catherine Neil Heather",
				"Tracey Ross Duncan",
				"Ewan Calum Hugh"
			)
		)
	}
}
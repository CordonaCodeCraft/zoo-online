package tech.cordona.zooonline.domain.doctor.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.extension.heal
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.extension.assignEmployee
import tech.cordona.zooonline.domain.area.entity.extension.removeEmployee
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.doctor.entity.Doctor
import tech.cordona.zooonline.domain.doctor.entity.extension.reassigned
import tech.cordona.zooonline.domain.doctor.repository.DoctorRepository
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest
import tech.cordona.zooonline.extension.stringify

@Service
class DoctorServiceImpl(
	private val repository: DoctorRepository,
	private val animalService: AnimalService,
	private val areaService: AreaService,
	private val cellService: CellService
) : DoctorService {

	val logging = KotlinLogging.logger {}

	override fun deleteAll() = repository.deleteAll()

	override fun create(newDoctor: Doctor): Doctor = repository.save(newDoctor)

	override fun findByDoctorId(doctorId: String): Doctor =
		repository.findById(ObjectId(doctorId))
			?: run {
				logging.error { "Doctor with ID: $doctorId not found" }
				throw IllegalArgumentException("Doctor with ID: $doctorId not found")
			}

	override fun findByUserId(userId: String): Doctor =
		repository.findByUserId(ObjectId(userId))
			?: run {
				logging.error { "Doctor with ID: $userId not found" }
				throw IllegalArgumentException("Doctor with ID: $userId not found")
			}

	override fun healAnimals(userId: String, animals: List<String>) =
		animals
			.let { animalsIds -> animalService.findAllByIds(animalsIds) }
			.map { animal -> animal.heal() }
			.also { healed -> animalService.createMany(healed) }

	override fun reassignDoctor(request: ReassignEmployeeRequest) =
		findByDoctorId(request.employeeId)
			.also { doctor ->
				areaService.findAreaByName(request.fromArea)
					.removeEmployee(request.position, doctor.id!!)
					.also { fromArea -> areaService.create(fromArea) }
			}
			.also { doctor ->
				areaService.findAreaByName(request.toArea)
					.assignEmployee(request.position, doctor.id!!)
					.also { toArea -> areaService.create(toArea) }
					.let { toArea ->
						repository.save(doctor.reassigned(toArea, getAnimals(toArea)))
					}
			}

	fun getAnimals(toArea: Area) =
		cellService.findAllById(toArea.cells.stringify())
			.map { cell -> cell.species }
			.flatten()
			.toMutableSet()
}
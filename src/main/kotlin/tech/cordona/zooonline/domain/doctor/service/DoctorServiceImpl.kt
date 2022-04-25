package tech.cordona.zooonline.domain.doctor.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.extention.AnimalExtension.heal
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.doctor.entity.Doctor
import tech.cordona.zooonline.domain.doctor.repository.DoctorsRepository

@Service
class DoctorServiceImpl(
	private val repository: DoctorsRepository,
	private val animalService: AnimalService
) : DoctorService {

	val logging = KotlinLogging.logger {}

	override fun deleteAll() = repository.deleteAll()

	override fun create(newDoctor: Doctor): Doctor = repository.save(newDoctor)

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
			.also { healed -> animalService.saveAll(healed) }
}
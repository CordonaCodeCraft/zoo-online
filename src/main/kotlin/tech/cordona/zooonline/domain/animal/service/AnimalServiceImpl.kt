package tech.cordona.zooonline.domain.animal.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.repository.AnimalRepository
import tech.cordona.zooonline.domain.common.service.EntityValidator
import tech.cordona.zooonline.exception.EntityNotFoundException

@Service
class AnimalServiceImpl(
	private val repository: AnimalRepository,
) : AnimalService, EntityValidator(){

	private val logging = KotlinLogging.logger { }

	override fun create(newAnimal: Animal): Animal =
		validateAnimal(newAnimal).let { repository.save(newAnimal) }

	override fun createMany(newAnimals: List<Animal>) = newAnimals.map { animal -> create(animal) }

	override fun findAll(): List<Animal> = repository.findAll()

	override fun findById(id: ObjectId): Animal =
		repository.findById(id)
			?: run {
				logging.error { "Animal with ID: $id not found" }
				throw EntityNotFoundException("Animal with ID: $id not found")
			}

	override fun findAllByIds(ids: List<String>): List<Animal> = repository.findAllById(ids).toList()

	override fun deleteAll() = repository.deleteAll()

	private fun validateAnimal(animal: Animal) = animal.isValid().withGoodHealthStatistics().withGoodTaxonomyDetails()
}
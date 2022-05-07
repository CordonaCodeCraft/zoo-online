package tech.cordona.zooonline.domain.animal.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.repository.AnimalRepository
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.validation.EntityValidator
import tech.cordona.zooonline.validation.FailReport.entityNotFound

@Service
class AnimalServiceImpl(private val repository: AnimalRepository) : AnimalService, EntityValidator() {

	private val logging = KotlinLogging.logger { }

	override fun create(newAnimal: Animal): Animal =
		validateAnimal(newAnimal).let { repository.save(newAnimal) }

	override fun createMany(newAnimals: List<Animal>) = newAnimals.map { animal -> create(animal) }

	override fun updateMany(updatedAnimals: List<Animal>): List<Animal> = repository.saveAll(updatedAnimals)

	override fun findAll(): List<Animal> = repository.findAll()

	override fun findById(id: ObjectId): Animal =
		repository.findById(id)
			?: run {
				logging.error { entityNotFound(entity = "Animal", idType = "ID", id = id.toString()) }
				throw EntityNotFoundException(entityNotFound(entity = "Animal", idType = "ID", id = id.toString()))
			}

	override fun findAllByIds(ids: List<String>): List<Animal> = repository.findAllById(ids).toList()

	override fun findAllBySpecie(specie: String): List<Animal> = repository.findAllByTaxonomyDetailsName(specie)

	override fun deleteAll() = repository.deleteAll()

	private fun validateAnimal(animal: Animal) =
		animal
			.withValidProperties()
			.withValidHealthStatistics()
			.withValidTaxonomyDetails()
}
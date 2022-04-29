package tech.cordona.zooonline.domain.animal.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.repository.AnimalRepository
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException

@Service
class AnimalServiceImpl(
	private val validator: LocalValidatorFactoryBean,
	private val repository: AnimalRepository,
	private val taxonomyUnitService: TaxonomyUnitService
) : AnimalService {

	private val logging = KotlinLogging.logger { }

	override fun create(animal: Animal): Animal = validate(animal).let { repository.save(animal) }

	override fun createMany(animals: List<Animal>) = animals.map { animal -> create(animal) }

	override fun findAll(): List<Animal> = repository.findAll()

	override fun findById(id: ObjectId): Animal =
		repository.findById(id)
			?: run {
				logging.error { "Animal with ID: $id not found" }
				throw EntityNotFoundException("Animal with ID: $id not found")
			}

	override fun findAllByIds(ids: List<String>): List<Animal> = repository.findAllById(ids).toList()

	override fun deleteAll() = repository.deleteAll()

	private fun validate(animal: Animal) {

		validator.validate(animal)
			.filter { violation -> violation.invalidValue != null }
			.map { violation -> violation.message }
			.run {
				if (this.isNotEmpty()) {
					logging.error { "Animal is not valid: ${this.joinToString(" ; ")}" }
					throw InvalidEntityException("Animal is not valid: ${this.joinToString(" ; ")}")
				}
			}

		validator.validate(animal.healthStatistics)
			.filter { violation -> violation.invalidValue != null }
			.map { violation -> violation.message }
			.run {
				if (this.isNotEmpty()) {
					logging.error { "Animal's health statistics are not valid: ${this.joinToString(" ; ")}" }
					throw InvalidEntityException("Animal's health statistics are not valid: ${this.joinToString(" ; ")}")
				}
			}

		listOf(animal.taxonomyDetails.name, animal.taxonomyDetails.parent)
			.map { name -> taxonomyUnitService.findByName(name) }
			.takeUnless { retrieved -> retrieved.contains(null) }
			?: run {
				logging.error { "Taxonomy details are not valid: missing or misspelled taxonomy unit and/or parent" }
				throw InvalidEntityException("Taxonomy details are not valid: missing or misspelled taxonomy unit and/or parent")
			}
	}
}
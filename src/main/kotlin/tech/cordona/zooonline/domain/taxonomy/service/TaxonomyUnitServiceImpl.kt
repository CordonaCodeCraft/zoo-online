package tech.cordona.zooonline.domain.taxonomy.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer.Companion.ROOT
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.repository.TaxonomyUnitRepository
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException

@Service
class TaxonomyUnitServiceImpl(
	private val validator: LocalValidatorFactoryBean,
	private val repository: TaxonomyUnitRepository
) : TaxonomyUnitService {

	private val logging = KotlinLogging.logger { }

	override fun create(newUnit: TaxonomyUnit) =
		validate(newUnit).let { repository.save(newUnit).also { created -> associate(created) } }

	override fun createMany(units: List<TaxonomyUnit>): List<TaxonomyUnit> = units.map { unit -> create(unit) }

	override fun findByName(name: String): TaxonomyUnit? = repository.findByName(name)

	override fun findAll(): List<TaxonomyUnit> = repository.findAll()

	override fun findAllAnimals(): List<TaxonomyUnit> = repository.findAllAnimals()

	override fun findParentOf(child: String): TaxonomyUnit =
		findByName(child)
			?.let { childUnit -> repository.findByChildrenContaining(childUnit.name) }
			?: run {
				logging.error { "Child taxonomy unit with name: $child is wrong or does not exist" }
				throw EntityNotFoundException("Child taxonomy unit with name: $child is wrong or does not exist")
			}

	override fun deleteAll() = repository.deleteAll()

	private fun associate(child: TaxonomyUnit) {
		child.parent.takeUnless { parentName -> parentName == ROOT }
			?.run {
				findByName(this)
					?.also { parent ->
						parent.children.add(child.name)
						repository.save(parent)
					}
					?: run {
						logging.error { "Parent taxonomy unit with name: ${child.parent} is wrong or does not exist" }
						throw EntityNotFoundException("Parent taxonomy unit with name: ${child.parent} is wrong or does not exist")
					}
			}
	}

	private fun validate(newUnit: TaxonomyUnit) {

		validator.validate(newUnit)
			.filter { violation -> violation.invalidValue != null }
			.map { violation -> violation.message }
			.takeIf { it.isNotEmpty() }
			?.run {
				logging.error { "Taxonomy unit is not valid: ${this.joinToString(" ; ")}" }
				throw InvalidEntityException("Taxonomy unit is not valid: ${this.joinToString(" ; ")}")

			}

		findByName(newUnit.name)
			?.run {
				logging.error { "Taxonomy unit with name: ${this.name} already exists" }
				throw InvalidEntityException("Taxonomy unit with name: ${this.name} already exists")
			}
	}
}
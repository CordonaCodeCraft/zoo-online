package tech.cordona.zooonline.domain.taxonomy.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer.Companion.ROOT
import tech.cordona.zooonline.domain.common.service.EntityValidator
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.repository.TaxonomyUnitRepository
import tech.cordona.zooonline.exception.EntityNotFoundException

@Service
class TaxonomyUnitServiceImpl(private val repository: TaxonomyUnitRepository) : TaxonomyUnitService, EntityValidator() {

	private val logging = KotlinLogging.logger { }

	override fun create(newUnit: TaxonomyUnit) =
		validateTaxonomyUnit(newUnit).let { repository.save(newUnit).also { created -> associate(created) } }

	override fun createMany(newUnits: List<TaxonomyUnit>): List<TaxonomyUnit> = newUnits.map { unit -> create(unit) }

	override fun findByName(name: String): TaxonomyUnit =
		repository.findByName(name)
			?: run {
				logging.error { "Taxonomy unit with name: $name is wrong or does not exist" }
				throw EntityNotFoundException("Taxonomy unit with name: $name is wrong or does not exist")
			}

	override fun findAll(): List<TaxonomyUnit> = repository.findAll()

	override fun findAllAnimals(): List<TaxonomyUnit> = repository.findAllAnimals()

	override fun findParentOf(child: String): TaxonomyUnit =
		findByName(child).let { childUnit -> repository.findByChildrenContaining(childUnit.name) }


	override fun deleteAll() = repository.deleteAll()

	private fun associate(child: TaxonomyUnit) {
		child.parent.takeUnless { parentName -> parentName == ROOT }
			?.run {
				findByName(this)
					.also { parent ->
						parent.children.add(child.name)
						repository.save(parent)
					}
			}
	}

	private fun validateTaxonomyUnit(newUnit: TaxonomyUnit) = newUnit.withValidProperties().withUniqueName()
}
package tech.cordona.zooonline.domain.taxonomy.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.repository.TaxonomyUnitRepository

@Service
class TaxonomyUnitServiceImpl(private val repository: TaxonomyUnitRepository) : TaxonomyUnitService {

	override fun save(unit: TaxonomyUnit): TaxonomyUnit = repository.save(unit)

	override fun saveAll(units: List<TaxonomyUnit>): List<TaxonomyUnit> = repository.saveAll(units)

	override fun findAll(): List<TaxonomyUnit> = repository.findAll()

	override fun findAllAnimals(): List<TaxonomyUnit> = repository.findAllAnimals()

	override fun findParentOf(value: String): TaxonomyUnit {
		val id = repository.findByName(value).id
		return repository.findByChildrenContaining(id)
	}
}
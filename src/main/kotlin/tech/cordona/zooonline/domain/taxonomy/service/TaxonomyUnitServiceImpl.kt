package tech.cordona.zooonline.domain.taxonomy.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.repository.TaxonomyUnitsRepository

@Service
class TaxonomyUnitServiceImpl(private val repository: TaxonomyUnitsRepository) : TaxonomyUnitService {

	override fun save(unit: TaxonomyUnit): TaxonomyUnit = repository.save(unit)

	override fun saveAll(units: List<TaxonomyUnit>): List<TaxonomyUnit> = repository.saveAll(units)

	override fun findAll(): List<TaxonomyUnit> = repository.findAll()

	override fun findAllAnimals(): List<TaxonomyUnit> = repository.findAllAnimals()

	override fun findParentOf(value: String): TaxonomyUnit =
		repository.findByName(value).let { unit -> repository.findByChildrenContaining(unit.name) }

	override fun deleteAll() = repository.deleteAll()
}
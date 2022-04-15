package tech.cordona.zooonline.domain.taxonomy.service

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

interface TaxonomyUnitService {
	fun save(unit: TaxonomyUnit): TaxonomyUnit
	fun saveAll(units: List<TaxonomyUnit>): List<TaxonomyUnit>
	fun findAll(): List<TaxonomyUnit>
	fun findAllAnimals(): List<TaxonomyUnit>
	fun findParentOf(value: String): TaxonomyUnit
	fun deleteAll()
}
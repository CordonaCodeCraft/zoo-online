package tech.cordona.zooonline.domain.taxonomy.service

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

interface TaxonomyUnitService {
	fun create(newUnit: TaxonomyUnit): TaxonomyUnit
	fun createMany(newUnits: List<TaxonomyUnit>): List<TaxonomyUnit>
	fun findByName(name: String): TaxonomyUnit?
	fun findAll(): List<TaxonomyUnit>
	fun findAllAnimals(): List<TaxonomyUnit>
	fun findParentOf(child: String): TaxonomyUnit
	fun findChildrenOf(parent: String): List<TaxonomyUnit>
	fun deleteAll()
}
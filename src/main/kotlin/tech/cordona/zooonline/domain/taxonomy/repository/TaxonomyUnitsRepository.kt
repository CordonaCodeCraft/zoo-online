package tech.cordona.zooonline.domain.taxonomy.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

interface TaxonomyUnitsRepository : MongoRepository<TaxonomyUnit, String> {
	@Query("{'children' : { \$size: 0 }}")
	fun findAllAnimals(): List<TaxonomyUnit>
	fun findByName(value: String): TaxonomyUnit
	fun findByChildrenContaining(name: String): TaxonomyUnit
}
package tech.cordona.zooonline.domain.taxonomy.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

interface TaxonomyUnitRepository : MongoRepository<TaxonomyUnit, String> {
	@Query("{'children' : { \$size: 0 }}")
	fun findAllAnimals(): List<TaxonomyUnit>
	fun findByName(value: String): TaxonomyUnit
	fun findByChildrenContaining(id: ObjectId): TaxonomyUnit
}
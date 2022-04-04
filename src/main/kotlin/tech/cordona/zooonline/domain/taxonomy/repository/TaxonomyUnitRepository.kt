package tech.cordona.zooonline.domain.taxonomy.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

@Repository
interface TaxonomyUnitRepository : MongoRepository<TaxonomyUnit, String> {

	@Query("{'children' : { \$size: 0 }}")
	fun getAllAnimals(): List<TaxonomyUnit>
}
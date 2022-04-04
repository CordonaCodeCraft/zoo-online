package tech.cordona.zooonline.domain.taxonomy.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

@Repository
interface TaxonomyUnitRepository : MongoRepository<TaxonomyUnit, String> {
}
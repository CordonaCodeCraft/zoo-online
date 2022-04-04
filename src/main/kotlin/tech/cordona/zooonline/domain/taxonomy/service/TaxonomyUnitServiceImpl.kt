package tech.cordona.zooonline.domain.taxonomy.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.repository.TaxonomyUnitRepository

@Service
class TaxonomyUnitServiceImpl @Autowired constructor(val repository: TaxonomyUnitRepository): TaxonomyUnitService {

	override fun save(unit : TaxonomyUnit) : TaxonomyUnit = repository.save(unit)

	override fun saveAll(units: List<TaxonomyUnit>): List<TaxonomyUnit> = repository.saveAll(units)

	override fun findAll(): List<TaxonomyUnit> = repository.findAll()

}
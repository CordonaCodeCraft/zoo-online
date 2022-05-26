package tech.cordona.zooonline.domain.area.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.repository.AreaRepository
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.validation.EntityValidator
import tech.cordona.zooonline.validation.FailReport.entityNotFound

@Service
class AreaServiceImpl(private val repository: AreaRepository) : AreaService, EntityValidator() {

	private val logging = KotlinLogging.logger { }

	override fun create(newArea: Area): Area = validateNew(newArea).let { repository.save(newArea) }

	override fun update(updatedArea: Area): Area = validateUpdated(updatedArea).let { repository.save(updatedArea) }

	override fun createMany(newAreas: List<Area>): List<Area> = newAreas.map { newArea -> create(newArea) }

	override fun findAll(): List<Area> = repository.findAll()

	override fun findAreaByName(name: String) =
		repository.findByName(name)
			?: run {
				logging.error { entityNotFound(entity = "Area", idType = "name", id = name) }
				throw EntityNotFoundException(entityNotFound(entity = "Area", idType = "name", id = name))
			}

	override fun findAllByNames(names: List<String>): List<Area> = repository.findAllByNameIn(names)

	override fun deleteAll() = repository.deleteAll()

	private fun validateNew(newArea: Area) =
		newArea
			.withValidProperties()
			.withUniqueName()
			.withExistingTaxonomyUnit()

	private fun validateUpdated(updatedArea: Area) =
		updatedArea
			.withValidProperties()
			.withExistingTaxonomyUnit()
}
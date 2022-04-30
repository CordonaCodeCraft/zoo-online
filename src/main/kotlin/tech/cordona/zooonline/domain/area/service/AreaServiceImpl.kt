package tech.cordona.zooonline.domain.area.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.repository.AreaRepository
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.extension.stringify

@Service
class AreaServiceImpl(
	private val validator: LocalValidatorFactoryBean,
	private val repository: AreaRepository,
	private val cellService: CellService
) : AreaService {

	private val logging = KotlinLogging.logger { }

	override fun create(area: Area): Area = repository.save(area)

	override fun createMany(areas: List<Area>): List<Area> = repository.saveAll(areas)

	override fun findAll(): List<Area> = repository.findAll()

	override fun findAreaByName(name: String) = repository.findByName(name)
		?: throw IllegalArgumentException("Animal type $name does not exist")

	override fun findAllByNames(names: List<String>): List<Area> = repository.findAllByNameIn(names)

	override fun deleteAll() = repository.deleteAll()

	private fun validate(newArea: Area) {

		validator.validate(newArea)
			.filter { violation -> violation.invalidValue != null }
			.map { violation -> violation.message }
			.takeIf { it.isNotEmpty() }
			?.run {
				logging.error { "Cell is not valid: ${this.joinToString(" ; ")}" }
				throw InvalidEntityException("Cell is not valid: ${this.joinToString(" ; ")}")
			}

		newArea.cells
			.let { cellsIds -> cellService.findAllById(cellsIds.stringify()) }
			.takeUnless { retrieved -> retrieved.size < newArea.cells.size }
			?: run {
				logging.error { "Area is not valid: missing cells" }
				throw InvalidEntityException("Area is not valid: missing cells")
			}
	}
}
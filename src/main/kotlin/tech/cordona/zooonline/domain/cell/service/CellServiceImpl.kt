package tech.cordona.zooonline.domain.cell.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.repository.CellRepository
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.validation.EntityValidator
import tech.cordona.zooonline.validation.FailReport.entityNotFound

@Service
class CellServiceImpl(private val repository: CellRepository) : CellService, EntityValidator() {

	private val logging = KotlinLogging.logger { }

	override fun create(newCell: Cell) = validateCell(newCell).let { repository.save(newCell) }

	override fun createMany(newCells: List<Cell>): List<Cell> = newCells.map { cell -> create(cell) }

	override fun saveAll(newCells: List<Cell>): List<Cell> = repository.saveAll(newCells)

	override fun findAll(): List<Cell> = repository.findAll()

	override fun findAllById(ids: List<String>): List<Cell> = repository.findAllById(ids).toList()

	override fun findAllByAnimalType(type: String): List<Cell> = repository.findAllByAnimalType(type)

	override fun findCellBySpecie(specie: String): Cell =
		repository.findBySpecie(specie)
			?: run {
				logging.error { entityNotFound(entity = "Cell", idType = "specie", id = specie) }
				throw EntityNotFoundException(entityNotFound(entity = "Cell", idType = "specie", id = specie))
			}

	override fun deleteAll() = repository.deleteAll()

	private fun validateCell(newCell: Cell) =
		newCell
			.withValidProperties()
			.withExistingSpecie()
			.withUniqueSpecie()
			.withValidTaxonomyDetails()
			.withExistingAnimals()
}
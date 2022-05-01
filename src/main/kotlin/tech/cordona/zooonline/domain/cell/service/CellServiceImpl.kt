package tech.cordona.zooonline.domain.cell.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.repository.CellRepository
import tech.cordona.zooonline.domain.common.service.EntityValidator
import tech.cordona.zooonline.exception.EntityNotFoundException

@Service
class CellServiceImpl(private val repository: CellRepository) : CellService, EntityValidator() {

	private val logging = KotlinLogging.logger { }

	override fun create(newCell: Cell) = validateCell(newCell).let { repository.save(newCell) }

	override fun createMany(newCells: List<Cell>): List<Cell> = newCells.map { cell -> create(cell) }

	override fun saveAll(newCells: List<Cell>): List<Cell> = repository.saveAll(newCells)

	override fun findAll(): List<Cell> = repository.findAll()

	override fun findAllById(ids: List<String>): List<Cell> = repository.findAllById(ids).toList()

	override fun findCellBySpecie(specie: String): Cell =
		repository.findBySpecie(specie)
			?: run {
				logging.error { "Cell with specie $specie not found" }
				throw EntityNotFoundException("Cell with specie $specie not found")
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
package tech.cordona.zooonline.domain.cell.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.repository.CellsRepository

@Service
class CellServiceImpl(private val repository: CellsRepository) : CellService {

	override fun saveAll(cells: List<Cell>): List<Cell> = repository.saveAll(cells)

	override fun findAllById(ids: List<String>): List<Cell> = repository.findAllById(ids).toList()

	override fun findCellBySpecie(specie: String): Cell = repository.findBySpecie(specie)
		?: throw IllegalArgumentException("Cell with specie $specie not found")

	override fun deleteAll() = repository.deleteAll()
}
package tech.cordona.zooonline.domain.cell.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.repository.CellRepository

@Service
class CellServiceImpl(private val repository: CellRepository) : CellService {

	override fun saveAllCells(cells: List<Cell>): List<Cell> = repository.saveAll(cells)

	override fun findCellBySpecie(specie: String): Cell = repository.findCellBySpecie(specie)

	override fun findCellsByIds(ids: MutableSet<ObjectId>): List<Cell> =
		repository.findAllById(ids.map { id -> id.toString() }).toList()

	override fun findAllCells(): List<Cell> = repository.findAll()

	override fun deleteAllCells() = repository.deleteAll()
}
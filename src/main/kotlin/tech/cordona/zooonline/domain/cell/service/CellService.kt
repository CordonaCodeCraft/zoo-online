package tech.cordona.zooonline.domain.cell.service

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.cell.entity.Cell

interface CellService {
	fun saveAllCells(cells: List<Cell>): List<Cell>
	fun findCellBySpecie(specie: String): Cell
	fun findCellsByIds(ids: MutableSet<ObjectId>): List<Cell>
	fun findAllCells(): List<Cell>
	fun deleteAllCells()
}
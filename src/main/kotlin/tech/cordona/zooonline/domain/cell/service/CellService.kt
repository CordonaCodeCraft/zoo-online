package tech.cordona.zooonline.domain.cell.service

import tech.cordona.zooonline.domain.cell.entity.Cell

interface CellService {
	fun saveAll(cells: List<Cell>): List<Cell>
	fun findAllById(ids: List<String>): List<Cell>
	fun findCellBySpecie(specie: String): Cell
	fun deleteAll()
}
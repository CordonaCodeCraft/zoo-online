package tech.cordona.zooonline.domain.cell.service

import tech.cordona.zooonline.domain.cell.entity.Cell

interface CellService {
	fun saveAll(cells: List<Cell>): List<Cell>
}
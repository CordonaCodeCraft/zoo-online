package tech.cordona.zooonline.domain.cell.service

import tech.cordona.zooonline.domain.cell.entity.Cell

interface CellService {
	fun create(newCell: Cell): Cell
	fun createMany(newCells: List<Cell>): List<Cell>
	fun saveAll(newCells: List<Cell>): List<Cell>
	fun findAll(): List<Cell>
	fun findAllById(ids: List<String>): List<Cell>
	fun findAllByAnimalType(type: String): List<Cell>
	fun findCellBySpecie(specie: String): Cell?
	fun deleteAll()
}
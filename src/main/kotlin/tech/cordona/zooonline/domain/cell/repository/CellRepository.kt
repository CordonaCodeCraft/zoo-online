package tech.cordona.zooonline.domain.cell.repository

import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.cell.entity.Cell

interface CellRepository : MongoRepository<Cell, String> {
	fun findBySpecie(specie: String): Cell?
}
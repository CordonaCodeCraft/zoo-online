package tech.cordona.zooonline.domain.cell.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import tech.cordona.zooonline.domain.cell.entity.Cell

@Repository
interface CellRepository : MongoRepository<Cell, String>
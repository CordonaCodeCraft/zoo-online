package tech.cordona.zooonline.domain.cell.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.repository.CellRepository

@Service
class CellServiceImpl @Autowired constructor(val repository: CellRepository) : CellService {
	override fun saveAll(cells : List<Cell>): List<Cell> = repository.saveAll(cells)
}
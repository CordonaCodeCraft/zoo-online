package tech.cordona.zooonline.domain.area.model

import tech.cordona.zooonline.domain.cell.model.CellToGuard

data class AreaToGuard(val name: String, val cells: List<CellToGuard>)
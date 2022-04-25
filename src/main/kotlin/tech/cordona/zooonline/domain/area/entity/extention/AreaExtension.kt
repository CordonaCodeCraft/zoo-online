package tech.cordona.zooonline.domain.area.entity.extention

import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.model.AreaToGuard
import tech.cordona.zooonline.domain.area.model.AreaToVisitor
import tech.cordona.zooonline.domain.cell.model.CellToGuard

object AreaExtension {

	fun Area.toVisitor(species: List<String>) = AreaToVisitor(
		areaName = this.name,
		species = species
	)

	fun Area.toGuard(cells: List<CellToGuard>) = AreaToGuard(
		name = this.name,
		cells = cells
	)
}
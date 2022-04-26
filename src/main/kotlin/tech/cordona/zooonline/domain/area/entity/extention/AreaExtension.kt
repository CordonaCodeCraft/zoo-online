package tech.cordona.zooonline.domain.area.entity.extention

import org.bson.types.ObjectId
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

	fun Area.assignTrainer(trainerId: ObjectId) {
		this.staff.trainers.add(trainerId)
	}

	fun Area.assignDoctor(doctorId: ObjectId) {
		this.staff.doctors.add(doctorId)
	}

	fun Area.assignGuard(guardId: ObjectId) {
		this.staff.guards.add(guardId)
	}
}
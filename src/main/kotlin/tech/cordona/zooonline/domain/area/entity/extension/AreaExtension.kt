package tech.cordona.zooonline.domain.area.entity.extension

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.model.AreaToGuard
import tech.cordona.zooonline.domain.area.model.AreaToVisitor
import tech.cordona.zooonline.domain.cell.model.CellToGuard
import tech.cordona.zooonline.extension.StringExtension.asTitlecase
import tech.cordona.zooonline.security.user.entity.Authority.DOCTOR
import tech.cordona.zooonline.security.user.entity.Authority.TRAINER

object AreaExtension {

	fun Area.toVisitor(species: List<String>) = AreaToVisitor(
		areaName = this.name,
		species = species
	)

	fun Area.toGuard(cells: List<CellToGuard>) = AreaToGuard(
		name = this.name,
		cells = cells
	)

	fun Area.assignEmployee(position: String, employeeId: ObjectId): Area {
		when (position) {
			TRAINER.name.asTitlecase() -> this.staff.trainers.add(employeeId)
			DOCTOR.name.asTitlecase() -> this.staff.doctors.add(employeeId)
			else -> this.staff.guards.add(employeeId)
		}
		return this
	}

	fun Area.removeEmployee(position: String, employeeId: ObjectId): Area {
		when (position) {
			TRAINER.name.asTitlecase() -> this.staff.trainers.remove(employeeId)
			DOCTOR.name.asTitlecase() -> this.staff.doctors.remove(employeeId)
			else -> this.staff.guards.remove(employeeId)
		}
		return this
	}

}
package tech.cordona.zooonline.domain.guard.entity.extension

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.guard.entity.Guard

object GuardExtension {
	fun Guard.reassigned(area: Area, cells: MutableSet<ObjectId>) = this.copy(area = area.name, cells = cells)
}
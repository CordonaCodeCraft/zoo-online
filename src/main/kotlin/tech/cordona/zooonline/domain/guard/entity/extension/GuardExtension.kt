package tech.cordona.zooonline.domain.guard.entity.extension

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.guard.entity.Guard

fun Guard.reassigned(area: Area, cells: Set<ObjectId>) = this.copy(area = area.name, cells = cells)
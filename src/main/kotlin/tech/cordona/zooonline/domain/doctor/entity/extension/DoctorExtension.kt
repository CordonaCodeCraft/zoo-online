package tech.cordona.zooonline.domain.doctor.entity.extension

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.doctor.entity.Doctor

fun Doctor.reassigned(area: Area, animals: MutableSet<ObjectId>) = this.copy(area = area.name, animals = animals)
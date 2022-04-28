package tech.cordona.zooonline.domain.area.entity

import org.bson.types.ObjectId

data class AreaStaff(
	val trainers: MutableSet<ObjectId>,
	val doctors: MutableSet<ObjectId>,
	val guards: MutableSet<ObjectId>,
)
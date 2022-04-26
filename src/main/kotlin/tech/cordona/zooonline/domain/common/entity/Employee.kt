package tech.cordona.zooonline.domain.common.entity

import org.bson.types.ObjectId

interface Employee {
	val id: ObjectId?
	val userId: ObjectId
	val firstName: String
	val lastName: String
	val area: String
	val position: String
}
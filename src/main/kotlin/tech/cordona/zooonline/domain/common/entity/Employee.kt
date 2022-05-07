package tech.cordona.zooonline.domain.common.entity

import org.bson.types.ObjectId
import tech.cordona.zooonline.validation.annotation.validname.ValidName

interface Employee {
	val id: ObjectId?
	val userId: ObjectId
	@get:ValidName
	val firstName: String
	@get:ValidName
	val middleName: String
	@get:ValidName
	val lastName: String
	val area: String
	val position: String
}
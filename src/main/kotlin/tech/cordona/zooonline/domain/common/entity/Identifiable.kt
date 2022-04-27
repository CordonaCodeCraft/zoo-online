package tech.cordona.zooonline.domain.common.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

interface Identifiable {
	@get:Id
	val id: ObjectId?
}




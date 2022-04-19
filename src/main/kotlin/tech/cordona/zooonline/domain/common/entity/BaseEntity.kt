package tech.cordona.zooonline.domain.common.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

interface BaseEntity {
	@get:Id
	val id: ObjectId
	val createdDate: LocalDateTime
	var modifiedDate: LocalDateTime
}




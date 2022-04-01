package tech.cordona.zooonline.domain.tiger.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

abstract class BaseEntity (
	@Id
	var id: ObjectId = ObjectId.get(),
	var createdDate: LocalDateTime = LocalDateTime.now(),
	var modifiedDate: LocalDateTime = LocalDateTime.now()
)

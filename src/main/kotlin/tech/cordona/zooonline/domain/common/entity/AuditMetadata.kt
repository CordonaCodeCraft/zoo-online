package tech.cordona.zooonline.domain.common.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

open class AuditMetadata {
	@CreatedDate
	lateinit var createdOn: LocalDateTime

	@LastModifiedDate
	lateinit var lastModifiedOn: LocalDateTime

	@CreatedBy
	lateinit var createdBy: String

	@LastModifiedBy
	lateinit var lastModifiedBy: String
}
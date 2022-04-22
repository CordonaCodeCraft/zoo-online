package tech.cordona.zooonline.domain.common.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

open class AuditMetadata {
	@CreatedDate
	private lateinit var createdOn: LocalDateTime
	@LastModifiedDate
	private lateinit var lastModifiedOn: LocalDateTime
	@CreatedBy
	private lateinit var createdBy: String
	@LastModifiedBy
	private lateinit var lastModifiedBy: String
}
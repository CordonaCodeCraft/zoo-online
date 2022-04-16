package tech.cordona.zooonline.domain.taxonomy.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.DatabaseInitializer.Companion.TAXONOMY_UNITS_COLLECTION
import tech.cordona.zooonline.domain.BaseEntity
import java.time.LocalDateTime

@Document(collection = TAXONOMY_UNITS_COLLECTION)
@TypeAlias("TaxonomyUnit")
data class TaxonomyUnit(
	val name: String,
	val parent: String,
	var children: MutableSet<String>,
	override val id: ObjectId = ObjectId.get(),
	override val createdDate: LocalDateTime = LocalDateTime.now(),
	override var modifiedDate: LocalDateTime = LocalDateTime.now()
) : BaseEntity
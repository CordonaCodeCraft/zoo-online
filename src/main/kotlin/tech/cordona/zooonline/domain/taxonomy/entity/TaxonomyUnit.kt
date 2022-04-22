package tech.cordona.zooonline.domain.taxonomy.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.DatabaseInitializer.Companion.TAXONOMY_UNITS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable

@Document(collection = TAXONOMY_UNITS_COLLECTION)
@TypeAlias("TaxonomyUnit")
data class TaxonomyUnit(
	@Indexed(unique = true)
	val name: String,
	val parent: String,
	var children: MutableSet<String>,
	override val id: ObjectId? = null,
) : Identifiable, AuditMetadata()
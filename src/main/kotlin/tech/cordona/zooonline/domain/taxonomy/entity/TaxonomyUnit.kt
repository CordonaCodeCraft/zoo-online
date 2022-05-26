package tech.cordona.zooonline.domain.taxonomy.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer.Companion.TAXONOMY_UNITS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.validation.annotation.validname.ValidName

@Document(collection = TAXONOMY_UNITS_COLLECTION)
@TypeAlias("TaxonomyUnit")
data class TaxonomyUnit(
	@get:ValidName
	val name: String,
	@get:ValidName
	val parent: String,
	var children: MutableSet<String>,
	override val id: ObjectId? = null,
) : Identifiable, AuditMetadata()
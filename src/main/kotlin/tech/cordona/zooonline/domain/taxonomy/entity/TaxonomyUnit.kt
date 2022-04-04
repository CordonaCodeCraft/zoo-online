package tech.cordona.zooonline.domain.taxonomy.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.BaseEntity

@Document(collection = "TaxonomyUnits")
@TypeAlias("TaxonomyUnit")
data class TaxonomyUnit(
	val type: String,
	val parent: String,
	var children: MutableSet<ObjectId>
) : BaseEntity()
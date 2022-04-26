package tech.cordona.zooonline.domain.area.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer.Companion.AREAS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable

@Document(collection = AREAS_COLLECTION)
@TypeAlias("Area")
data class Area(
	@Indexed(unique = true)
	val name: String,
	val cells: MutableSet<ObjectId>,
	val staff: AreaStaff,
	override val id: ObjectId? = null,
) : Identifiable, AuditMetadata()
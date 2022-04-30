package tech.cordona.zooonline.domain.cell.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer.Companion.CELLS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.validation.annotation.ValidName

@Document(collection = CELLS_COLLECTION)
@TypeAlias("Cell")
data class Cell(
	@get:ValidName
	val animalGroup: String,
	@get:ValidName
	val animalType: String,
	val specie: String,
	val species: MutableSet<ObjectId>,
	override val id: ObjectId? = null,
) : Identifiable, AuditMetadata()
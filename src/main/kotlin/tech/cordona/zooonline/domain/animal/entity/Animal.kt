package tech.cordona.zooonline.domain.animal.entity

import org.bson.types.ObjectId
import org.hibernate.validator.constraints.URL
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer.Companion.ANIMALS_COLLECTION
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.common.entity.AuditMetadata
import tech.cordona.zooonline.domain.common.entity.Identifiable
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.validation.annotation.ValidAge
import tech.cordona.zooonline.validation.annotation.ValidName
import tech.cordona.zooonline.validation.annotation.ValidWeight

@Document(collection = ANIMALS_COLLECTION)
@TypeAlias("Animal")
data class Animal(
	@get:ValidName
	val name: String,
	@get:ValidAge
	val age: Int,
	@get:ValidWeight
	val weight: Double,
	val gender: String,
	val taxonomyDetails: TaxonomyUnit,
	val healthStatistics: HealthStatistics,
	@get:URL(protocol = "https", message = "URL is not valid")
	val url: String,
	override val id: ObjectId? = null,
) : Identifiable, AuditMetadata()
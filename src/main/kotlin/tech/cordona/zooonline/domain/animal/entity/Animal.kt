package tech.cordona.zooonline.domain.animal.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.DatabaseInitializer.Companion.ANIMALS_COLLECTION
import tech.cordona.zooonline.domain.BaseEntity
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import java.time.LocalDateTime

@Document(collection = ANIMALS_COLLECTION)
@TypeAlias("Animal")
data class Animal(
	val name: String,
	val age: Int,
	val weight: Double,
	val gender: String,
	val taxonomyDetails: TaxonomyUnit,
	val healthStatistics: HealthStatistics,
	val url: String,
	override val id: ObjectId = ObjectId.get(),
	override val createdDate: LocalDateTime = LocalDateTime.now(),
	override var modifiedDate: LocalDateTime = LocalDateTime.now()

) : BaseEntity
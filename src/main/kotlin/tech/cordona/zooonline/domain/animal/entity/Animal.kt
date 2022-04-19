package tech.cordona.zooonline.domain.animal.entity

import org.bson.types.ObjectId
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.bootstrap.mongock.DatabaseInitializer.Companion.ANIMALS_COLLECTION
import tech.cordona.zooonline.domain.common.entity.BaseEntity
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import java.time.LocalDateTime
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@Document(collection = ANIMALS_COLLECTION)
@TypeAlias("Animal")
data class Animal(
	@get:Length(min = 1, max = 20, message = "The animal's name must be between 1 and 20 characters long")
	val name: String,
	@get:Positive
	@get:Size(min = 1, max = 60, message = "The animal's age must be an integer between 1 and 60")
	val age: Int,
	@get:Positive
	@get:DecimalMin("0.1", message = "The animal's weight must be bigger or equal to 0.1")
	@get:DecimalMin("60.0", message = "The animal's weight must be equal or less than 60.0")
	val weight: Double,
	val gender: String,
	val taxonomyDetails: TaxonomyUnit,
	val healthStatistics: HealthStatistics,
	@get:URL(protocol = "https")
	val url: String,
	override val id: ObjectId = ObjectId.get(),
	override val createdDate: LocalDateTime = LocalDateTime.now(),
	override var modifiedDate: LocalDateTime = LocalDateTime.now()
) : BaseEntity
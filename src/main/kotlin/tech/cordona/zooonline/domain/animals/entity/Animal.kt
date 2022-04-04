package tech.cordona.zooonline.domain.animals.entity

import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import tech.cordona.zooonline.domain.BaseEntity
import tech.cordona.zooonline.domain.animals.entity.structs.AnimalDescription
import tech.cordona.zooonline.domain.animals.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.animals.entity.structs.TaxonomyDetails

@Document(collection = "Animals")
@TypeAlias("Animal")
data class Animal(
	val name: String,
	val age: Int,
	val weight: Double,
	val gender: Gender,
	val taxonomy: TaxonomyDetails,
	val stats: HealthStatistics,
	val description: AnimalDescription
) : BaseEntity() {
	enum class Gender(var value: String) {
		MALE("Male"),
		FEMALE("Female")
	}
}


package tech.cordona.zooonline.domain.animal.entity.structs

import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class HealthStatistics(
	@get:Positive
	@get:Size(min = 1, max = 10, message = "The animal's health points must be an integer between 1 and 10")
	val healthPoints: Int,
	val healthStatus: String,
	@get:Positive
	@get:Size(min = 1, max = 10, message = "The animal's training points must be an integer between 1 and 10")
	val trainingPoints: Int,
	val trainingStatus: String
)







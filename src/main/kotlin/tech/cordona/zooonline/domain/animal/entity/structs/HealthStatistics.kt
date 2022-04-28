package tech.cordona.zooonline.domain.animal.entity.structs

import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class HealthStatistics(
	@get:Positive
	@get:Size(
		min = MINIMUM_TRAINING_POINTS,
		max = MAXIMUM_TRAINING_POINTS,
		message = "The animal's health points must be an integer between 1 and 10"
	)
	val healthPoints: Int,
	val healthStatus: String,
	@get:Positive
	@get:Size(
		min = MINIMUM_HEALTH_POINTS,
		max = MAXIMUM_HEALTH_POINTS,
		message = "The animal's training points must be an integer between 1 and 10"
	)
	val trainingPoints: Int,
	val trainingStatus: String
) {
	companion object {
		const val MINIMUM_TRAINING_POINTS = 1
		const val MAXIMUM_TRAINING_POINTS = 10
		const val UNTRAINED_THRESHOLD = 5
		const val MINIMUM_HEALTH_POINTS = 1
		const val MAXIMUM_HEALTH_POINTS = 10
		const val SICK_THRESHOLD = 5
	}
}







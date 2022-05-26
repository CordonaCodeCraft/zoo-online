package tech.cordona.zooonline.domain.animal.entity.structs

import tech.cordona.zooonline.validation.annotation.validhealthpoints.ValidHealthPoints
import tech.cordona.zooonline.validation.annotation.validtrainingpoints.ValidTrainingPoints

data class HealthStatistics(
	@get:ValidHealthPoints
	val healthPoints: Int,
	val healthStatus: String,
	@get:ValidTrainingPoints
	val trainingPoints: Int,
	val trainingStatus: String
) {
	companion object {
		const val UNTRAINED_THRESHOLD = 5
		const val SICK_THRESHOLD = 5
	}
}







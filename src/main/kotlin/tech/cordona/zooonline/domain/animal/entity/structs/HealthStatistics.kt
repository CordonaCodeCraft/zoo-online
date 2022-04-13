package tech.cordona.zooonline.domain.animal.entity.structs

data class HealthStatistics(
	var healthPoints: Int,
	var healthStatus: String,
	var trainingPoints: Int,
	var trainingStatus: String
) {
	enum class TrainingStatus(val asString: String) {
		TRAINED("Trained"),
		UNTRAINED("Untrained")
	}

	enum class HealthStatus(val asString: String) {
		HEALTHY("Healthy"),
		SICK("Sick")
	}
}







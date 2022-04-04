package tech.cordona.zooonline.domain.animals.entity.structs

data class HealthStatistics(
	var healthPoints: Int,
	var health: String,
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







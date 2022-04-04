package tech.cordona.zooonline.domain.animals.entity.structs

data class HealthStatistics(
	var healthPoints: Int,
	var health: HealthStatus,
	var trainingPoints: Int,
	var trainingStatus: TrainingStatus
) {
	enum class TrainingStatus(val value: String) {
		TRAINED("Trained"),
		UNTRAINED("Untrained")
	}

	enum class HealthStatus(val value: String) {
		HEALTHY("Healthy"),
		SICK("Sick")
	}
}







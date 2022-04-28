package tech.cordona.zooonline.domain.animal.model

data class AnimalToTrainer(
	val animalId: String,
	val specie: String,
	val name: String,
	val trainingPoints: Int,
	val trainingStatus: String
)
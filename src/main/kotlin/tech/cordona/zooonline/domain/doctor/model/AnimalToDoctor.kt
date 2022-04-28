package tech.cordona.zooonline.domain.doctor.model

class AnimalToDoctor(
	val animalId: String,
	val specie: String,
	val name: String,
	val healthPoints: Int,
	val healthStatus: String
)
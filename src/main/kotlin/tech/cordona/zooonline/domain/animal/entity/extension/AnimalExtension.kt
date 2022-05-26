package tech.cordona.zooonline.domain.animal.entity.extension

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.Companion.SICK_THRESHOLD
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.Companion.UNTRAINED_THRESHOLD
import tech.cordona.zooonline.domain.animal.model.AnimalToTrainer
import tech.cordona.zooonline.domain.animal.model.AnimalToVisitor
import tech.cordona.zooonline.domain.doctor.model.AnimalToDoctor
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_TRAINING_POINTS

fun Animal.toVisitor() = AnimalToVisitor(
	name = this.name,
	gender = this.gender,
	age = this.age,
	weight = this.weight
)

fun Animal.toTrainer() = AnimalToTrainer(
	animalId = this.id!!.toString(),
	specie = this.taxonomyDetails.name,
	name = this.name,
	trainingPoints = this.healthStatistics.trainingPoints,
	trainingStatus = this.healthStatistics.trainingStatus
)

fun Animal.toDoctor() = AnimalToDoctor(
	animalId = this.id!!.toString(),
	specie = this.taxonomyDetails.name,
	name = this.name,
	healthPoints = this.healthStatistics.healthPoints,
	healthStatus = this.healthStatistics.healthStatus
)

fun Animal.train() =
	this.healthStatistics.trainingPoints
		.takeIf { it + 1 <= MAX_TRAINING_POINTS }
		?.let { points ->
			return this.copy(
				healthStatistics = healthStatistics.copy(
					trainingPoints = points + 1,
					trainingStatus = if (points + 1 > UNTRAINED_THRESHOLD) "Trained" else "Untrained"
				)
			)
		}
		?: this

fun Animal.heal() =
	this.healthStatistics.healthPoints
		.takeIf { it + 1 <= MAX_HEALTH_POINTS }
		?.let { points ->
			return this.copy(
				healthStatistics = healthStatistics.copy(
					healthPoints = points + 1,
					healthStatus = if (points + 1 > SICK_THRESHOLD) "Healthy" else "Sick"
				)
			)
		}
		?: this
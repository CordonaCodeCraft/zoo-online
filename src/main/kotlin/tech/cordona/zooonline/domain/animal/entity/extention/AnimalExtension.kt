package tech.cordona.zooonline.domain.animal.entity.extention

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.Companion.MAXIMUM_TRAINING_POINTS
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics.Companion.UNTRAINED_THRESHOLD
import tech.cordona.zooonline.domain.animal.model.AnimalToTrainer
import tech.cordona.zooonline.domain.animal.model.AnimalToVisitor

object AnimalExtension {

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

	fun Animal.train() =
		this.healthStatistics.trainingPoints
			.takeIf { it + 1 <= MAXIMUM_TRAINING_POINTS }
			?.let { points ->
				return this.copy(
					healthStatistics = healthStatistics.copy(
						trainingPoints = points + 1,
						trainingStatus = if (points + 1 > UNTRAINED_THRESHOLD) "Trained" else "Untrained"
					)
				)
			}
			?: this
}
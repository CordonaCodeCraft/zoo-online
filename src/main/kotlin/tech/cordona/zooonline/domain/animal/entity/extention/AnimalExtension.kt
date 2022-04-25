package tech.cordona.zooonline.domain.animal.entity.extention

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.model.AnimalToVisitor

object AnimalExtension {

	fun Animal.toVisitor() = AnimalToVisitor(
		name = this.name,
		gender = this.gender,
		age = this.age,
		weight = this.weight
	)
}
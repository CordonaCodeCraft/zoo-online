package tech.cordona.zooonline.domain.animal.service

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.animal.entity.Animal

interface AnimalService {
	fun saveAnimal(animal: Animal): Animal
	fun saveAllAnimals(animals: List<Animal>): List<Animal>
	fun findAnimalsByIds(ids : MutableSet<ObjectId>) : List<Animal>
	fun findAllAnimals(): List<Animal>
	fun deleteAllAnimals()
}
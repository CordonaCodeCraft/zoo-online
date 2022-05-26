package tech.cordona.zooonline.domain.animal.service

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.animal.entity.Animal

interface AnimalService {
	fun create(newAnimal: Animal): Animal
	fun createMany(newAnimals: List<Animal>): List<Animal>
	fun updateMany(updatedAnimals : List<Animal>) : List<Animal>
	fun findAll(): List<Animal>
	fun findById(id: ObjectId): Animal
	fun findAllByIds(ids: List<String>): List<Animal>
	fun findAllBySpecie(specie: String): List<Animal>
	fun deleteAll()
}
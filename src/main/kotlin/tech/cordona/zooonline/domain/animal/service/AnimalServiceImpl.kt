package tech.cordona.zooonline.domain.animal.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.repository.AnimalRepository

@Service
class AnimalServiceImpl(private val repository: AnimalRepository) : AnimalService {

	override fun saveAnimal(animal: Animal) = repository.save(animal)

	override fun saveAllAnimals(animals: List<Animal>): MutableList<Animal> = repository.saveAll(animals)

	override fun findAnimalsByIds(ids: MutableSet<ObjectId>): List<Animal> =
		repository.findAllById(ids.map { id -> id.toString() }).toList()

	override fun findAllAnimals(): List<Animal> = repository.findAll()

	override fun deleteAllAnimals() = repository.deleteAll()
}
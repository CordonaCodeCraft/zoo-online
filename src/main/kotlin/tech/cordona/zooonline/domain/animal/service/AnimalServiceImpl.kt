package tech.cordona.zooonline.domain.animal.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.repository.AnimalRepository

@Service
class AnimalServiceImpl(private val repository: AnimalRepository) : AnimalService {

	override fun save(animal: Animal) = repository.save(animal)

	override fun saveAll(animals: List<Animal>): MutableList<Animal> = repository.saveAll(animals)

	override fun findAll(): List<Animal> = repository.findAll()

	override fun deleteAll() = repository.deleteAll()
}
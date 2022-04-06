package tech.cordona.zooonline.domain.animal.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.repository.AnimalRepository

@Service
class AnimalServiceImpl @Autowired constructor(val repository: AnimalRepository) : AnimalService {

	override fun save(animal: Animal): Animal = repository.save(animal)

	override fun saveAll(animals: List<Animal>): List<Animal> = repository.saveAll(animals)

	override fun findAll(): List<Animal> = repository.findAll()
}
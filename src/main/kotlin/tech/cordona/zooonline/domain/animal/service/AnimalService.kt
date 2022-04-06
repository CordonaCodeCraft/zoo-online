package tech.cordona.zooonline.domain.animal.service

import tech.cordona.zooonline.domain.animal.entity.Animal

interface AnimalService {
	fun save(animal: Animal): Animal
	fun saveAll(animals: List<Animal>): List<Animal>
	fun findAll(): List<Animal>
}
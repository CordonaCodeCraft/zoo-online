package tech.cordona.zooonline.domain.animals.service

import tech.cordona.zooonline.domain.animals.entity.Animal

interface AnimalService {

	fun saveAll(animals: List<Animal>): List<Animal>

	fun findAll() : List<Animal>
}
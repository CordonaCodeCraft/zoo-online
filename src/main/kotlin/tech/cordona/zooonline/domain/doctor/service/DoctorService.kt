package tech.cordona.zooonline.domain.doctor.service

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.doctor.entity.Doctor

interface DoctorService {
	fun deleteAll()
	fun create(newDoctor: Doctor): Doctor
	fun findByUserId(userId: String): Doctor
	fun healAnimals(userId: String, animals: List<String>): List<Animal>
}
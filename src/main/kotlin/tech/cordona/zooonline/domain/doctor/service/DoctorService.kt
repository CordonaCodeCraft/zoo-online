package tech.cordona.zooonline.domain.doctor.service

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.doctor.entity.Doctor
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest

interface DoctorService {
	fun deleteAll()
	fun create(newDoctor: Doctor): Doctor
	fun findByDoctorId(doctorId: String): Doctor
	fun findByUserId(userId: String): Doctor
	fun healAnimals(userId: String, animals: List<String>): List<Animal>
	fun reassignDoctor(request: ReassignEmployeeRequest): Doctor
}
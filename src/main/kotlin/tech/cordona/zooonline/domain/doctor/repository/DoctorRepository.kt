package tech.cordona.zooonline.domain.doctor.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.doctor.entity.Doctor

interface DoctorRepository : MongoRepository<Doctor, String> {
	fun findByUserId(userId: ObjectId): Doctor?
	fun findById(doctorId: ObjectId): Doctor?
}
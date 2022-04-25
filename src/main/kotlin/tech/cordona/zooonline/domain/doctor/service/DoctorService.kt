package tech.cordona.zooonline.domain.doctor.service

import tech.cordona.zooonline.domain.doctor.entity.Doctor

interface DoctorService {
	fun deleteAll()
	fun create(newDoctor: Doctor): Doctor
}
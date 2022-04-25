package tech.cordona.zooonline.domain.doctor.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.doctor.entity.Doctor
import tech.cordona.zooonline.domain.doctor.repository.DoctorsRepository

@Service
class DoctorServiceImpl(private val repository: DoctorsRepository) : DoctorService {
	override fun deleteAll() = repository.deleteAll()
	override fun create(newDoctor: Doctor): Doctor = repository.save(newDoctor)
}
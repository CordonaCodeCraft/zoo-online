package tech.cordona.zooonline.domain.doctor.repository

import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.domain.doctor.entity.Doctor

interface DoctorsRepository : MongoRepository<Doctor, String>
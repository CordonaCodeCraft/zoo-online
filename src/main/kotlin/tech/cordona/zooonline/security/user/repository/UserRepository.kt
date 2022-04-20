package tech.cordona.zooonline.security.user.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import tech.cordona.zooonline.security.user.entity.User

interface UserRepository : MongoRepository<User, String> {
	fun findByEmail(email: String): User?
	fun findById(id: ObjectId): User?
}
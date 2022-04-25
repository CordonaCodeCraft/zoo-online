package tech.cordona.zooonline.domain.guard.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.guard.entity.Guard
import tech.cordona.zooonline.domain.guard.repository.GuardsRepository

@Service
class GuardServiceImpl(private val repository: GuardsRepository) : GuardService {

	override fun create(newGuard: Guard): Guard = repository.save(newGuard)

	override fun findGuardByUserId(userId: String): Guard =
		repository.findGuardByUserId(ObjectId(userId))
			?: throw throw IllegalArgumentException("Guard with ID $userId not found")

	override fun deleteAll() = repository.deleteAll()
}
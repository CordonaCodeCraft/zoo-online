package tech.cordona.zooonline.domain.guard.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.guard.entity.Guard
import tech.cordona.zooonline.domain.guard.repository.GuardsRepository

@Service
class GuardServiceImpl(private val repository: GuardsRepository) : GuardService {
	override fun deleteAll() = repository.deleteAll()
	override fun create(newGuard: Guard): Guard = repository.save(newGuard)
}
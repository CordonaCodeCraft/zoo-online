package tech.cordona.zooonline.domain.guard.service

import tech.cordona.zooonline.domain.guard.entity.Guard
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest

interface GuardService {
	fun create(newGuard: Guard): Guard
	fun findByGuardId(guardId: String): Guard
	fun findByUserId(userId: String): Guard
	fun deleteAll()
	fun reassignGuard(request: ReassignEmployeeRequest): Guard
}
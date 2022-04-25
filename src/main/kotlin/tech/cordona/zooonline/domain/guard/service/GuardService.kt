package tech.cordona.zooonline.domain.guard.service

import tech.cordona.zooonline.domain.guard.entity.Guard

interface GuardService {
	fun create(newGuard: Guard): Guard
	fun findGuardByUserId(userId: String): Guard
	fun deleteAll()
}
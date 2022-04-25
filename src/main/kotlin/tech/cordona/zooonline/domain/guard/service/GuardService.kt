package tech.cordona.zooonline.domain.guard.service

import tech.cordona.zooonline.domain.guard.entity.Guard

interface GuardService {
	fun deleteAll()
	fun create(newGuard: Guard): Guard
}
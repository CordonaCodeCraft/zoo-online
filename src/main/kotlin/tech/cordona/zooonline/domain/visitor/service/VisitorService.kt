package tech.cordona.zooonline.domain.visitor.service

import tech.cordona.zooonline.domain.visitor.entity.Visitor

interface VisitorService {
	fun create(newVisitor: Visitor): Visitor
	fun findVisitorByUserId(userId: String): Visitor
	fun addFavorites(userId: String, toBeAdded: Set<String>): Visitor
	fun removeFavorites(userId: String, toBeRemoved: Set<String>): Visitor
	fun deleteAll()
}
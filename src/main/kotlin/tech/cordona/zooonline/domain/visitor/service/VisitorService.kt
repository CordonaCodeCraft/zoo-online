package tech.cordona.zooonline.domain.visitor.service

import tech.cordona.zooonline.domain.visitor.entity.Visitor

interface VisitorService {
	fun create(newVisitor: Visitor): Visitor
	fun findVisitorByUserId(userId: String): Visitor
	fun addFavorites(userId: String, favorites: Set<String>) : Visitor
	fun removeFavorites(userId: String, toBeRemoved: Set<String>) : Visitor
}
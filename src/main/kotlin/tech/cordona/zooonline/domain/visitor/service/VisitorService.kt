package tech.cordona.zooonline.domain.visitor.service

import tech.cordona.zooonline.domain.visitor.entity.Visitor

interface VisitorService {
	fun create(newVisitor: Visitor): Visitor
	fun findVisitorByUserId(id: String): Visitor
	fun addFavorites(id: String, favorites: Set<String>) : Visitor
	fun removeFavorites(id: String, toBeRemoved: Set<String>) : Visitor
}
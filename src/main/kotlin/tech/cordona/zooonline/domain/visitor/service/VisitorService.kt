package tech.cordona.zooonline.domain.visitor.service

import tech.cordona.zooonline.domain.visitor.entity.Visitor

interface VisitorService {
	fun create(newVisitor: Visitor): Visitor
}
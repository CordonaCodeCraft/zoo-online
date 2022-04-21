package tech.cordona.zooonline.domain.visitor.service

import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.domain.visitor.repository.VisitorRepository

@Service
class VisitorServiceImpl(private val repository: VisitorRepository) : VisitorService {
	override fun create(newVisitor: Visitor): Visitor = repository.save(newVisitor)
}
package tech.cordona.zooonline.domain.visitor.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.domain.visitor.entity.extension.removeFavorites
import tech.cordona.zooonline.domain.visitor.entity.extension.updateFavorites
import tech.cordona.zooonline.domain.visitor.repository.VisitorRepository
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.validation.EntityValidator
import tech.cordona.zooonline.validation.FailReport.entityNotFound

@Service
class VisitorServiceImpl(private val repository: VisitorRepository) : VisitorService, EntityValidator() {

	override fun create(newVisitor: Visitor): Visitor =
		validate(newVisitor).let { repository.save(newVisitor) }

	override fun findVisitorByUserId(userId: String) =
		repository.findVisitorByUserId(ObjectId(userId))
			?: throw EntityNotFoundException(entityNotFound(entity = "Visitor", idType = "ID", id = userId))

	override fun addFavorites(userId: String, toBeAdded: Set<String>) =
		findVisitorByUserId(userId)
			.let { visitor ->
				visitor.whenValidFavorites(toBeAdded).updateFavorites(toBeAdded).also { repository.save(it) }
			}

	override fun removeFavorites(userId: String, toBeRemoved: Set<String>) =
		findVisitorByUserId(userId)
			.let { visitor ->
				visitor.whenValidFavorites(toBeRemoved).removeFavorites(toBeRemoved).also { repository.save(it) }
			}

	override fun deleteAll() = repository.deleteAll()

	private fun validate(newVisitor: Visitor) =
		newVisitor
			.withValidProperties()
			.forExistingUser()
			.forUniqueUser()
}
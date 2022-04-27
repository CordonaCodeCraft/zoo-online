package tech.cordona.zooonline.domain.visitor.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.domain.visitor.entity.extension.VisitorExtension.removeFavorites
import tech.cordona.zooonline.domain.visitor.entity.extension.VisitorExtension.updateFavorites
import tech.cordona.zooonline.domain.visitor.repository.VisitorsRepository

@Service
class VisitorServiceImpl(private val repository: VisitorsRepository) : VisitorService {

	override fun create(newVisitor: Visitor): Visitor = repository.save(newVisitor)

	override fun findVisitorByUserId(userId: String) =
		repository.findVisitorByUserId(ObjectId(userId))
			?: throw throw IllegalArgumentException("Visitor with ID $userId not found")

	override fun addFavorites(userId: String, favorites: Set<String>) =
		repository.findVisitorByUserId(ObjectId(userId))
			?.let { visitor ->
				visitor.updateFavorites(favorites).also { repository.save(it) }
			}
			?: throw throw IllegalArgumentException("Visitor with ID $userId not found")

	override fun removeFavorites(userId: String, toBeRemoved: Set<String>) =
		repository.findVisitorByUserId(ObjectId(userId))
			?.let { visitor ->
				visitor.removeFavorites(toBeRemoved).also { repository.save(it) }
			}
			?: throw throw IllegalArgumentException("Visitor with ID $userId not found")
}
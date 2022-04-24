package tech.cordona.zooonline.domain.visitor.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.domain.visitor.repository.VisitorsRepository
import tech.cordona.zooonline.Extensions.removeFavorites
import tech.cordona.zooonline.Extensions.updateFavorites

@Service
class VisitorServiceImpl(private val repository: VisitorsRepository) : VisitorService {

	override fun create(newVisitor: Visitor): Visitor = repository.save(newVisitor)

	override fun findVisitorByUserId(id: String) =
		repository.findVisitorByUserId(ObjectId(id))
			?: throw throw IllegalArgumentException("Visitor with ID $id not found")

	override fun addFavorites(id: String, favorites: Set<String>) =
		repository.findVisitorByUserId(ObjectId(id))
			?.let { visitor ->
				visitor.updateFavorites(favorites).also { repository.save(it) }
			}
			?: throw throw IllegalArgumentException("Visitor with ID $id not found")

	override fun removeFavorites(id: String, toBeRemoved: Set<String>) = repository.findVisitorByUserId(ObjectId(id))
		?.let { visitor ->
			visitor.removeFavorites(toBeRemoved).also { repository.save(it) }
		}
		?: throw throw IllegalArgumentException("Visitor with ID $id not found")
}
package tech.cordona.zooonline.bootstrap.mongock

import io.mongock.api.annotations.BeforeExecution
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackBeforeExecution
import io.mongock.api.annotations.RollbackExecution
import org.springframework.data.mongodb.core.MongoTemplate
import tech.cordona.zooonline.security.user.entity.Authority.ADMIN
import tech.cordona.zooonline.security.user.entity.Authority.MANAGER
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.service.UserService

@ChangeUnit(order = "2", id = "users-initialization", author = "Cordona")
class UsersInitializer(
	private val mongoTemplate: MongoTemplate,
	private val userService: UserService
) {

	@BeforeExecution
	fun beforeExecution() {
		mongoTemplate.dropCollection(USERS_COLLECTION)
		mongoTemplate.createCollection(USERS_COLLECTION)
	}

	@RollbackBeforeExecution
	fun rollbackBeforeExecution() {
		mongoTemplate.dropCollection(USERS_COLLECTION)
	}

	@RollbackExecution
	fun rollbackExecution() {
		userService.deleteAll()
	}

	@Execution
	fun execute() {
		listOf(
			User(
				firstName = "Antoan",
				lastName = "Bukluka",
				email = "myHobbyIsGettingPaid@bukluk.org",
				password = "ILoveToWorkAsHardAsPossibleAmaDrugPat123",
				authority = ADMIN,
				confirmed = true
			),
			User(
				firstName = "Pesho",
				lastName = "Shampoana",
				email = "mentoringVentsiForOneBetterFuture@daskal.info",
				password = "MasturbateWhileImagingUncleBobNaked456",
				authority = MANAGER,
				confirmed = true
			),
		).forEach { user -> userService.createUser(user) }
	}

	companion object {
		const val USERS_COLLECTION = "Users"
	}
}
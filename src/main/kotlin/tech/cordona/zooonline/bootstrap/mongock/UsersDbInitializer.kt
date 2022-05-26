package tech.cordona.zooonline.bootstrap.mongock

import io.mongock.api.annotations.BeforeExecution
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackBeforeExecution
import io.mongock.api.annotations.RollbackExecution
import org.springframework.data.mongodb.core.MongoTemplate
import tech.cordona.zooonline.domain.user.entity.Authority.ADMIN
import tech.cordona.zooonline.domain.user.entity.Authority.MANAGER
import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.service.UserService

@ChangeUnit(order = "2", id = "admin-and-manager-initialization", author = "Cordona")
class UsersDbInitializer(
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
	fun initialize() {
		listOf(
			User(
				firstName = "Antoan",
				middleName = "Trashov",
				lastName = "Buklukov",
				email = "myHobbyIsGettingPaid@bukluk.org",
				password = "ILoveToWorkAsHardAsPossibleAmaDrugPat123",
				authority = ADMIN,
				confirmed = true
			),
			User(
				firstName = "Pesho",
				middleName = "Mentorov",
				lastName = "Shampoanov",
				email = "mentoringVentsiForOneBetterFuture@daskal.info",
				password = "MasturbateWhileImagingUncleBobNaked456",
				authority = MANAGER,
				confirmed = true
			),
		)
			.also { users -> userService.createUsers(users) }
	}

	companion object {
		const val USERS_COLLECTION = "Users"
	}
}
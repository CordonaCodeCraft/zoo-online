package tech.cordona.zooonline.validation


import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.repository.AreaRepository
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.repository.CellRepository
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.repository.TaxonomyUnitRepository
import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.model.UserModel
import tech.cordona.zooonline.domain.user.repository.UserRepository
import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.domain.visitor.repository.VisitorRepository
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.extension.stringify
import tech.cordona.zooonline.validation.FailReport.animalNotFound
import tech.cordona.zooonline.validation.FailReport.entityNotFound
import tech.cordona.zooonline.validation.FailReport.existingArea
import tech.cordona.zooonline.validation.FailReport.existingCell
import tech.cordona.zooonline.validation.FailReport.existingEmail
import tech.cordona.zooonline.validation.FailReport.existingTaxonomyUnit
import tech.cordona.zooonline.validation.FailReport.existingUserId
import tech.cordona.zooonline.validation.FailReport.invalidEntity
import tech.cordona.zooonline.validation.FailReport.invalidTaxonomyDetails

@Component
abstract class EntityValidator {

	private val logging = KotlinLogging.logger { }

	@Autowired lateinit var validator: LocalValidatorFactoryBean
	@Autowired @Lazy lateinit var taxonomyUnitRepository: TaxonomyUnitRepository
	@Autowired @Lazy lateinit var cellRepository: CellRepository
	@Autowired @Lazy lateinit var animalService: AnimalService
	@Autowired @Lazy lateinit var areaRepository: AreaRepository
	@Autowired @Lazy lateinit var userRepository: UserRepository
	@Autowired @Lazy lateinit var visitorRepository: VisitorRepository

	protected fun validate(subject: Any) {
		validator.validate(subject)
			.filter { violation -> violation.invalidValue != null }
			.map { violation -> violation.message }
			.takeIf { it.isNotEmpty() }
			?.run {
				logging.error { invalidEntity(this) }
				throw InvalidEntityException(invalidEntity(this))
			}
	}

	private fun validateTaxonomyDetails(unitNames: List<String>) {
		unitNames
			.map { name -> taxonomyUnitRepository.findByName(name) }
			.takeUnless { retrieved -> retrieved.contains(null) }
			?: run {
				logging.error { invalidTaxonomyDetails() }
				throw EntityNotFoundException(invalidTaxonomyDetails())
			}
	}

	private fun validateTaxonomyUnitNameIsUnique(newUnit: TaxonomyUnit) =
		taxonomyUnitRepository.findByName(newUnit.name)
			?.run {
				logging.error { existingTaxonomyUnit(this.name) }
				throw InvalidEntityException(existingTaxonomyUnit(this.name))
			}
			?: this

	private fun validateCellSpecieIsUnique(newCell: Cell) =
		cellRepository.findBySpecie(newCell.specie)
			?.run {
				logging.error { existingCell(this.specie) }
				throw InvalidEntityException(existingCell(this.specie))
			}

	private fun validateAnimals(newCell: Cell) {
		newCell.species
			.takeIf { it.isNotEmpty() }
			?.stringify()
			?.let { animalsIds ->
				animalService.findAllByIds(animalsIds)
					.takeIf { retrieved -> retrieved.size == animalsIds.size }
					?: run {
						logging.error { animalNotFound() }
						throw EntityNotFoundException(animalNotFound())
					}
			}
	}

	private fun validateAreaNameIsUnique(newArea: Area) =
		areaRepository.findByName(newArea.name)
			?.run {
				logging.error { existingArea(this.name) }
				throw InvalidEntityException(existingArea(this.name))
			}

	private fun validateUsernameIsUnique(email: String) =
		userRepository.findByEmail(email)
			?.run {
				logging.error { existingEmail(this.email) }
				throw InvalidEntityException(existingEmail(this.email))
			}

	private fun validateUserIdIsUnique(userId: ObjectId) {
		visitorRepository.findVisitorByUserId(userId)
			?.run {
				logging.error { existingUserId(this.id.toString()) }
				throw InvalidEntityException(existingUserId(this.userId.toString()))
			}
	}

	private fun validateUserExists(userId: ObjectId) {
		userRepository.findById(userId)
			?: run {
			logging.error { entityNotFound(entity = "User", idType = "ID", id = userId.toString()) }
			throw EntityNotFoundException(entityNotFound(entity = "User", idType = "ID", id = userId.toString()))
		}
	}

	fun TaxonomyUnit.withValidProperties() = validate(this).let { this }
	fun TaxonomyUnit.withUniqueName() = validateTaxonomyUnitNameIsUnique(this).let { this }

	fun Animal.withValidProperties() = validate(this).let { this }
	fun Animal.withValidHealthStatistics() = validate(this.healthStatistics).let { this }
	fun Animal.withValidTaxonomyDetails() =
		validateTaxonomyDetails(listOf(this.taxonomyDetails.name, this.taxonomyDetails.parent)).let { this }

	fun Cell.withValidProperties() = validate(this).let { this }
	fun Cell.withUniqueSpecie() = validateCellSpecieIsUnique(this).let { this }
	fun Cell.withExistingSpecie() = validateTaxonomyDetails(listOf(this.specie)).let { this }
	fun Cell.withExistingAnimals() = validateAnimals(this).let { this }
	fun Cell.withValidTaxonomyDetails() =
		validateTaxonomyDetails(listOf(this.animalGroup, this.animalType)).let { this }

	fun Area.withValidProperties() = validate(this).let { this }
	fun Area.withUniqueName() = validateAreaNameIsUnique(this).let { this }
	fun Area.withExistingTaxonomyUnit() = validateTaxonomyDetails(listOf(this.name)).let { this }

	fun UserModel.withValidProperties() = validate(this).let { this }
	fun UserModel.withUniqueUsername() = validateUsernameIsUnique(this.email).let { this }

	fun User.withValidProperties() = validate(this).let { this }
	fun User.withUniqueUsername() = validateUsernameIsUnique(this.email).let { this }

	fun Visitor.withValidProperties() = validate(this).let { this }
	fun Visitor.forExistingUser() = validateUserExists(this.userId).let { this }
	fun Visitor.forUniqueUser() = validateUserIdIsUnique(this.userId).let { this }
	fun Visitor.whenValidFavorites(favorites: Set<String>) = validateTaxonomyDetails(favorites.toList()).let { this }
}
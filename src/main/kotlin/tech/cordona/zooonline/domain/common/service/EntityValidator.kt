package tech.cordona.zooonline.domain.common.service


import mu.KotlinLogging
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
import tech.cordona.zooonline.exception.EntityNotFoundException
import tech.cordona.zooonline.exception.InvalidEntityException
import tech.cordona.zooonline.extension.stringify

@Component
abstract class EntityValidator {

	private val logging = KotlinLogging.logger { }

	@Autowired lateinit var validator: LocalValidatorFactoryBean
	@Autowired @Lazy lateinit var taxonomyUnitRepository: TaxonomyUnitRepository
	@Autowired @Lazy lateinit var cellRepository: CellRepository
	@Autowired @Lazy lateinit var animalService: AnimalService
	@Autowired @Lazy lateinit var areaRepository: AreaRepository

	protected fun validate(subject: Any) {
		validator.validate(subject)
			.filter { violation -> violation.invalidValue != null }
			.map { violation -> violation.message }
			.takeIf { it.isNotEmpty() }
			?.run {
				logging.error { "Entity is not valid: ${this.joinToString(" ; ")}" }
				throw InvalidEntityException("Entity is not valid: ${this.joinToString(" ; ")}")
			}
	}

	private fun validateTaxonomyDetails(unitNames: List<String>) {
		unitNames
			.map { name -> taxonomyUnitRepository.findByName(name) }
			.takeUnless { retrieved -> retrieved.contains(null) }
			?: run {
				logging.error { "Invalid taxonomy unit" }
				throw EntityNotFoundException("Invalid taxonomy unit")
			}
	}

	private fun validateTaxonomyUnitNameIsUnique(newUnit: TaxonomyUnit) =
		taxonomyUnitRepository.findByName(newUnit.name)
			?.run {
				logging.error { "Taxonomy unit with name: ${this.name} already exists" }
				throw InvalidEntityException("Taxonomy unit with name: ${this.name} already exists")
			}
			?: this

	private fun validateCellSpecieIsUnique(newCell: Cell) =
		cellRepository.findBySpecie(newCell.specie)
			?.run {
				logging.error { "Cell with specie: ${this.specie} already exists" }
				throw InvalidEntityException("Cell with specie: ${this.specie} already exists")
			}

	private fun validateAnimals(newCell: Cell) {
		newCell.species
			.takeIf { it.isNotEmpty() }
			?.stringify()
			?.let { animalsIds ->
				animalService.findAllByIds(animalsIds)
					.takeIf { retrieved -> retrieved.size == animalsIds.size }
					?: run {
						logging.error { "Invalid animals ID(s)" }
						throw EntityNotFoundException("Invalid animals ID(s)")
					}
			}
	}

	private fun validateAreaNameIsUnique(newArea: Area) =
		areaRepository.findByName(newArea.name)
			?.run {
				logging.error { "Area with name: ${this.name} already exists" }
				throw InvalidEntityException("Area with name: ${this.name} already exists")
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
}
package tech.cordona.zooonline.domain.common.service


import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.exception.InvalidEntityException

@Component
abstract class EntityValidator {

	private val logging = KotlinLogging.logger { }

	@Autowired lateinit var validator: LocalValidatorFactoryBean
	@Autowired @Lazy lateinit var taxonomyUnitService: TaxonomyUnitService
	@Autowired @Lazy lateinit var cellService: CellService

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
			.map { name -> taxonomyUnitService.findByName(name) }
			.takeUnless { retrieved -> retrieved.contains(null) }
			?: run {
				logging.error { "Invalid taxonomy unit" }
				throw InvalidEntityException("Invalid taxonomy unit")
			}
	}

	private fun validateTaxonomyUnitNameIsUnique(newUnit: TaxonomyUnit) =
		taxonomyUnitService.findByName(newUnit.name)
			?.run {
				logging.error { "Taxonomy unit with name: ${this.name} already exists" }
				throw InvalidEntityException("Taxonomy unit with name: ${this.name} already exists")
			}
			?: this

	private fun validateUniqueSpecie(newCell: Cell) {
		cellService.findCellBySpecie(newCell.specie)
			.run {
				logging.error { "Cell with specie: ${this.specie} already exists" }
				throw InvalidEntityException("Cell with specie: ${this.specie} already exists")
			}
	}

	fun TaxonomyUnit.isValid() = validate(this).let { this }
	fun Animal.isValid() = validate(this).let { this }
	fun Cell.isValid() = validate(this).let { this }

	fun TaxonomyUnit.withUniqueName() = validateTaxonomyUnitNameIsUnique(this).let { this }
	fun Cell.withUniqueSpecie() = validateUniqueSpecie(this).let { this }
	fun Animal.withGoodHealthStatistics() = validate(this.healthStatistics).let { this }

	fun Animal.withGoodTaxonomyDetails() =
		validateTaxonomyDetails(listOf(this.taxonomyDetails.name, this.taxonomyDetails.parent)).let { this }

	fun Cell.withGoodTaxonomyDetails() =
		validateTaxonomyDetails(listOf(this.animalGroup, this.animalType)).let { this }
}
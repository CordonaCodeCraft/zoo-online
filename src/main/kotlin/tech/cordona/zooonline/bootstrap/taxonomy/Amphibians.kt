package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.generateTaxonomyUnits
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getChildrenIds
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getSpeciesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getTypesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.initializeTaxonomyMap
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Amphibian
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Amphibian.FROG
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Amphibian.SALAMANDER
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Group.AMPHIBIAN
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object Amphibians {

	private val amphibianTypes = Amphibian.values().map { enum -> enum.asString }.toList()

	private val taxonomyUnits = listOf(
		generateTaxonomyUnits(FROG.asString, FROG.species),
		generateTaxonomyUnits(SALAMANDER.asString, SALAMANDER.species)
	)

	private val amphibiansMap = initializeTaxonomyMap(AMPHIBIAN.asString, amphibianTypes, taxonomyUnits)

	val amphibianTaxonomyUnit = TaxonomyUnit(AMPHIBIAN.asString, ANIMAL.asString, getChildrenIds(getAmphibianTypes()))

	fun getAmphibianTypes() = getTypesFrom(amphibiansMap)
	fun getAmphibianSpecies() = getSpeciesFrom(amphibiansMap)
}
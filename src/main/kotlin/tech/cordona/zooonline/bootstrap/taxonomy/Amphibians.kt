package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Amphibian.FROG
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Amphibian.SALAMANDER
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Group.AMPHIBIAN
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Phylum.ANIMAL
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.generateTaxonomyUnits
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getChildrenIds
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getSpeciesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getTypesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.initializeTaxonomyMap
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object Amphibians {

	private val amphibianTypes = Taxonomy.Amphibian.values().map { enum -> enum.asString }.toList()

	private val frogSpecies = listOf("Golden Mantella", "Mountain Chicken", "Tomato Frog")
	private val salamanderSpecies = listOf("Hellbender", "Tiger Salamander", "Mudpuppy")

	private val taxonomyUnits = listOf(
		generateTaxonomyUnits(FROG.asString, frogSpecies),
		generateTaxonomyUnits(SALAMANDER.asString, salamanderSpecies)
	)

	private val amphibiansMap = initializeTaxonomyMap(AMPHIBIAN.asString, amphibianTypes, taxonomyUnits)

	val amphibianTaxonomyUnit = TaxonomyUnit(AMPHIBIAN.asString, ANIMAL.asString, getChildrenIds(getAmphibianTypes()))

	fun getAmphibianTypes() = getTypesFrom(amphibiansMap)
	fun getAmphibianSpecies() = getSpeciesFrom(amphibiansMap)
}
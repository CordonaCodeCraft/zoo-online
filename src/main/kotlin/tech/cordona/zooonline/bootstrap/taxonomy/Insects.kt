package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.generateTaxonomyUnits
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getChildrenIds
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getSpeciesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getTypesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.initializeTaxonomyMap
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Group.INSECT
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Insect
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Insect.SCORPION
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Insect.SPIDER
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object Insects {

	private val insectTypes = Insect.values().map { enum -> enum.asString }.toList()

	private val spiderSpecies = listOf("Black widow", "Brown widow", "Missouri tarantula")
	private val scorpionSpecies = listOf("Bark scorpion", "Emperor scorpion", "Whip scorpion")

	private val taxonomyUnits = listOf(
		generateTaxonomyUnits(SPIDER.asString, spiderSpecies),
		generateTaxonomyUnits(SCORPION.asString, scorpionSpecies)
	)

	private val insectsMap = initializeTaxonomyMap(INSECT.asString, insectTypes, taxonomyUnits)

	val insectTaxonomyUnit = TaxonomyUnit(INSECT.asString, ANIMAL.asString, getChildrenIds(getInsectTypes()))

	fun getInsectTypes() = getTypesFrom(insectsMap)
	fun getInsectSpecies() = getSpeciesFrom(insectsMap)
}
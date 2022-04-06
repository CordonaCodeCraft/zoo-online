package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Bird.CRANE
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Bird.GULL
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Bird.OWL
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Bird.PERCHING
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Bird.PREDATOR
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Group.BIRD
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Phylum.ANIMAL
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.generateTaxonomyUnits
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getChildrenIds
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getSpeciesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getTypesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.initializeTaxonomyMap
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object Birds {

	private val birdTypes = Taxonomy.Bird.values().map { enum -> enum.asString }.toList()

	private val craneSpecies = listOf("Sarus Crane", "Stanley Crane", "White-naped Crane")
	private val gullSpecies = listOf("Cape Thick-knee", "Horned Puffin", "Tufted Puffin")
	private val owlSpecies = listOf("Eastern Screech Owl", "Great Horned Owl")
	private val perchingSpecies = listOf("Bali Mynah", "Blue-faced Honeyeater", "Collie's Jay")
	private val predatorSpecies = listOf("Bald Eagle", "Bateleur Eagle", "Cinereous Vulture")

	private val taxonomyUnits = listOf(
		generateTaxonomyUnits(CRANE.asString, craneSpecies),
		generateTaxonomyUnits(GULL.asString, gullSpecies),
		generateTaxonomyUnits(OWL.asString, owlSpecies),
		generateTaxonomyUnits(PERCHING.asString, perchingSpecies),
		generateTaxonomyUnits(PREDATOR.asString, predatorSpecies)
	)

	private val birdsMap = initializeTaxonomyMap(BIRD.asString, birdTypes, taxonomyUnits)

	val birdTaxonomyUnit = TaxonomyUnit(BIRD.asString, ANIMAL.asString, getChildrenIds(getBirdTypes()))

	fun getBirdTypes() = getTypesFrom(birdsMap)
	fun getBirdSpecies() = getSpeciesFrom(birdsMap)
}
package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Group.MAMMAL
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Mammal.CARNIVORE
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Mammal.ELEPHANT
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Mammal.HOOFED
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Mammal.MONKEY
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Mammal.POUCHED
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Phylum.ANIMAL
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.generateTaxonomyUnits
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getChildrenIds
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getSpeciesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getTypesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.initializeTaxonomyMap
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object Mammals {

	private val mammalTypes = Taxonomy.Mammal.values().map { enum -> enum.asString }.toList()

	private val carnivoreSpecies = listOf(
		"Andean bear", "Grizzly bear", "Amur tiger", "Amur leopard",
		"Snow leopard", "Cheetah", "Jaguar", "Lion", "Puma", "Spotted hyena"
	)
	private val elephantSpecies = listOf("African elephant", "Indian elephant")
	private val monkeySpecies = listOf("Gorilla", "Proboscis", "Emperor tamarin", "Cotton top tamarin")
	private val hoofedSpecies = listOf("Addax", "Gerenuk", "Somali wild ass", "Gazelle")
	private val pouchedSpecies = listOf("Red kangaroo", "Tammar wallaby")

	private val taxonomyUnits = listOf(
		generateTaxonomyUnits(CARNIVORE.asString, carnivoreSpecies),
		generateTaxonomyUnits(ELEPHANT.asString, elephantSpecies),
		generateTaxonomyUnits(MONKEY.asString, monkeySpecies),
		generateTaxonomyUnits(HOOFED.asString, hoofedSpecies),
		generateTaxonomyUnits(POUCHED.asString, pouchedSpecies)
	)

	private val mammalsMap = initializeTaxonomyMap(MAMMAL.asString, mammalTypes, taxonomyUnits)

	val mammalTaxonomyUnit = TaxonomyUnit(MAMMAL.asString, ANIMAL.asString, getChildrenIds(getMammalTypes()))

	fun getMammalTypes() = getTypesFrom(mammalsMap)
	fun getMammalSpecies() = getSpeciesFrom(mammalsMap)
}
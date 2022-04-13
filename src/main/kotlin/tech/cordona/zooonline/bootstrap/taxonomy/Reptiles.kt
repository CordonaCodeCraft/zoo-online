package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.generateTaxonomyUnits
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getChildrenIds
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getSpeciesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.getTypesFrom
import tech.cordona.zooonline.bootstrap.taxonomy.TaxonomyUtils.initializeTaxonomyMap
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Group.REPTILE
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Reptile
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Reptile.ALLIGATOR
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Reptile.CROCODILE
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Reptile.LIZARD
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Reptile.SNAKE
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Reptile.TURTLE
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object Reptiles {

	private val reptileTypes = Reptile.values().map { enum -> enum.asString }.toList()

	private val alligatorSpecies = listOf("American Alligator", "Chinese Alligator")
	private val crocodileSpecies = listOf("Dwarf Caiman", "Malayan Gharial")
	private val lizardSpecies = listOf("Banded Gila Monster", "Chinese Crocodile Lizard", "Scheltopusik")
	private val snakeSpecies = listOf("Armenian Viper", "Ball Python", "King Cobra")
	private val turtleSpecies = listOf("Aldabra Tortoise", "Box Turtle", "Asian Giant Pond Turtle")

	private val taxonomyUnits = listOf(
		generateTaxonomyUnits(ALLIGATOR.asString, alligatorSpecies),
		generateTaxonomyUnits(CROCODILE.asString, crocodileSpecies),
		generateTaxonomyUnits(LIZARD.asString, lizardSpecies),
		generateTaxonomyUnits(SNAKE.asString, snakeSpecies),
		generateTaxonomyUnits(TURTLE.asString, turtleSpecies)
	)

	private val reptilesMap = initializeTaxonomyMap(REPTILE.asString, reptileTypes, taxonomyUnits)

	val reptileTaxonomyUnit = TaxonomyUnit(REPTILE.asString, ANIMAL.asString, getChildrenIds(getReptileTypes()))

	fun getReptileTypes() = getTypesFrom(reptilesMap)
	fun getReptileSpecies() = getSpeciesFrom(reptilesMap)
}
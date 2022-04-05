package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.generateTaxonomyCollection
import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.initializeTaxonomyMap
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Reptile.Reptile.*
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy.Group.REPTILE

object Reptile {

	private val reptiles = Reptile.values().map { enum -> enum.asString }.toList()

	private val alligatorSpecies = listOf("American Alligator", "Chinese Alligator")
	private val crocodileSpecies = listOf("Dwarf Caiman", "Malayan Gharial")
	private val lizardSpecies = listOf("Banded Gila Monster", "Chinese Crocodile Lizard", "Scheltopusik")
	private val snakeSpecies = listOf("Armenian Viper", "Ball Python", "King Cobra")
	private val turtleSpecies = listOf("Aldabra Tortoise", "Box Turtle", "Asian Giant Pond Turtle")

	private val taxonomyUnits = listOf(
		generateTaxonomyCollection(ALLIGATOR.asString, alligatorSpecies),
		generateTaxonomyCollection(CROCODILE.asString, crocodileSpecies),
		generateTaxonomyCollection(LIZARD.asString, lizardSpecies),
		generateTaxonomyCollection(SNAKE.asString, snakeSpecies),
		generateTaxonomyCollection(TURTLE.asString, turtleSpecies)
	)

	val reptilesMap = initializeTaxonomyMap(REPTILE.asString, reptiles, taxonomyUnits)

	private enum class Reptile(val asString: String) {
		ALLIGATOR("Alligator"),
		CROCODILE("Crocodile"),
		LIZARD("Lizard"),
		SNAKE("Snake"),
		TURTLE("Turtle")
	}

}
package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.generateTaxonomyCollection
import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.initializeTaxonomyMap
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Mammal.Mammal.*

object Mammal {

	private val mammals = values().map { enum -> enum.asString }.toList()

	private val carnivoreSpecies = listOf(
		"Andean bear", "Grizzly bear", "Amur tiger", "Amur leopard",
		"Snow leopard", "Cheetah", "Jaguar", "Lion", "Puma", "Spotted hyena"
	)
	private val elephantSpecies = listOf("African elephant", "Indian elephant")
	private val monkeySpecies = listOf("Gorilla", "Proboscis", "Emperor tamarin", "Cotton top tamarin")
	private val hoofedSpecies = listOf("Addax", "Gerenuk", "Somali wild ass", "Gazelle")
	private val pouchedSpecies = listOf("Red kangaroo", "Tammar wallaby")

	private val taxonomyUnits = listOf(
		generateTaxonomyCollection(CARNIVORE.asString, carnivoreSpecies),
		generateTaxonomyCollection(ELEPHANT.asString, elephantSpecies),
		generateTaxonomyCollection(MONKEY.asString, monkeySpecies),
		generateTaxonomyCollection(HOOFED.asString, hoofedSpecies),
		generateTaxonomyCollection(POUCHED.asString, pouchedSpecies)
	)

	val mammalsMap = initializeTaxonomyMap("Mammal", mammals, taxonomyUnits)

	private enum class Mammal(val asString: String) {
		CARNIVORE("Carnivore"),
		ELEPHANT("Elephant"),
		MONKEY("Monkey"),
		HOOFED("Hoofed"),
		POUCHED("Pouched")
	}
}
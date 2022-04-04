package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.bootstrap.objects.taxonomy.Bird.Bird.*
import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.generateTaxonomyCollection
import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.initializeTaxonomyMap

object Bird {

	private val birds = values().map { enum -> enum.asString }.toList()

	private val craneSpecies = listOf("Sarus Crane", "Stanley Crane", "White-naped Crane")
	private val gullSpecies = listOf("Cape Thick-knee", "Horned Puffin", "Tufted Puffin")
	private val owlSpecies = listOf("Eastern Screech Owl", "Great Horned Owl")
	private val perchingSpecies = listOf("Bali Mynah", "Blue-faced Honeyeater", "Collie's Jay")
	private val predatorSpecies = listOf("Bald Eagle", "Bateleur Eagle", "Cinereous Vulture")

	private val taxonomyUnits = listOf(
		generateTaxonomyCollection(CRANE.asString, craneSpecies),
		generateTaxonomyCollection(GULL.asString, gullSpecies),
		generateTaxonomyCollection(OWL.asString, owlSpecies),
		generateTaxonomyCollection(PERCHING.asString, perchingSpecies),
		generateTaxonomyCollection(PREDATOR.asString, predatorSpecies)
	)

	val birdsMap = initializeTaxonomyMap("Bird", birds, taxonomyUnits)

	private enum class Bird(val asString: String) {
		CRANE("Crane"),
		GULL("Gull"),
		OWL("Owl"),
		PERCHING("Perching"),
		PREDATOR("Predator")
	}
}
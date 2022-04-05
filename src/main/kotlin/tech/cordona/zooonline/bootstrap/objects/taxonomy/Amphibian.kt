package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.bootstrap.objects.taxonomy.Amphibian.Amphibian.FROG
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Amphibian.Amphibian.SALAMANDER
import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.generateTaxonomyCollection
import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.initializeTaxonomyMap
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy.Group.AMPHIBIAN

object Amphibian {

	private val amphibians = Amphibian.values().map { enum -> enum.asString }.toList()

	private val frogSpecies = listOf("Golden Mantella", "Mountain Chicken", "Tomato Frog")
	private val salamanderSpecies = listOf("Hellbender", "Tiger Salamander", "Mudpuppy")

	private val taxonomyUnits = listOf(
		generateTaxonomyCollection(FROG.asString, frogSpecies),
		generateTaxonomyCollection(SALAMANDER.asString, salamanderSpecies)
	)

	val amphibiansMap = initializeTaxonomyMap(AMPHIBIAN.asString, amphibians, taxonomyUnits)

	private enum class Amphibian(val asString: String) {
		FROG("Frog"),
		SALAMANDER("Salamander"),
	}
}
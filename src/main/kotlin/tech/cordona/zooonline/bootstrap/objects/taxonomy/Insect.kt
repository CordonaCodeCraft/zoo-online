package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.generateTaxonomyCollection
import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.initializeTaxonomyMap
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Insect.Insect.SCORPION
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Insect.Insect.SPIDER
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy.Type.INSECT

object Insect {

	private val insects = Insect.values().map { enum -> enum.asString }.toList()

	private val spiderSpecies = listOf("Black widow", "Brown widow", "Missouri tarantula")
	private val scorpionSpecies = listOf("Bark scorpion", "Emperor scorpion", "Whip scorpion")

	private val taxonomyUnits = listOf(
		generateTaxonomyCollection(SPIDER.asString, spiderSpecies),
		generateTaxonomyCollection(SCORPION.asString, scorpionSpecies)
	)

	val insectsMap = initializeTaxonomyMap(INSECT.asString, insects, taxonomyUnits)

	private enum class Insect(val asString: String) {
		SPIDER("Spider"),
		SCORPION("Scorpion"),
	}
}
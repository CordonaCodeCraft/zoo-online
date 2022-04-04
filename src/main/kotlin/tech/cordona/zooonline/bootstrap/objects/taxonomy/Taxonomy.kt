package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.getChildrenIds
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object Taxonomy {

	private val mammals = Mammal.mammalsMap
	private val birds = Bird.birdsMap
	private val reptiles = Reptile.reptilesMap
	private val insects = Insect.insectsMap
	private val amphibians = Amphibian.amphibiansMap

	fun getMammalSpecies() = mammals.values.flatten().toList()
	fun getBirdSpecies() = birds.values.flatten().toList()
	fun getReptileSpecies() = reptiles.values.flatten().toList()
	fun getInsectSpecies() = insects.values.flatten().toList()
	fun getAmphibianSpecies() = amphibians.values.flatten().toList()

	fun getMammals() = mammals.keys.toList()
	fun getBirds() = birds.keys.toList()
	fun getReptiles() = reptiles.keys.toList()
	fun getInsects() = insects.keys.toList()
	fun getAmphibians() = amphibians.keys.toList()

	val mammal = TaxonomyUnit("Mammal", "Animal", getChildrenIds(getMammals()))
	val bird = TaxonomyUnit("Bird", "Animal", getChildrenIds(getBirds()))
	val reptile = TaxonomyUnit("Reptile", "Animal", getChildrenIds(getReptiles()))
	val insect = TaxonomyUnit("Insect", "Animal", getChildrenIds(getInsects()))
	val amphibian = TaxonomyUnit("Amphibian", "Animal", getChildrenIds(getAmphibians()))

	val phylum = TaxonomyUnit("Animal", "Animalia", mutableSetOf(mammal.id, bird.id, reptile.id, insect.id, amphibian.id))
	val kingdom = TaxonomyUnit("Animalia", "Eukaryote", mutableSetOf(phylum.id))
	val domain = TaxonomyUnit("Eukaryote", "Life", mutableSetOf(kingdom.id))
}

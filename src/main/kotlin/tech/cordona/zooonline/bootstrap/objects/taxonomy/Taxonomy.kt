package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.bootstrap.objects.taxonomy.BootstrapUtils.getChildrenIds
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy.Domain.EUKARYOTE
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy.Group.*
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy.Kingdom.ANIMALIA
import tech.cordona.zooonline.bootstrap.objects.taxonomy.Taxonomy.Phylum.ANIMAL
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

	val mammal = TaxonomyUnit(MAMMAL.asString, ANIMAL.asString, getChildrenIds(getMammals()))
	val bird = TaxonomyUnit(BIRD.asString, ANIMAL.asString, getChildrenIds(getBirds()))
	val reptile = TaxonomyUnit(REPTILE.asString, ANIMAL.asString, getChildrenIds(getReptiles()))
	val insect = TaxonomyUnit(INSECT.asString, ANIMAL.asString, getChildrenIds(getInsects()))
	val amphibian = TaxonomyUnit(AMPHIBIAN.asString, ANIMAL.asString, getChildrenIds(getAmphibians()))

	val phylum =
		TaxonomyUnit(
			ANIMAL.asString,
			ANIMALIA.asString,
			mutableSetOf(mammal.id, bird.id, reptile.id, insect.id, amphibian.id)
		)
	val kingdom = TaxonomyUnit(ANIMALIA.asString, EUKARYOTE.asString, mutableSetOf(phylum.id))
	val domain = TaxonomyUnit(EUKARYOTE.asString, "Life", mutableSetOf(kingdom.id))

	enum class Domain(val asString: String) {
		EUKARYOTE("Eukaryote")
	}

	enum class Kingdom(val asString: String) {
		ANIMALIA("Animalia")
	}

	enum class Phylum(val asString: String) {
		ANIMAL("Animal")
	}

	enum class Group(val asString: String) {
		MAMMAL("Mammal"),
		BIRD("Bird"),
		REPTILE("Reptile"),
		INSECT("Insect"),
		AMPHIBIAN("Amphibian")
	}


}

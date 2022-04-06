package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Domain.EUKARYOTE
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Kingdom.ANIMALIA
import tech.cordona.zooonline.bootstrap.taxonomy.Taxonomy.Phylum.ANIMAL
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object Taxonomy {

	val phylum =
		TaxonomyUnit(
			ANIMAL.asString,
			ANIMALIA.asString,
			mutableSetOf(
				Mammals.mammalTaxonomyUnit.id,
				Birds.birdTaxonomyUnit.id,
				Reptiles.reptileTaxonomyUnit.id,
				Insects.insectTaxonomyUnit.id,
				Amphibians.amphibianTaxonomyUnit.id
			)
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

	enum class Mammal(val asString: String) {
		CARNIVORE("Carnivore"),
		ELEPHANT("Elephant"),
		MONKEY("Monkey"),
		HOOFED("Hoofed"),
		POUCHED("Pouched")
	}

	enum class Bird(val asString: String) {
		CRANE("Crane"),
		GULL("Gull"),
		OWL("Owl"),
		PERCHING("Perching"),
		PREDATOR("Predator")
	}

	enum class Reptile(val asString: String) {
		ALLIGATOR("Alligator"),
		CROCODILE("Crocodile"),
		LIZARD("Lizard"),
		SNAKE("Snake"),
		TURTLE("Turtle")
	}

	enum class Insect(val asString: String) {
		SPIDER("Spider"),
		SCORPION("Scorpion"),
	}

	enum class Amphibian(val asString: String) {
		FROG("Frog"),
		SALAMANDER("Salamander"),
	}
}

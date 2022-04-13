package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.bootstrap.taxonomy.enums.Domain.EUKARYOTE
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Kingdom.ANIMALIA
import tech.cordona.zooonline.bootstrap.taxonomy.enums.Phylum.ANIMAL
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
}


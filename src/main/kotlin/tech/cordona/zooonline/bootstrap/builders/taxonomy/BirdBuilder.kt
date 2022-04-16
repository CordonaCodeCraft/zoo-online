package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Bird
import tech.cordona.zooonline.domain.taxonomy.enums.Group.BIRD
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL

object BirdBuilder {

	private val birdSpecies: List<List<TaxonomyUnit>> = Bird.values()
		.map { parent ->
			parent.species
				.map { specie ->
					TaxonomyUnit(
						name = specie,
						parent = parent.asString,
						children = mutableSetOf()
					)
				}
		}

	private val map = TaxonomyUtils.buildTaxonomyMap(BIRD.asString, birdSpecies)

	val birdTaxonomyUnit = TaxonomyUnit(
		name = BIRD.asString,
		parent = ANIMAL.asString,
		children = TaxonomyUtils.getChildrenNames(getBirdTypes())
	)

	fun getBirdTypes() = TaxonomyUtils.getTypes(map)
	fun getBirdSpecies() = TaxonomyUtils.getSpecies(map)
}

package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Bird
import tech.cordona.zooonline.domain.taxonomy.enums.Group.BIRD
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.extension.asTitlecase

object BirdBuilder {

	private val birdSpecies: List<List<TaxonomyUnit>> = Bird.values()
		.map { parent ->
			parent.species
				.map { specie ->
					TaxonomyUnit(
						name = specie,
						parent = parent.name.asTitlecase(),
						children = mutableSetOf()
					)
				}
		}

	private val map = TaxonomyUtils.buildTaxonomyMap(BIRD.name.asTitlecase(), birdSpecies)

	val birdTaxonomyUnit = TaxonomyUnit(
		name = BIRD.name.asTitlecase(),
		parent = ANIMAL.name.asTitlecase(),
		children = TaxonomyUtils.getChildrenNames(getBirdTypes())
	)

	fun getBirdTypes() = TaxonomyUtils.getTypes(map)
	fun getBirdSpecies() = TaxonomyUtils.getSpecies(map)
}

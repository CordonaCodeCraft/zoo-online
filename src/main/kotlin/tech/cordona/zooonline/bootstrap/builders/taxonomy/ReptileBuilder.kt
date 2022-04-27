package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Group.REPTILE
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.domain.taxonomy.enums.Reptile
import tech.cordona.zooonline.extension.asTitlecase

object ReptileBuilder {

	private val reptileSpecies: List<List<TaxonomyUnit>> = Reptile.values()
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

	private val map = TaxonomyUtils.buildTaxonomyMap(REPTILE.name.asTitlecase(), reptileSpecies)

	val reptileTaxonomyUnit = TaxonomyUnit(
		name = REPTILE.name.asTitlecase(),
		parent = ANIMAL.name.asTitlecase(),
		children = TaxonomyUtils.getChildrenNames(getReptileTypes())
	)

	fun getReptileTypes() = TaxonomyUtils.getTypes(map)
	fun getReptileSpecies() = TaxonomyUtils.getSpecies(map)
}
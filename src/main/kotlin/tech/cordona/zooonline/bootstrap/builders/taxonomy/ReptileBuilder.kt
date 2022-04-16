package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Group.REPTILE
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.domain.taxonomy.enums.Reptile

object ReptileBuilder {

	private val reptileSpecies: List<List<TaxonomyUnit>> = Reptile.values()
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

	private val map = TaxonomyUtils.buildTaxonomyMap(REPTILE.asString, reptileSpecies)

	val reptileTaxonomyUnit = TaxonomyUnit(
		name = REPTILE.asString,
		parent = ANIMAL.asString,
		children = TaxonomyUtils.getChildrenNames(getReptileTypes())
	)

	fun getReptileTypes() = TaxonomyUtils.getTypes(map)
	fun getReptileSpecies() = TaxonomyUtils.getSpecies(map)
}
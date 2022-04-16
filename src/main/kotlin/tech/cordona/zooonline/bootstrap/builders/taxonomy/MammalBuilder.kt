package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Group.MAMMAL
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum

object MammalBuilder {

	private val mammalSpecies: List<List<TaxonomyUnit>> = Mammal.values()
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

	private val map = TaxonomyUtils.buildTaxonomyMap(MAMMAL.asString, mammalSpecies)

	val mammalTaxonomyUnit = TaxonomyUnit(
		name = MAMMAL.asString,
		parent = Phylum.ANIMAL.asString,
		children = TaxonomyUtils.getChildrenNames(getMammalTypes())
	)

	fun getMammalTypes() = TaxonomyUtils.getTypes(map)
	fun getMammalSpecies() = TaxonomyUtils.getSpecies(map)

}
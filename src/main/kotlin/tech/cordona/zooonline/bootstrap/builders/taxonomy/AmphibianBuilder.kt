package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Amphibian
import tech.cordona.zooonline.domain.taxonomy.enums.Group.AMPHIBIAN
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL

object AmphibianBuilder {

	private val amphibianSpecies: List<List<TaxonomyUnit>> = Amphibian.values()
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

	private val map = TaxonomyUtils.buildTaxonomyMap(AMPHIBIAN.asString, amphibianSpecies)

	val amphibianTaxonomyUnit = TaxonomyUnit(
		name = AMPHIBIAN.asString,
		parent = ANIMAL.asString,
		children = TaxonomyUtils.getChildrenNames(getAmphibianTypes())
	)

	fun getAmphibianTypes() = TaxonomyUtils.getTypes(map)
	fun getAmphibianSpecies() = TaxonomyUtils.getSpecies(map)
}
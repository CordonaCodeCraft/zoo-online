package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Amphibian
import tech.cordona.zooonline.domain.taxonomy.enums.Group.AMPHIBIAN
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.extension.StringExtension.asTitlecase

object AmphibianBuilder {

	private val amphibianSpecies: List<List<TaxonomyUnit>> = Amphibian.values()
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

	private val map = TaxonomyUtils.buildTaxonomyMap(AMPHIBIAN.name.asTitlecase(), amphibianSpecies)

	val amphibianTaxonomyUnit = TaxonomyUnit(
		name = AMPHIBIAN.name.asTitlecase(),
		parent = ANIMAL.name.asTitlecase(),
		children = TaxonomyUtils.getChildrenNames(getAmphibianTypes())
	)

	fun getAmphibianTypes() = TaxonomyUtils.getTypes(map)
	fun getAmphibianSpecies() = TaxonomyUtils.getSpecies(map)
}
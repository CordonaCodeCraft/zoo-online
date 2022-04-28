package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Group.INSECT
import tech.cordona.zooonline.domain.taxonomy.enums.Insect
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.extension.asTitlecase

object InsectBuilder {

	private val insectSpecies: List<List<TaxonomyUnit>> = Insect.values()
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

	private val map = TaxonomyUtils.buildTaxonomyMap(INSECT.name.asTitlecase(), insectSpecies)

	val insectTaxonomyUnit = TaxonomyUnit(
		name = INSECT.name.asTitlecase(),
		parent = ANIMAL.name.asTitlecase(),
		children = TaxonomyUtils.getChildrenNames(getInsectTypes())
	)

	fun getInsectTypes() = TaxonomyUtils.getTypes(map)
	fun getInsectSpecies() = TaxonomyUtils.getSpecies(map)
}
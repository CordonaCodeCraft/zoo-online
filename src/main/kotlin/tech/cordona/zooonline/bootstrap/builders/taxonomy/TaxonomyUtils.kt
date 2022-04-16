package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object TaxonomyUtils {

	fun buildTaxonomyMap(
		parent: String,
		species: List<List<TaxonomyUnit>>
	): Map<TaxonomyUnit, List<TaxonomyUnit>> {
		return species.associateBy {
			TaxonomyUnit(
				name = it.first().parent,
				parent = parent,
				children = getChildrenNames(it)
			)
		}
	}

	fun getTypes(map: Map<TaxonomyUnit, List<TaxonomyUnit>>) = map.keys.toList()

	fun getSpecies(map: Map<TaxonomyUnit, List<TaxonomyUnit>>) = map.values.flatten().toList()

	fun getChildrenNames(children: List<TaxonomyUnit>) = children.map { child -> child.name }.toMutableSet()
}
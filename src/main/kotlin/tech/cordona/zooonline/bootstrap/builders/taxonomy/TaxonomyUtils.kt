package tech.cordona.zooonline.bootstrap.builders.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object TaxonomyUtils {

	fun buildTaxonomyMap(
		parent: String,
		species: List<List<TaxonomyUnit>>
	): LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>> {
		val map: LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>> = LinkedHashMap()
		species
			.forEach { units ->
				val key = TaxonomyUnit(
					name = units[0].parent,
					parent = parent,
					children = getChildrenNames(units)
				)
				map.putIfAbsent(key, units)
			}
		return map
	}

	fun getTypes(map: LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>>) = map.keys.toList()

	fun getSpecies(map: LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>>) = map.values.flatten().toList()

	fun getChildrenNames(children: List<TaxonomyUnit>) = children.map { child -> child.name }.toMutableSet()
}
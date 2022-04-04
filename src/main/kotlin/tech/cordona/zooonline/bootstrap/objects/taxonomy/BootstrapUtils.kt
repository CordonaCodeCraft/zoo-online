package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object BootstrapUtils {

	fun generateTaxonomyCollection(parent: String, source: List<String>) =
		source.map { type -> TaxonomyUnit(type, parent, mutableSetOf()) }.toList()

	fun initializeTaxonomyMap(
		parent: String,
		source: List<String>,
		children: List<List<TaxonomyUnit>>
	): LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>> {
		val map = linkedMapOf<TaxonomyUnit, List<TaxonomyUnit>>()

		generateTaxonomyCollection(parent, source).map { unit -> map[unit] = listOf() }

		map
			.entries
			.forEach { entry ->
				map[entry.key] = findCollectionFor(entry.key.type, children)
				entry.key.children = getChildrenIds(entry.value)
			}

		return map
	}

	fun getChildrenIds(children: List<TaxonomyUnit>) =
		children.map { child -> child.id }.toMutableSet()

	private fun findCollectionFor(key: String, children: List<List<TaxonomyUnit>>) =
		children.first { subList -> subList[0].parent == key }
}
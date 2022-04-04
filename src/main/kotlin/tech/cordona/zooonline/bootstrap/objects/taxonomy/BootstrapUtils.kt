package tech.cordona.zooonline.bootstrap.objects.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object BootstrapUtils {

	fun generateTaxonomyCollection(parent: String, names: List<String>) =
		names.map { name -> TaxonomyUnit(name, parent, mutableSetOf()) }.toList()

	fun initializeTaxonomyMap(
		parent: String,
		names: List<String>,
		children: List<List<TaxonomyUnit>>
	): LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>> {
		val map = linkedMapOf<TaxonomyUnit, List<TaxonomyUnit>>()

		generateTaxonomyCollection(parent, names).map { unit -> map[unit] = listOf() }

		map
			.entries
			.forEach { entry ->
				map[entry.key] = findCollectionFor(entry.key.name, children)
				entry.key.children = getChildrenIds(entry.value)
			}

		return map
	}

	fun getChildrenIds(children: List<TaxonomyUnit>) =
		children.map { child -> child.id }.toMutableSet()

	private fun findCollectionFor(keyName: String, children: List<List<TaxonomyUnit>>) =
		children.first { subList -> subList[0].parent == keyName }
}
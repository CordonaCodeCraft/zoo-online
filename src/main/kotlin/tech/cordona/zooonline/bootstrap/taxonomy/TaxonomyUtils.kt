package tech.cordona.zooonline.bootstrap.taxonomy

import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit

object TaxonomyUtils {

	fun generateTaxonomyUnits(parent: String, source: List<String>) =
		source.map { name -> TaxonomyUnit(name, parent, mutableSetOf()) }.toList()

	fun initializeTaxonomyMap(
		group: String,
		types: List<String>,
		children: List<List<TaxonomyUnit>>
	): LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>> {
		val taxonomyMap = linkedMapOf<TaxonomyUnit, List<TaxonomyUnit>>()

		generateTaxonomyUnits(group, types).map { parent -> taxonomyMap[parent] = listOf() }

		taxonomyMap
			.entries
			.forEach { entry ->
				taxonomyMap[entry.key] = findCollectionFor(entry.key.name, children)
				entry.key.children = getChildrenIds(entry.value)
			}

		return taxonomyMap
	}

	fun getChildrenIds(children: List<TaxonomyUnit>) =
		children.map { child -> child.id }.toMutableSet()

	fun getTypesFrom(map: LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>>) = map.keys.toList()

	fun getSpeciesFrom(map: LinkedHashMap<TaxonomyUnit, List<TaxonomyUnit>>) = map.values.flatten().toList()

	private fun findCollectionFor(keyName: String, children: List<List<TaxonomyUnit>>) =
		children.first { subList -> subList[0].parent == keyName }
}
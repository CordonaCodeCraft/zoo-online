package tech.cordona.zooonline.common

import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Domain
import tech.cordona.zooonline.domain.taxonomy.enums.Group
import tech.cordona.zooonline.domain.taxonomy.enums.Kingdom
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum
import tech.cordona.zooonline.extension.asTitlecase

object TestAssets {

	val root = TaxonomyUnit(
		name = Domain.EUKARYOTE.name.asTitlecase(),
		parent = TaxonomyUnitsDbInitializer.ROOT,
		children = mutableSetOf()
	)

	val kingdom = TaxonomyUnit(
		name = Kingdom.ANIMALIA.name.asTitlecase(),
		parent = Domain.EUKARYOTE.name.asTitlecase(),
		children = mutableSetOf()
	)

	val phylym = TaxonomyUnit(
		name = Phylum.ANIMAL.name.asTitlecase(),
		parent = Kingdom.ANIMALIA.name.asTitlecase(),
		children = mutableSetOf()
	)

	val group = TaxonomyUnit(
		name = Group.MAMMAL.name.asTitlecase(),
		parent = Phylum.ANIMAL.name.asTitlecase(),
		children = mutableSetOf()
	)

	val type = TaxonomyUnit(
		name = Mammal.CARNIVORE.name.asTitlecase(),
		parent = Group.MAMMAL.name.asTitlecase(),
		children = mutableSetOf()
	)

	val andeanBearUnit = TaxonomyUnit(
		name = "Andean bear",
		parent = Mammal.CARNIVORE.name.asTitlecase(),
		children = mutableSetOf()
	)

	val grizzlyBearUnit = TaxonomyUnit(
		name = "Grizzly bear",
		parent = Mammal.CARNIVORE.name.asTitlecase(),
		children = mutableSetOf()
	)

	val amurTigerUnit = TaxonomyUnit(
		name = "Amur tiger",
		parent = Mammal.CARNIVORE.name.asTitlecase(),
		children = mutableSetOf()
	)

	val validChainOfUnits = listOf(root, kingdom, phylym, group, type, andeanBearUnit)
	val invalidChainOfUnits = listOf(phylym, kingdom, root)
	const val andeanBear = "Andean bear"
	const val grizzlyBear = "Grizzly bear"
	const val amurTiger = "Amur tiger"
	const val misspelled = "Misspelled"
	const val invalidLongName = "This is invalid name with length of more than 20 characters"
	const val invalidShortName = "No"

}
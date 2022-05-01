package tech.cordona.zooonline.common

import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.entity.enums.Gender
import tech.cordona.zooonline.domain.animal.entity.enums.HealthStatus
import tech.cordona.zooonline.domain.animal.entity.enums.TrainingStatus
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Domain.EUKARYOTE
import tech.cordona.zooonline.domain.taxonomy.enums.Group.MAMMAL
import tech.cordona.zooonline.domain.taxonomy.enums.Kingdom.ANIMALIA
import tech.cordona.zooonline.domain.taxonomy.enums.Mammal.CARNIVORE
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum.ANIMAL
import tech.cordona.zooonline.extension.asTitlecase

object TestAssets {

	val root = TaxonomyUnit(
		name = EUKARYOTE.name.asTitlecase(),
		parent = TaxonomyUnitsDbInitializer.ROOT,
		children = mutableSetOf()
	)

	val kingdom = TaxonomyUnit(
		name = ANIMALIA.name.asTitlecase(),
		parent = EUKARYOTE.name.asTitlecase(),
		children = mutableSetOf()
	)

	val phylum = TaxonomyUnit(
		name = ANIMAL.name.asTitlecase(),
		parent = ANIMALIA.name.asTitlecase(),
		children = mutableSetOf()
	)

	val group = TaxonomyUnit(
		name = MAMMAL.name.asTitlecase(),
		parent = ANIMAL.name.asTitlecase(),
		children = mutableSetOf()
	)

	 val carnivoreTU = TaxonomyUnit(
		name = CARNIVORE.name.asTitlecase(),
		parent = MAMMAL.name.asTitlecase(),
		children = mutableSetOf()
	)

	 val andeanBearTU = TaxonomyUnit(
		name = "Andean bear",
		parent = CARNIVORE.name.asTitlecase(),
		children = mutableSetOf()
	)

	val grizzlyBearTU = TaxonomyUnit(
		name = "Grizzly bear",
		parent = CARNIVORE.name.asTitlecase(),
		children = mutableSetOf()
	)

	val amurTigerTU = TaxonomyUnit(
		name = "Amur tiger",
		parent = CARNIVORE.name.asTitlecase(),
		children = mutableSetOf()
	)

	val healthStatistics = HealthStatistics(
		trainingPoints = 5,
		trainingStatus = TrainingStatus.TRAINED.name.asTitlecase(),
		healthPoints = 1,
		healthStatus = HealthStatus.SICK.name.asTitlecase()
	)

	val andeanBearSpecie = Animal(
		name = "Animal",
		age = 5,
		weight = 10.0,
		gender = Gender.MALE.name.asTitlecase(),
		taxonomyDetails = andeanBearTU,
		healthStatistics = healthStatistics,
		url = "https://www.animal.org/animal"
	)

	val validChainOfUnits = listOf(root, kingdom, phylum, group, carnivoreTU, andeanBearTU)
	val invalidChainOfUnits = listOf(phylum, kingdom, root)
	const val andeanBear = "Andean bear"
	const val grizzlyBear = "Grizzly bear"
	const val amurTiger = "Amur tiger"
	const val misspelled = "Misspelled"
	const val invalidLongName = "This is invalid name with length of more than 20 characters"
	const val invalidShortName = "No"
}
package tech.cordona.zooonline.common

import org.bson.types.ObjectId
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer.Companion.ROOT
import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.entity.enums.Gender.MALE
import tech.cordona.zooonline.domain.animal.entity.enums.HealthStatus.SICK
import tech.cordona.zooonline.domain.animal.entity.enums.TrainingStatus.UNTRAINED
import tech.cordona.zooonline.domain.animal.entity.structs.HealthStatistics
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.AreaStaff
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
		parent = ROOT,
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
		trainingStatus = UNTRAINED.name.asTitlecase(),
		healthPoints = 1,
		healthStatus = SICK.name.asTitlecase()
	)

	val andeanBearSpecie = Animal(
		name = "Animal",
		age = 5,
		weight = 10.0,
		gender = MALE.name.asTitlecase(),
		taxonomyDetails = andeanBearTU,
		healthStatistics = healthStatistics,
		url = "https://www.animal.org/animal"
	)

	val grizzlyBearSpecie = Animal(
		name = "Animal",
		age = 5,
		weight = 10.0,
		gender = MALE.name.asTitlecase(),
		taxonomyDetails = grizzlyBearTU,
		healthStatistics = healthStatistics,
		url = "https://www.animal.org/animal"
	)

	val staff = AreaStaff(
		trainers = mutableSetOf(),
		doctors = mutableSetOf(),
		guards = mutableSetOf()
	)
	val carnivoreArea = Area(
		name = CARNIVORE.name.asTitlecase(),
		cells = setOf(),
		staff = staff
	)

	const val ANDEAN_BEAR = "Andean bear"
	const val GRIZZLY_BEAR = "Grizzly bear"
	const val AMUR_TIGER = "Amur tiger"
	const val MISSPELLED = "Misspelled"
	const val INVALID_LONG_NAME = "This is invalid name with length of more than 30 characters"
	const val INVALID_SHORT_NAME = "No"

	val wrongID: ObjectId = ObjectId.get()
	val validGraphOfTaxonomyUnits = listOf(root, kingdom, phylum, group)
	val invalidGraphOfTaxonomyUnits = listOf(phylum, kingdom, root)
}
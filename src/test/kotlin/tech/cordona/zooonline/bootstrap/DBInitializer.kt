package tech.cordona.zooonline.bootstrap

import tech.cordona.zooonline.bootstrap.builders.animal.AnimalBuilder
import tech.cordona.zooonline.bootstrap.builders.area.AreaBuilder
import tech.cordona.zooonline.bootstrap.builders.cell.CellBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.AmphibianBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.BirdBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.InsectBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.MammalBuilder
import tech.cordona.zooonline.bootstrap.builders.taxonomy.ReptileBuilder
import tech.cordona.zooonline.bootstrap.mongock.StaffDbInitializer
import tech.cordona.zooonline.bootstrap.mongock.TaxonomyUnitsDbInitializer
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.extension.assignEmployee
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.doctor.service.DoctorService
import tech.cordona.zooonline.domain.guard.service.GuardService
import tech.cordona.zooonline.domain.taxonomy.entity.TaxonomyUnit
import tech.cordona.zooonline.domain.taxonomy.enums.Domain
import tech.cordona.zooonline.domain.taxonomy.enums.Kingdom
import tech.cordona.zooonline.domain.taxonomy.enums.Phylum
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.domain.trainer.service.TrainerService
import tech.cordona.zooonline.extension.asTitlecase
import tech.cordona.zooonline.extension.buildEmail
import tech.cordona.zooonline.extension.buildPassword
import tech.cordona.zooonline.extension.getFirstName
import tech.cordona.zooonline.extension.getLastName
import tech.cordona.zooonline.extension.getMiddleName
import tech.cordona.zooonline.extension.stringify
import tech.cordona.zooonline.security.user.entity.Authority
import tech.cordona.zooonline.security.user.entity.Authority.ADMIN
import tech.cordona.zooonline.security.user.entity.Authority.MANAGER
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.entity.extension.asDoctor
import tech.cordona.zooonline.security.user.entity.extension.asGuard
import tech.cordona.zooonline.security.user.entity.extension.asTrainer
import tech.cordona.zooonline.security.user.service.UserService

class DBInitializer(
	private val taxonomyUnitService: TaxonomyUnitService,
	private val animalService: AnimalService,
	private val cellService: CellService,
	private val areaService: AreaService,
	private val userService: UserService,
	private val trainerService: TrainerService,
	private val doctorService: DoctorService,
	private val guardService: GuardService
) {

	fun initializeDatabase() {
		listOf(
			listOf(
				TaxonomyUnit(
					name = Domain.EUKARYOTE.name.asTitlecase(),
					parent = TaxonomyUnitsDbInitializer.ROOT,
					children = mutableSetOf(Kingdom.ANIMALIA.name.asTitlecase())
				),
				TaxonomyUnit(
					name = Kingdom.ANIMALIA.name.asTitlecase(),
					parent = Domain.EUKARYOTE.name.asTitlecase(),
					children = mutableSetOf(Phylum.ANIMAL.name.asTitlecase())
				),
				TaxonomyUnit(
					name = Phylum.ANIMAL.name.asTitlecase(),
					parent = Kingdom.ANIMALIA.name.asTitlecase(),
					children = mutableSetOf(
						MammalBuilder.mammalTaxonomyUnit.name,
						BirdBuilder.birdTaxonomyUnit.name,
						ReptileBuilder.reptileTaxonomyUnit.name,
						InsectBuilder.insectTaxonomyUnit.name,
						AmphibianBuilder.amphibianTaxonomyUnit.name
					)
				)
			),
			listOf(
				MammalBuilder.mammalTaxonomyUnit,
				BirdBuilder.birdTaxonomyUnit,
				ReptileBuilder.reptileTaxonomyUnit,
				InsectBuilder.insectTaxonomyUnit,
				AmphibianBuilder.amphibianTaxonomyUnit
			),
			MammalBuilder.getMammalTypes(),
			BirdBuilder.getBirdTypes(),
			ReptileBuilder.getReptileTypes(),
			InsectBuilder.getInsectTypes(),
			AmphibianBuilder.getAmphibianTypes(),
			MammalBuilder.getMammalSpecies(),
			BirdBuilder.getBirdSpecies(),
			ReptileBuilder.getReptileSpecies(),
			InsectBuilder.getInsectSpecies(),
			AmphibianBuilder.getAmphibianSpecies()
		)
			.flatten()
			.also { taxonomyUnits -> taxonomyUnitService.createMany(taxonomyUnits) }
			.run {
				taxonomyUnitService.findAllAnimals()
					.map { animal -> AnimalBuilder.buildAnimals(animal, taxonomyUnitService) }
					.flatten()
					.let { animals -> animalService.createMany(animals) }
					.let { animals -> CellBuilder.buildCells(animals, taxonomyUnitService) }
					.let { cells -> cellService.createMany(cells) }
					.let { cells -> AreaBuilder.buildAreas(cells) }
					.let { areas -> areaService.createMany(areas) }
			}

		listOf(
			User(
				firstName = "Antoan",
				middleName = "Trashov",
				lastName = "Buklukov",
				email = "myHobbyIsGettingPaid@bukluk.org",
				password = "ILoveToWorkAsHardAsPossibleAmaDrugPat123",
				authority = ADMIN,
				confirmed = true
			),
			User(
				firstName = "Pesho",
				middleName = "Mentorov",
				lastName = "Shampoanov",
				email = "mentoringVentsiForOneBetterFuture@daskal.info",
				password = "MasturbateWhileImagingUncleBobNaked456",
				authority = MANAGER,
				confirmed = true
			),
		)
			.also { users -> userService.createUsers(users) }

		areaService.findAll()
			.forEach { area ->
				val animals = getAnimals(area)

				buildUser(Authority.TRAINER, StaffDbInitializer.trainersNames.peek())
					.let { userService.createUser(it) }
					.also {
						trainerService.create(it.asTrainer(area.name, animals))
							.also { trainer ->
								area.assignEmployee(trainer.position, trainer.id!!)
								StaffDbInitializer.trainersNames.pop()
							}
					}

				buildUser(Authority.DOCTOR, StaffDbInitializer.doctorsNames.peek())
					.let { userService.createUser(it) }
					.also {
						doctorService.create(it.asDoctor(area.name, animals))
							.also { doctor ->
								area.assignEmployee(doctor.position, doctor.id!!)
								StaffDbInitializer.doctorsNames.pop()
							}
					}

				buildUser(Authority.GUARD, StaffDbInitializer.guardNames.peek())
					.let { userService.createUser(it) }
					.also {
						guardService.create(it.asGuard(area.name, area.cells))
							.also { guard ->
								area.assignEmployee(guard.position, guard.id!!)
								StaffDbInitializer.guardNames.pop()
								areaService.update(area)
							}
					}
			}
	}

	private fun buildUser(authority: Authority, name: String) = User(
		firstName = name.getFirstName(),
		middleName = name.getMiddleName(),
		lastName = name.getLastName(),
		email = name.buildEmail(),
		password = name.buildPassword(),
		authority = authority,
		confirmed = true
	)

	private fun getAnimals(area: Area) =
		cellService.findAllById(area.cells.stringify())
			.map { cell -> animalService.findAllByIds(cell.species.stringify()) }
			.flatten()
			.map { it.id!! }
}
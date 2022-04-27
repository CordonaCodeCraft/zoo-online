package tech.cordona.zooonline.domain.visitor.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.domain.animal.entity.extension.AnimalExtension.toVisitor
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.extension.AreaExtension.toVisitor
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.entity.extension.CellExtension.toVisitor
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.common.controller.AbstractUserController
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.domain.visitor.controller.VisitorController.Companion.VISITOR_BASE_URL
import tech.cordona.zooonline.domain.visitor.dto.ModifyFavoritesRequest
import tech.cordona.zooonline.domain.visitor.service.VisitorService
import tech.cordona.zooonline.extension.Extensions.stringify
import tech.cordona.zooonline.extension.StringExtension.withEmptySpace
import tech.cordona.zooonline.security.annotation.IsUser

@IsUser
@RestController
@RequestMapping(VISITOR_BASE_URL)
class VisitorController(
	private val areaService: AreaService,
	private val cellService: CellService,
	private val animalService: AnimalService,
	private val visitorService: VisitorService,
	private val taxonomyUnitService: TaxonomyUnitService,
) : AbstractUserController() {

	@GetMapping("/areas")
	fun listAllAreas() =
		areaService.findAll().map { area -> area.toVisitor(withSpecies(area)) }

	@GetMapping("/areas/{animalType}")
	fun visitArea(@PathVariable animalType: String) =
		areaService.findAreaByName(animalType).let { area -> area.toVisitor(withSpecies(area)) }

	@GetMapping("/cells/{specie}")
	fun visitCell(@PathVariable specie: String) =
		cellService.findCellBySpecie(specie.withEmptySpace()).let { cell -> cell.toVisitor(withAnimals(cell)) }

	@GetMapping("/animals")
	fun listAllAnimals(): Map<String, List<String>> =
		taxonomyUnitService.findAllAnimals().let { animals ->
			animals
				.associate {
					it.parent to
							animals
								.filter { animal -> animal.parent == it.parent }
								.map { unit -> unit.name }
				}
		}

	@GetMapping("/favorites")
	fun getFavorites() =
		visitorService.findVisitorByUserId(getUserId())
			.let { "${it.firstName} ${it.lastName}'s favorites: ${it.favorites}" }

	@PostMapping("/favorites/add")
	fun addFavorites(@RequestBody request: ModifyFavoritesRequest) =
		visitorService.addFavorites(getUserId(), request.favorites)
			.let { "Added favorites ${request.favorites} to ${it.firstName} ${it.lastName}" }

	@PostMapping("/favorites/remove")
	fun removeFavorites(@RequestBody request: ModifyFavoritesRequest) =
		visitorService.removeFavorites(getUserId(), request.favorites)
			.let { "Removed ${request.favorites} from ${it.firstName} ${it.lastName}. Current favorites are: ${it.favorites}" }

//	private fun getId() = jwtTokenService.decodeToken(tokenWrapper.token).id

	private fun withSpecies(area: Area) = area.cells.stringify()
		.let { ids -> cellService.findAllById(ids) }
		.map { cell -> cell.specie }

	private fun withAnimals(cell: Cell) = cell.species.stringify()
		.let { ids -> animalService.findAllByIds(ids) }
		.map { animal -> animal.toVisitor() }

	companion object {
		const val VISITOR_BASE_URL = "/visitor"
	}
}
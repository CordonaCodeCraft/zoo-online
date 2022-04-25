package tech.cordona.zooonline.domain.visitor.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.Extensions.withEmptySpace
import tech.cordona.zooonline.domain.animal.entity.extention.AnimalExtension.toVisitor
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.entity.extention.AreaExtension.toVisitor
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.entity.extention.CellExtension.toVisitor
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.domain.visitor.controller.VisitorController.Companion.VISITOR_BASE_URL
import tech.cordona.zooonline.domain.visitor.dto.ModifyFavoritesRequest
import tech.cordona.zooonline.domain.visitor.service.VisitorService
import tech.cordona.zooonline.security.annotation.IsUser
import tech.cordona.zooonline.security.dto.TokenWrapper
import tech.cordona.zooonline.security.jwt.service.JwtTokenService

@IsUser
@RestController
@RequestMapping(VISITOR_BASE_URL)
class VisitorController(
	private val areaService: AreaService,
	private val cellService: CellService,
	private val animalService: AnimalService,
	private val visitorService: VisitorService,
	private val taxonomyUnitService: TaxonomyUnitService,
	private val tokenWrapper: TokenWrapper,
	private val jwtTokenService: JwtTokenService
) {

	@GetMapping("/areas")
	fun getAreas() =
		areaService.findAll().map { area -> area.toVisitor(withSpecies(area)) }

	@GetMapping("/areas/{animalType}")
	fun getArea(@PathVariable animalType: String) =
		areaService.findAreaByAnimalType(animalType).let { area -> area.toVisitor(withSpecies(area)) }

	@GetMapping("/cells/{specie}")
	fun getCell(@PathVariable specie: String) =
		cellService.findCellBySpecie(specie.withEmptySpace()).let { cell -> cell.toVisitor(withAnimals(cell)) }

	@GetMapping("/animals")
	fun getAllAnimals(): Map<String, List<String>> =
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
		visitorService.findVisitorByUserId(getId())
			.let { "${it.firstName} ${it.lastName}'s favorites: ${it.favorites}" }

	@PostMapping("/favorites/add")
	fun addFavorites(@RequestBody request: ModifyFavoritesRequest) =
		visitorService.addFavorites(getId(), request.favorites)
			.let { "Added favorites ${request.favorites} to ${it.firstName} ${it.lastName}" }

	@PostMapping("/favorites/remove")
	fun removeFavorites(@RequestBody request: ModifyFavoritesRequest) =
		visitorService.removeFavorites(getId(), request.favorites)
			.let { "Removed ${request.favorites} from ${it.firstName} ${it.lastName}. Current favorites are: ${it.favorites}" }

	private fun getId() = jwtTokenService.decodeToken(tokenWrapper.token).id

	private fun withSpecies(area: Area) = area.cells
		.map { id -> id.toString() }
		.let { ids -> cellService.findAllById(ids) }
		.map { cell -> cell.specie }

	private fun withAnimals(cell: Cell) = cell.species
		.map { id -> id.toString() }
		.let { ids -> animalService.findAllByIds(ids) }
		.map { animal -> animal.toVisitor() }

	companion object {
		const val VISITOR_BASE_URL = "/visitor"
	}
}
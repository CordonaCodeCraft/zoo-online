package tech.cordona.zooonline.api.visitor

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.api.visitor.VisitorController.Companion.VISITOR_BASE_URL
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.taxonomy.service.TaxonomyUnitService
import tech.cordona.zooonline.domain.visitor.dto.AddFavoritesRequest
import tech.cordona.zooonline.domain.visitor.dto.RemoveFavoritesRequest
import tech.cordona.zooonline.domain.visitor.service.VisitorService
import tech.cordona.zooonline.security.annotation.IsUser
import tech.cordona.zooonline.security.dto.TokenWrapper
import tech.cordona.zooonline.security.jwt.service.JwtTokenService
import tech.cordona.zooonline.security.user.mapper.Extensions.toVisitor
import tech.cordona.zooonline.security.user.mapper.Extensions.withEmptySpace

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
	fun getFavorites() = visitorService.findVisitorByUserId(extractIdFromToken()).favorites

	@PostMapping("/favorites/add")
	fun addFavorites(@RequestBody request: AddFavoritesRequest) =
		visitorService.addFavorites(extractIdFromToken(), request.favorites)

	@PostMapping("/favorites/remove")
	fun removeFavorites(@RequestBody request: RemoveFavoritesRequest) =
		visitorService.removeFavorites(extractIdFromToken(), request.toBeRemoved)

	private fun extractIdFromToken() = jwtTokenService.decodeToken(tokenWrapper.token).id

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
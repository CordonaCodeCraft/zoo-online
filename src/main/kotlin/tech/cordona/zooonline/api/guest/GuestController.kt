package tech.cordona.zooonline.api.guest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.domain.area.models.AreaConverter
import tech.cordona.zooonline.domain.cell.models.CellConverter

@RestController
@RequestMapping("/api/guest/")
class GuestController(
	private val areaConverter: AreaConverter,
	private val cellConverter: CellConverter
) {

	@GetMapping("areas/{animalType}")
	fun getArea(@PathVariable animalType: String) = areaConverter.areaToVisitor(animalType)

	@GetMapping("areas")
	fun getAreas() = areaConverter.areasToVisitor()

	@GetMapping("cells/{specie}")
	fun getCell(@PathVariable specie: String) = cellConverter.cellToVisitor(specie)
}
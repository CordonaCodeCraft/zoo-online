package tech.cordona.zooonline.domain.guard.controller

import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.domain.area.dto.PatrolAreasRequest
import tech.cordona.zooonline.domain.area.entity.extension.AreaExtension.toGuard
import tech.cordona.zooonline.domain.area.model.AreaToGuard
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.cell.entity.extension.CellExtension.toGuard
import tech.cordona.zooonline.domain.cell.model.CellToGuard
import tech.cordona.zooonline.domain.cell.service.CellService
import tech.cordona.zooonline.domain.common.controller.AbstractUserController
import tech.cordona.zooonline.domain.guard.controller.GuardController.Companion.GUARD_BASE_URL
import tech.cordona.zooonline.domain.guard.service.GuardService
import tech.cordona.zooonline.extension.Extensions.stringify
import tech.cordona.zooonline.security.annotation.IsGuard

@IsGuard
@RestController
@RequestMapping(GUARD_BASE_URL)
class GuardController(
	private val guardService: GuardService,
	private val areaService: AreaService,
	private val cellService: CellService,
) : AbstractUserController() {

	@GetMapping("/patrol-own-area")
	fun patrolOwnArea(): AreaToGuard {
		val guard = guardService.findByUserId(getUserId())
		return guard.area
			.let { areaService.findAreaByName(it) }.toGuard(withCells(guard.cells))
	}

	@GetMapping("/patrol-areas")
	fun patrolAreas(@RequestBody request: PatrolAreasRequest): List<AreaToGuard> =
		areaService.findAllByNames(request.areaNames)
			.map { area -> area.toGuard(withCells(area.cells)) }

	@GetMapping("/patrol-all-areas")
	fun patrolAllAreas(): List<AreaToGuard> =
		areaService.findAll()
			.map { area -> area.toGuard(withCells(area.cells)) }


	private fun withCells(cells: Set<ObjectId>): List<CellToGuard> =
		cellService.findAllById(cells.toList().stringify()).map { it.toGuard() }

	companion object {
		const val GUARD_BASE_URL = "/guard"
	}
}
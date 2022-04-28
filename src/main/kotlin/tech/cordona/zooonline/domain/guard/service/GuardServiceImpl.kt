package tech.cordona.zooonline.domain.guard.service

import mu.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import tech.cordona.zooonline.domain.area.entity.extension.assignEmployee
import tech.cordona.zooonline.domain.area.entity.extension.removeEmployee
import tech.cordona.zooonline.domain.area.service.AreaService
import tech.cordona.zooonline.domain.guard.entity.Guard
import tech.cordona.zooonline.domain.guard.entity.extension.reassigned
import tech.cordona.zooonline.domain.guard.repository.GuardRepository
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest

@Service
class GuardServiceImpl(
	private val repository: GuardRepository,
	private val areaService: AreaService,
) : GuardService {

	val logging = KotlinLogging.logger {}


	override fun create(newGuard: Guard): Guard = repository.save(newGuard)

	override fun findByGuardId(guardId: String): Guard =
		repository.findById(ObjectId(guardId))
			?: run {
				logging.error { "Guard with ID: $guardId not found" }
				throw IllegalArgumentException("Guard with ID: $guardId not found")
			}

	override fun findByUserId(userId: String): Guard =
		repository.findGuardByUserId(ObjectId(userId))
			?: run {
				logging.error { "Guard with ID: $userId not found" }
				throw IllegalArgumentException("Guard with ID: $userId not found")
			}

	override fun deleteAll() = repository.deleteAll()

	override fun reassignGuard(request: ReassignEmployeeRequest) =
		findByGuardId(request.employeeId)
			.also { guard ->
				areaService.findAreaByName(request.fromArea)
					.removeEmployee(request.position, guard.id!!)
					.also { fromArea -> areaService.save(fromArea) }
			}
			.also { guard ->
				areaService.findAreaByName(request.toArea)
					.assignEmployee(request.position, guard.id!!)
					.also { toArea -> areaService.save(toArea) }
					.let { toArea ->
						repository.save(guard.reassigned(toArea, toArea.cells))
					}
			}
}
package tech.cordona.zooonline.domain.manager.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.Extensions.asTitlecase
import tech.cordona.zooonline.domain.common.controller.AbstractUserController
import tech.cordona.zooonline.domain.common.entity.Employee
import tech.cordona.zooonline.domain.doctor.service.DoctorService
import tech.cordona.zooonline.domain.guard.service.GuardService
import tech.cordona.zooonline.domain.manager.controller.ManagerController.Companion.MANAGER_BASE_URL
import tech.cordona.zooonline.domain.manager.dto.ReassignEmployeeRequest
import tech.cordona.zooonline.domain.trainer.service.TrainerService
import tech.cordona.zooonline.security.annotation.IsManager
import tech.cordona.zooonline.security.user.entity.Authority.DOCTOR
import tech.cordona.zooonline.security.user.entity.Authority.TRAINER

@IsManager
@RestController
@RequestMapping(MANAGER_BASE_URL)
class ManagerController(
	private val trainerService: TrainerService,
	private val doctorService: DoctorService,
	private val guardService: GuardService,
) : AbstractUserController() {

	@PostMapping("/reassign-employee")
	fun reassign(@RequestBody request: ReassignEmployeeRequest) =
		when (request.position) {
			TRAINER.name.asTitlecase() -> report(trainerService.reassignTrainer(request), request)
			DOCTOR.name.asTitlecase() -> report(doctorService.reassignDoctor(request), request)
			else -> report(guardService.reassignGuard(request), request)
		}

	private fun report(employee: Employee, request: ReassignEmployeeRequest) =
		"Reassigned ${employee.position} ${employee.firstName} ${employee.lastName} from ${request.fromArea} area to ${request.toArea} area"

	companion object {
		const val MANAGER_BASE_URL = "/manager"
	}
}
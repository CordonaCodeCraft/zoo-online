package tech.cordona.zooonline.domain.doctor.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.cordona.zooonline.domain.animal.entity.extension.toDoctor
import tech.cordona.zooonline.domain.animal.service.AnimalService
import tech.cordona.zooonline.domain.common.controller.AbstractUserController
import tech.cordona.zooonline.domain.doctor.controller.DoctorController.Companion.DOCTOR_BASE_URL
import tech.cordona.zooonline.domain.doctor.dto.HealAnimalsRequest
import tech.cordona.zooonline.domain.doctor.service.DoctorService
import tech.cordona.zooonline.extension.stringify
import tech.cordona.zooonline.security.annotation.IsDoctor

@IsDoctor
@RestController
@RequestMapping(DOCTOR_BASE_URL)
class DoctorController(
	private val doctorService: DoctorService,
	private val animalService: AnimalService
) : AbstractUserController() {

	@GetMapping("/my-animals")
	fun getAnimals() =
		doctorService.findByUserId(getUserId())
			.let { animalService.findAllByIds(it.animals.stringify()) }
			.map { it.toDoctor() }

	@GetMapping("all-animals")
	fun getAllAnimals() = animalService.findAll().map { it.toDoctor() }

	@GetMapping("/report-my-sick-animals")
	fun reportMySickAnimals() =
		doctorService.findByUserId(getUserId())
			.let { animalService.findAllByIds(it.animals.stringify()) }
			.filter { it.healthStatistics.healthStatus == "Sick" }
			.map { it.toDoctor() }

	@GetMapping("/report-all-sick")
	fun reportAllSickAnimals() =
		animalService.findAll()
			.filter { it.healthStatistics.healthStatus == "Sick" }
			.map { it.toDoctor() }

	@PostMapping("/heal")
	fun healAnimals(@RequestBody request: HealAnimalsRequest) =
		doctorService.healAnimals(getUserId(), request.animalsToBeHealed)
			.map { it.toDoctor() }

	companion object {
		const val DOCTOR_BASE_URL = "/doctor"
	}
}
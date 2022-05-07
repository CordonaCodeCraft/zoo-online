package tech.cordona.zooonline.domain.manager.dto

data class AssignEmployeeRequest(
	val position: String,
	val employeeId: String,
	val toArea: String
)
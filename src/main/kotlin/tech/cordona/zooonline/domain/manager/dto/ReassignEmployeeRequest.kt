package tech.cordona.zooonline.domain.manager.dto

data class ReassignEmployeeRequest(
	val position: String,
	val employeeId: String,
	val fromArea: String,
	val toArea: String
)
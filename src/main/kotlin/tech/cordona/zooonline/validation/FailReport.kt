package tech.cordona.zooonline.validation

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_NAME_LENGTH
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MAX_WEIGHT
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_NAME_LENGTH
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_WEIGHT

object FailReport {
	fun invalidEntity(violations: List<String>) = "Entity is not valid: ${violations.joinToString(" ; ")}"
	fun invalidTaxonomyDetails() = "Invalid taxonomy details"
	fun existingTaxonomyUnit(name: String) = "Taxonomy unit with name: $name already exists"
	fun existingCell(name: String) = "Cell with specie: $name already exists"
	fun animalNotFound() = "Animal(s) with provided ID(s) not found"
	fun existingArea(name: String) = "Area with name: $name already exists"
	fun existingEmail(email: String) = "User with username: $email already exists"
	fun entityNotFound(entity: String, idType: String, id: String) = "$entity with $idType: $id not found"
	fun invalidName() = "The name must be between $MIN_NAME_LENGTH and $MAX_NAME_LENGTH characters long"
	fun invalidAge() = "The animal's age must be an integer between $MIN_AGE and $MAX_AGE"
	fun invalidWeight() = "The animal's weight must be a double between $MIN_WEIGHT and $MAX_WEIGHT"
	fun invalidHealthPoints() = "The health points must be an integer between $MIN_HEALTH_POINTS and $MAX_HEALTH_POINTS"
	fun invalidTrainingPoints() =
		"The training points must be an integer between $MIN_TRAINING_POINTS and $MAX_TRAINING_POINTS"
	fun invalidURL() = "URL is not valid"
	fun invalidEmail() = "Entity is not valid: must be a well-formed email address"
}
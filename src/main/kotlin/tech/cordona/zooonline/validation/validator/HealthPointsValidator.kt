package tech.cordona.zooonline.validation.validator

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_HEALTH_POINTS
import tech.cordona.zooonline.validation.annotation.ValidHealthPoints
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class HealthPointsValidator : ConstraintValidator<ValidHealthPoints, Int> {
	override fun isValid(value: Int, context: ConstraintValidatorContext?) =
		value in MIN_HEALTH_POINTS..MAX_HEALTH_POINTS
}
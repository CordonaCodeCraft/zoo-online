package tech.cordona.zooonline.validation.validator

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_WEIGHT
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_WEIGHT
import tech.cordona.zooonline.validation.annotation.ValidWeight
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class WeightValidator : ConstraintValidator<ValidWeight, Double> {
	override fun isValid(value: Double, context: ConstraintValidatorContext?) =
		value in MIN_WEIGHT..MAX_WEIGHT
}
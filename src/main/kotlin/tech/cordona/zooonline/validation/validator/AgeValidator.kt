package tech.cordona.zooonline.validation.validator

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_AGE
import tech.cordona.zooonline.validation.annotation.ValidAge
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AgeValidator : ConstraintValidator<ValidAge, Int> {
	override fun isValid(value: Int, context: ConstraintValidatorContext?) =
		value in MIN_AGE..MAX_AGE
}
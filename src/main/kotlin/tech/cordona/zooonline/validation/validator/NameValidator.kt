package tech.cordona.zooonline.validation.validator

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_NAME_LENGTH
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_NAME_LENGTH
import tech.cordona.zooonline.validation.annotation.ValidName
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NameValidator : ConstraintValidator<ValidName, String> {
	override fun isValid(value: String, context: ConstraintValidatorContext?) =
		value.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH
}